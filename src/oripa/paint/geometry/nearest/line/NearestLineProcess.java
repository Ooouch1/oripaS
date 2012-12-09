package oripa.paint.geometry.nearest.line;

import java.util.Collection;

import javax.vecmath.Vector2d;

import oripa.ORIPA;
import oripa.concurrent.MultiInProcess;
import oripa.geom.GeomUtil;
import oripa.geom.OriLine;

public class NearestLineProcess extends MultiInProcess<OriLine, NearestLine> {
	private Vector2d target;
	
	public NearestLineProcess() {
	}
	
	public NearestLineProcess(Vector2d target) {
		setTarget(target);
	}

	public void setTarget(Vector2d target){
		this.target = target;
	}
	
	@Override
	public NearestLine run(Collection<OriLine> values) {
		NearestLine bestLine = new NearestLine();

		for (OriLine line : ORIPA.doc.creasePattern) {
			bestLine.update(target, line);
		}

		return bestLine;
	}

}
