package oripa.paint.geometry.nearest.point;

import java.awt.geom.Point2D;
import java.util.Collection;

import javax.vecmath.Vector2d;

import oripa.concurrent.MultiInProcess;
import oripa.geom.OriLine;

class FindNearestFromLinesProcess extends MultiInProcess<OriLine, Vector2d>{
	private Point2D.Double target;
	
	public FindNearestFromLinesProcess(Point2D.Double p) {
		target = p;
	}
	
	@Override
	public Vector2d run(Collection<OriLine> lines) {
		NearestPoint minPosition = new NearestPoint();

		for (OriLine line : lines) {			

			NearestPointUpdater.update(target, minPosition, line.p0);
			NearestPointUpdater.update(target, minPosition, line.p1);
			
		}

		return minPosition.point;
	}
}