package oripa.paint.geometry.nearest.line;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.vecmath.Vector2d;

import oripa.concurrent.MultiInMultiOutProcessor;
import oripa.geom.OriLine;

public class NearestLineFinder {

	private static NearestLineProcessFactory factory = new NearestLineProcessFactory(null);
	private static MultiInMultiOutProcessor<OriLine, NearestLine> processor = new MultiInMultiOutProcessor<>(factory);
	
	public static NearestLine find(Vector2d target, List<OriLine> lines){
		
		factory.setTarget(target);
		
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
