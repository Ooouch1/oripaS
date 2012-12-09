package oripa.paint.geometry.nearest.line;

import javax.vecmath.Vector2d;

import oripa.concurrent.MultiInProcessFactory;
import oripa.concurrent.MultiInProcess;
import oripa.geom.OriLine;

public class NearestLineProcessFactory implements MultiInProcessFactory<OriLine, NearestLine> {

	private Vector2d target;
	
	public NearestLineProcessFactory(Vector2d target) {
		this.target = target;
	}
	
	@Override
	public MultiInProcess<OriLine, NearestLine> create() {
		NearestLineProcess process = new NearestLineProcess();
		process.setTarget(target);
		return process;
	}
	
	public void setTarget(Vector2d target){
		this.target = target;
	}

}
