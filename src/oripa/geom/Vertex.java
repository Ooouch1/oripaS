package oripa.geom;

import javax.vecmath.Tuple2d;
import javax.vecmath.Vector2d;

public class Vertex extends Vector2d{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2570023430440320157L;

	/**
	 * compare by L2 distance without sqrt()
	 */
	@Override
	public boolean epsilonEquals(Tuple2d p, double eps_squared) {
		double dx = this.x - p.x;
		double dy = this.y - p.y;

		return dx * dx + dy * dy < eps_squared;
	}

}
