package oripa.paint.geometry.nearest.line;

import javax.vecmath.Vector2d;

import oripa.geom.GeomUtil;
import oripa.geom.OriLine;
import oripa.paint.geometry.nearest.NearestObject;

public class NearestLine extends NearestObject<OriLine>{

	/**
	 * distance is set to maximum. line is not null but dummy.
	 */
	public NearestLine() {
	}
	
	public NearestLine(NearestLine nearest) {
		super(nearest);
	}

	@Override
	public double calculateDistance(Vector2d target, OriLine line) {
		return GeomUtil.DistancePointToSegment(target, line.p0, line.p1);
	}
}
