package oripa.paint.geometry.nearest.point;

import java.awt.geom.Point2D;

import javax.vecmath.Vector2d;

import oripa.paint.geometry.nearest.NearestObject;

public class NearestPoint extends NearestObject<Vector2d>{

/*	public NearestPoint(Point2D.Double p) {
		object.set(p.x, p.y);		
	}

	*/
	
	public NearestPoint() {
		object = new Vector2d();
	}
	
	@Override
	public double calculateDistance(Vector2d target, Vector2d o) {
		return Point2D.Double.distance(target.x, target.y, o.x, o.y);
	}

	@Override
	public void set(Vector2d o) {
		// TODO Auto-generated method stub
		object.set(o.x, o.y);
	}
}
