package oripa.paint.geometry.nearest.point;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.List;

import javax.vecmath.Vector2d;

import oripa.ORIPA;
import oripa.geom.OriLine;
import oripa.paint.Globals;
import oripa.paint.PaintContext;
import oripa.util.concurrent.MultiInMultiOutProcessor;

public class NearestVertexFinder {
	private static FindNearestFromLinesFactory factory = new FindNearestFromLinesFactory(null);
	private static MultiInMultiOutProcessor<OriLine, Vector2d> processor = new MultiInMultiOutProcessor<>(factory);

	private static NearestPoint findNearestVertexFromLines(
			Point2D.Double p, List<OriLine> lines){

		factory.setTarget(p);

		NearestPoint minPosition = null;

		Collection<Vector2d> nearestCandidates = processor.execute(lines, 4);
		minPosition = findNearestVertex(p, nearestCandidates);
			
		
		return minPosition;

	}
	
	/**
	 * Find the nearest of p among vertices
	 * @param p
	 * @param vertices
	 * @return nearest point
	 */
	private static NearestPoint findNearestVertex(Point2D.Double p, Collection<Vector2d> vertices){

		NearestPoint minPosition = new NearestPoint();
		Vector2d target = new Vector2d(p.x, p.y);

		for(Vector2d vertex : vertices){
			minPosition.update(target, vertex);
		}

		return minPosition;
	}

	/**
	 * find the nearest of current mouse point in the circle whose radius = {@code distance}.
	 * @param context
	 * @param distance
	 * @return nearestPoint in the limit. null if there is no such vertex.
	 */
	public static NearestPoint findAround(PaintContext context, double distance){
		NearestPoint nearestPosition = new NearestPoint();

		Point2D.Double currentPoint = context.getLogicalMousePoint();
		
		
		Collection<Collection<Vector2d>> vertices = ORIPA.doc.getVerticesArea(
				currentPoint.x, currentPoint.y, distance);	
	
		for(Collection<Vector2d> locals : vertices){
			nearestPosition.update(findNearestVertex(currentPoint, locals));
		}

		if (context.dispGrid) {

			NearestPoint nearestGrid = findNearestVertex(
					currentPoint, context.updateGrids(Globals.gridDivNum));
			
			nearestPosition.update(nearestGrid);			
		}
		
		if (nearestPosition.distance >= distance) {
			return null;
		}
		else {
			
//			System.out.println("#area " + vertices.size() + 
//					", #v(area1) " + vertices.iterator().next().size() +
//					", scaled limit = " + distance);
			
		}



		return nearestPosition;
	}

	public static NearestPoint findFromPickedLine(PaintContext context){
		NearestPoint nearestPosition;

		Point2D.Double currentPoint = context.getLogicalMousePoint();
		nearestPosition = findNearestVertexFromLines(
				currentPoint, context.getLines());
		

		return nearestPosition;
	}
}
