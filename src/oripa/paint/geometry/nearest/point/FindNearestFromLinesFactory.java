package oripa.paint.geometry.nearest.point;

import java.awt.geom.Point2D;

import javax.vecmath.Vector2d;

import oripa.concurrent.MultiInProcess;
import oripa.concurrent.MultiInProcessFactory;
import oripa.geom.OriLine;

class FindNearestFromLinesFactory implements MultiInProcessFactory<OriLine, Vector2d>{
	private Point2D.Double target;
	
	
	
	public FindNearestFromLinesFactory(Point2D.Double target) {
		setTarget(target);
	}
	
	@Override
	public MultiInProcess<OriLine, Vector2d> create() {
		return new FindNearestFromLinesProcess(target);
	}
	public void setTarget(Point2D.Double target) {
		this.target = target;
	}
}