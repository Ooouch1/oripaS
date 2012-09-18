package oripa.paint.copypaste;

import javax.vecmath.Vector2d;

import oripa.paint.PaintContext;

public class OriginHolder {

	private static OriginHolder holder = null;
	
	private Vector2d origin = null;
	
	private OriginHolder(){}
	
	public static OriginHolder getInstance(){
		if(holder == null){
			holder = new OriginHolder();
		}
		
		return holder;
	}
	
	public void setOrigin(Vector2d p){
		origin = p;
	}
	
	public Vector2d getOrigin(PaintContext context){
    	if(origin == null){
    		if(context.getLineCount() > 0){
    			origin = context.getLine(0).p0;
    		}
		}

		return origin;
	}
	
}