package oripa.paint.geometry.nearest.point;

import java.awt.geom.Point2D;

import javax.vecmath.Vector2d;


public class NearestPointUpdater {

	/**
	 * 
	 * Note that {@code nearest} will be affected.
	 * @param p 		target point
	 * @param nearest	current nearest point to p
	 * @param other		new point to be tested
	 * 
	 * @return nearest point to p
	 */
	static void update(
			Point2D.Double p, NearestPoint nearest, Vector2d other){
		
		
		double dist = p.distance(other.x, other.y);
		if (dist < nearest.distance) {
			nearest.set(other);
			nearest.distance = dist;
		}
		
	}

	public static Vector2d update(
			Point2D.Double p, Vector2d nearest, Vector2d other){
		
		NearestPoint nearestPoint = new NearestPoint();
		nearestPoint.set(nearest);
		nearestPoint.distance = p.distance(nearest.x, nearest.y);
	
		update(p, nearestPoint, other);
	
		return nearest;
	}

}
