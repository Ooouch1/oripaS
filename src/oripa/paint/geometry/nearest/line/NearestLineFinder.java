package oripa.paint.geometry.nearest.line;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.vecmath.Vector2d;

import oripa.concurrent.MultiInMultiOutProcessor;
import oripa.geom.OriLine;

public class NearestLineFinder {

	
	public NearestLine find(Vector2d target, List<OriLine> lines){
		NearestLineProcessFactory factory = new NearestLineProcessFactory(target);
		MultiInMultiOutProcessor<OriLine, NearestLine> processor = new MultiInMultiOutProcessor<>(factory);
		
		Collection<NearestLine> candidates = null;

		NearestLine bestLine = new NearestLine();

		try {
			candidates = processor.execute(lines, 4);
			for(NearestLine candidate : candidates){
				bestLine.update(candidate);
			}
		} catch (IllegalAccessException | InterruptedException
				| ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bestLine;
	}
}
