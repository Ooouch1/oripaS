package oripa.paint.geometry.nearest;

import javax.vecmath.Vector2d;

import oripa.geom.GeomUtil;
import oripa.paint.geometry.nearest.line.NearestLine;


public abstract class NearestObject<Obj> {
	protected Obj object;
	public double distance = Double.MAX_VALUE;
	
	
	/**
	 * distance is set to maximum. object is not null but dummy.
	 */
	public NearestObject() {
	}
	
	public NearestObject(NearestObject<Obj> nearest) {
		if(nearest != null){
			set(nearest.object);
			distance = nearest.distance;
		}
	}

	public void set(Obj o){
		object = o;
	}
	
	public Obj get(){
		return object;
	}
	
	public abstract double calculateDistance(Vector2d target, Obj o);

	public void update(NearestObject<Obj> other){
		if(other.distance < distance){
			set(other.object);
			distance = other.distance;
		}
	}
	
	public void update(Vector2d target, Obj other){
		double dist = calculateDistance(target, other);
		if (dist < this.distance) {
			set(other);
			this.distance = dist;
		}

	}

}
