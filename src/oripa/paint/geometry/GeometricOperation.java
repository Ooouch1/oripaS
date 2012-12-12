package oripa.paint.geometry;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;

import javax.vecmath.Vector2d;

import oripa.ORIPA;
import oripa.doc.command.CalculationResource;
import oripa.geom.GeomUtil;
import oripa.geom.OriLine;
import oripa.paint.PaintContext;
import oripa.paint.geometry.nearest.line.NearestLine;
import oripa.paint.geometry.nearest.line.NearestLineFinder;
import oripa.paint.geometry.nearest.point.NearestPoint;
import oripa.paint.geometry.nearest.point.NearestVertexFinder;


/**
 * Logics using ORIPA data and mouse point in geometric form.
 * @author koji
 *
 */
public class GeometricOperation {

	private static double getScaledThresholdDistance(PaintContext context){
		return CalculationResource.CLOSE_THRESHOLD / context.scale;
	}

	private static double getScaledThresholdDistance(double scale){
		return CalculationResource.CLOSE_THRESHOLD / scale;
	}

	
	// returns the OriLine sufficiently closer to point p
	public static OriLine pickLine(Point2D.Double p, double scale) {
		Vector2d point = new Vector2d(p.x, p.y);
		
		NearestLine nearest = NearestLineFinder.find(point, ORIPA.doc.creasePattern);

		if (nearest.distance < getScaledThresholdDistance(scale)) {
			return nearest.get();
		} else {
			return null;
		}
	}



	public static Vector2d pickVertex(
			PaintContext context, boolean freeSelection){

		
		NearestPoint nearestPosition;

		nearestPosition = NearestVertexFinder.findAround(context, getScaledThresholdDistance(context));
		

		Vector2d picked = null;		

		if(nearestPosition != null){
			picked = new Vector2d(nearestPosition.get());		
		}
		
		if(picked == null && freeSelection == true){
			Point2D.Double currentPoint = context.getLogicalMousePoint();

			OriLine l = pickLine(currentPoint, context.scale);
			if(l != null) {
				picked = new Vector2d();
				Vector2d cp = new Vector2d(currentPoint.x, currentPoint.y);

				GeomUtil.DistancePointToSegment(cp, l.p0, l.p1, picked);
			}
		}

		return picked;
	}

	public static Vector2d pickVertexFromPickedLines(PaintContext context){

		
		NearestPoint nearestPosition;
		nearestPosition = NearestVertexFinder.findFromPickedLine(context);
		

		Vector2d picked = null; 
		if (nearestPosition.distance < getScaledThresholdDistance(context)) {
			picked = nearestPosition.get();
		}
		
		return picked;
	}

	public static OriLine pickLine(PaintContext context) {
		return pickLine(context.getLogicalMousePoint(), context.scale);
	}

	public static Vector2d getCandidateVertex(PaintContext context, boolean enableMousePoint){

		Vector2d candidate = context.pickCandidateV;

		if(candidate == null && enableMousePoint){
			Point2D.Double mp = context.getLogicalMousePoint();
			candidate = new Vector2d(mp.x, mp.y);
		}

		return candidate;
	}

	public static void shiftLines(Collection<OriLine> lines, 
			ArrayList<OriLine> shiftedLines, double diffX, double diffY){
		
		int i = 0;
		for (OriLine l : lines) {
			OriLine shifted = shiftedLines.get(i);

			shifted.p0.x = l.p0.x + diffX;
			shifted.p0.y = l.p0.y + diffY;

			shifted.p1.x = l.p1.x + diffX;
			shifted.p1.y = l.p1.y + diffY;

			shifted.typeVal = l.typeVal;
			
			i++;
		}

	}

}
