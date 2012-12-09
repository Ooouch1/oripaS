package oripa.paint.geometry.nearest.point;

import java.awt.geom.Point2D;
import java.util.Collection;

import javax.vecmath.Vector2d;

import oripa.concurrent.MultiInProcess;
import oripa.geom.OriLine;

class FindNearestFromLinesProcess extends MultiInProcess<OriLine, Vector2d>{
	private Vector2d target;
	
	public FindNearestFromLinesProcess(Point2D.Double p) {
		target = new Vector2d(p.x, p.y);
	}
	
	@Override
	public Vector2d run(Collection<OriLine> lines) {
		NearestPoint minPosition = new NearestPoint();

		for (OriLine line : lines) {			

			minPosition.update(target, line.p0);
			minPosition.update(target, line.p1);
			
		}

		return minPosition.get();
	}
}