package oripa.paint.geometry;

import java.awt.geom.Point2D;
import java.util.Collection;

import javax.vecmath.Vector2d;

import oripa.ORIPA;
import oripa.geom.OriLine;
import oripa.paint.Globals;
import oripa.paint.PaintContext;

public class NearestVertexFinder {
	/**
	 * 
	 * Note that {@code nearest} will be affected.
	 * @param p 		target point
	 * @param nearest	current nearest point to p
	 * @param other		new point to be tested
	 * 
	 * @return nearest point to p
	 */
	private static NearestPoint findNearestOf(
			Point2D.Double p, NearestPoint nearest, Vector2d other){
		
		
		double dist = p.distance(other.x, other.y);
		if (dist < nearest.distance) {
			nearest.point = other;
			nearest.distance = dist;
		}
		
		return nearest;
	}

	public static Vector2d findNearestOf(
			Point2D.Double p, Vector2d nearest, Vector2d other){
		
		NearestPoint nearestPoint = new NearestPoint();
		nearestPoint.point = nearest;
		nearestPoint.distance = p.distance(nearest.x, nearest.y);

		NearestVertexFinder.findNearestOf(
				p, nearestPoint, other);
	
		return nearest;
	}

	private static NearestPoint findNearestOrigamiVertex(Point2D.Double p){

		return findNearestVertexFromLines(p, ORIPA.doc.lines);

	}

	private static NearestPoint findNearestVertexFromLines(
			Point2D.Double p, Collection<OriLine> lines){

		NearestPoint minPosition = new NearestPoint();

		for (OriLine line : lines) {			

			minPosition = findNearestOf(p, minPosition, line.p0);
			minPosition = findNearestOf(p, minPosition, line.p1);
			
		}

		return minPosition;

	}
		
	private static NearestPoint findNearestVertex(Point2D.Double p, Collection<Vector2d> vertices){

		NearestPoint minPosition = new NearestPoint();

		for(Vector2d vertex : vertices){
			minPosition = findNearestOf(p, minPosition, vertex);
		}

		return minPosition;
	}

	public static NearestPoint find(PaintContext context){
		NearestPoint nearestPosition;

		Point2D.Double currentPoint = context.getLogicalMousePoint();
		nearestPosition = findNearestOrigamiVertex(currentPoint);
		
		if (context.dispGrid) {

			NearestPoint nearestGrid = findNearestVertex(
					currentPoint, context.updateGrids(Globals.gridDivNum));
			
			if(nearestGrid.distance < nearestPosition.distance){
				nearestPosition = nearestGrid;
			}
			
		}

		return nearestPosition;
	}

	public static NearestPoint findFromPickedLine(PaintContext context){
		NearestPoint nearestPosition;

		Point2D.Double currentPoint = context.getLogicalMousePoint();
		nearestPosition = findNearestVertexFromLines(
				currentPoint, context.getLines());
		
//		if (context.dispGrid) {
//
//			NearestPoint nearestGrid = findNearestVertex(
//					currentPoint, context.updateGrids(Globals.gridDivNum));
//			
//			if(nearestGrid.distance < nearestPosition.distance){
//				nearestPosition = nearestGrid;
//			}
//			
//		}

		return nearestPosition;
	}
}