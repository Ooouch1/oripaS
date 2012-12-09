package oripa.paint.geometry.nearest.point;

import javax.vecmath.Vector2d;

public class NearestPoint {
	public Vector2d point = new Vector2d();
	public double distance = Double.MAX_VALUE;

	/**
	 * distance is set to maximum. point is not null but dummy.
	 */
	public NearestPoint() {
	}
	
	public NearestPoint(NearestPoint p) {
		if(p != null){
			point.set(p.point);
			distance = p.distance;
		}
	}
}
