package oripa.doc.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

import javax.vecmath.Vector2d;

import oripa.geom.GeomUtil;
import oripa.geom.OriLine;
import oripa.util.LazyList;
import oripa.util.concurrent.MultiInMultiOutProcessor;
import oripa.util.concurrent.MultiInProcess;
import oripa.util.concurrent.MultiInProcessFactory;

public class AddLine {
	class PointComparatorX implements Comparator<Vector2d> {

		@Override
		public int compare(Vector2d v1, Vector2d v2) {
			if(v1.x == v2.x){
				return 0;
			}
			return v1.x > v2.x ? 1 : -1;
		}
	}

	class PointComparatorY implements Comparator<Vector2d> {

		@Override
		public int compare(Vector2d v1, Vector2d v2) {
			if(v1.y == v2.y){
				return 0;
			}
			return ((Vector2d) v1).y > ((Vector2d) v2).y ? 1 : -1;
		}
	}


	/**
	 * Adds a new OriLine, also searching for intersections with others 
	 * that would cause their mutual division
	 * 
	 * @param inputLine
	 * @param currentLines	current line list. it will be affected as 
	 * 						new lines are added and unnecessary lines are removed.
	 */

	public void addLine(OriLine inputLine, LazyList<OriLine> currentLines) {
		//ArrayList<OriLine> crossingLines = new ArrayList<OriLine>(); // for debug? 

		ArrayList<Vector2d> points = new ArrayList<>(currentLines.size());
		points.add(inputLine.p0);
		points.add(inputLine.p1);

		// If it already exists, do nothing
		for (OriLine line : currentLines) {
			if (line.epsilonEquals(inputLine, CalculationResource.POINT_EPS_SQUARED)) {
				return;
			}
		}

		divideLines(inputLine, currentLines);
		
		MultiInProcessFactory<OriLine, Collection<Vector2d>> 
				factory = new CrossPointProcessFactory(inputLine);

		MultiInMultiOutProcessor<OriLine, Collection<Vector2d>> processor = new MultiInMultiOutProcessor<>(factory);

		for(Collection<Vector2d> crossPoints : processor.execute(currentLines, 4) ){
			points.addAll(crossPoints);
		}
			
		
		// the sort is done on longer direction in order to suppress underflow error???
		boolean sortByX = Math.abs(inputLine.p0.x - inputLine.p1.x) > Math.abs(inputLine.p0.y - inputLine.p1.y);
		if (sortByX) {
			Collections.sort(points, new PointComparatorX());
		} else {
			Collections.sort(points, new PointComparatorY());
		}

		Vector2d prevPoint = points.get(0);

		// add new lines sequentially
		for (int i = 1; i < points.size(); i++) {
			Vector2d p = points.get(i);
			// remove very short line
			if (GeomUtil.Distance(prevPoint, p) < CalculationResource.POINT_EPS) {
				continue;
			}

			currentLines.add(new OriLine(prevPoint, p, inputLine.typeVal));
			prevPoint = p;
		}
	}
	
	/**
	 * Divides lines by inputed line.
	 * @param inputLine
	 * @param currentLines
	 * @return always true
	 */
	public boolean divideLines(OriLine inputLine, LazyList<OriLine> currentLines){

		LinkedList<OriLine> toBeAdded = new LinkedList<>();

		int i = 0;
		// If it intersects other line, divide them
		while(i < currentLines.size()) {
			OriLine line = currentLines.get(i);


			// Inputed line does not intersect
			if (inputLine.typeVal == OriLine.TYPE_NONE && line.typeVal != OriLine.TYPE_NONE) {
				i++;
				continue;
			}
			Vector2d crossPoint = GeomUtil.getCrossPoint(inputLine, line);
			if (crossPoint == null) {
				i++;
				continue;
			}

			i = currentLines.lazyRemove(i);

			OriLine partialLine = createLine(line.p0, crossPoint, line.typeVal);
			if(partialLine != null) {
				toBeAdded.add(partialLine);
			}

			partialLine = createLine(line.p1, crossPoint, line.typeVal);
			if(partialLine != null) {
				toBeAdded.add(partialLine);
			}

			//crossingLines.add(line);
		}

		for(OriLine line : toBeAdded){
			currentLines.add(line);
		}

		return true;
	}
	
	private OriLine createLine(Vector2d p0, Vector2d p1, int typeVal){
		OriLine line = null;
//		if (p0.epsilonEquals(p1, CalculationResource.POINT_EPS) == false) {
		if(GeomUtil.DistanceSquared(p0, p1) > CalculationResource.POINT_EPS_SQUARED){
			line = new OriLine(p0, p1, typeVal);
		}
		
		return line;
	}
	

	
	private class CrossPointProcessFactory implements MultiInProcessFactory<OriLine, Collection<Vector2d>> {
		private OriLine inputLine;
		
		public CrossPointProcessFactory(OriLine line) {
			inputLine = line;
		}
		
		@Override
		public MultiInProcess<OriLine, Collection<Vector2d>> create() {
			return new CrossPointProcess(inputLine);
		}
	}
	
	private class CrossPointProcess extends MultiInProcess<OriLine, Collection<Vector2d>> {

		OriLine inputLine;
		
		public CrossPointProcess(OriLine inputLine) {
			super();
			this.inputLine = inputLine;
		}
				
		@Override
		public Collection<Vector2d> run(Collection<OriLine> values) {
			return createCrossPointList(inputLine, values);
		}
		
		public ArrayList<Vector2d> createCrossPointList(OriLine inputLine, Collection<OriLine> lines){
			ArrayList<Vector2d> points = new ArrayList<>();

			points.add(inputLine.p0);
			points.add(inputLine.p1);

			for (OriLine line : lines) {

				// Don't divide if the type of line is Aux
				if (inputLine.typeVal != OriLine.TYPE_NONE && 
						line.typeVal == OriLine.TYPE_NONE) {
					continue;
				}

				// If the intersection is on the end of the line, skip
				if (inputLine.p0.epsilonEquals(line.p0, CalculationResource.POINT_EPS_SQUARED) ||
						inputLine.p0.epsilonEquals(line.p1, CalculationResource.POINT_EPS_SQUARED)||
						inputLine.p1.epsilonEquals(line.p0, CalculationResource.POINT_EPS_SQUARED)||
						inputLine.p1.epsilonEquals(line.p1, CalculationResource.POINT_EPS_SQUARED) ) {
					continue;
				}

				if (GeomUtil.DistancePointToSegment(line.p0, inputLine.p0, inputLine.p1) < CalculationResource.POINT_EPS) {
					points.add(line.p0);
				}
				if (GeomUtil.DistancePointToSegment(line.p1, inputLine.p0, inputLine.p1) < CalculationResource.POINT_EPS) {
					points.add(line.p1);
				}

				// Calculates the intersection
				Vector2d crossPoint = GeomUtil.getCrossPoint(inputLine, line);
				if (crossPoint != null) {
					points.add(crossPoint);
				}

			}
			
			return points;
		}
	}
	

	

}
