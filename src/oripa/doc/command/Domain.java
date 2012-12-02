package oripa.doc.command;

import java.util.Collection;

import javax.vecmath.Vector2d;

import oripa.geom.OriLine;

/**
 * A rectangle domain
 */
public class Domain{
	public double left, right, top, bottom;

	/**
	 * reset variables by the most opposite value.
	 */
	public Domain() {
		initialize();
	}

	/**
	 * A rectangle domain fitting to given lines
	 * @param target
	 */
	public Domain(Collection<OriLine> target){

		initialize();

		for(OriLine line : target){
			enlargeDomain(this, line.p0);
			enlargeDomain(this, line.p1);
		}

	}

	private void initialize(){
		left = Double.MAX_VALUE;
		right = Double.MIN_VALUE;
		top = Double.MAX_VALUE;
		bottom = Double.MIN_VALUE;

	}

	public void enlargeDomain(Domain domain, Vector2d v){
		domain.left = Math.min(domain.left, v.x);
		domain.right = Math.max(domain.right, v.x);
		domain.top = Math.min(domain.top, v.y);
		domain.bottom = Math.max(domain.bottom, v.y);

	}

}