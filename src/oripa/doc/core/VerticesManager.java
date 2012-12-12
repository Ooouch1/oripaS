package oripa.doc.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.HashMap;

import javax.vecmath.Vector2d;

import oripa.geom.OriLine;


/**
 * For a fast access to vertex.
 * It has some BUG!! (it does not correspond to line information)
 * @author koji
 *
 */
public class VerticesManager {

	/*
	 * divides paper equally to localize accessing to vertices
	 */
	static public final int divNum = 32;

	public final double interval;
	public final double paperCenter;

	
	public class AreaPosition{
		public int x, y;
		
		public AreaPosition(Vector2d v) {
			x = toDiv(v.x);
			y = toDiv(v.y);
		}
		
		public AreaPosition(double x, double y){
			this.x = toDiv(x);
			this.y = toDiv(y);
		}
		

	}

	int toDiv(double p){
		int div = (int) ((p + paperCenter) / interval);
					
		if(div < 0){
			return 0;
		}
		
		if(div >= divNum){
			return divNum-1;
		}
		
		return div;
	}
	
	//[div_x][div_y]
	private HashMap<Vector2d, Integer>[][] vertices = new HashMap[divNum][divNum];

	
	public VerticesManager(double paperSize) {
		interval = paperSize / divNum;
		paperCenter = paperSize/2;
		
		for(int x = 0; x < divNum; x++){
			for(int y = 0; y < divNum; y++){
				vertices[x][y] = new HashMap<>();
			}
		}
		
	}
	
	public void clear(){
		for(int x = 0; x < divNum; x++){
			for(int y = 0; y < divNum; y++){
				vertices[x][y].clear();
			}
		}		
	}
	
	private HashMap<Vector2d, Integer> getVertices(AreaPosition ap){
		return vertices[ap.x][ap.y];
	}
	
	public void add(Vector2d v){
		HashMap<Vector2d, Integer> vertices = getVertices(new AreaPosition(v));

		Integer count = vertices.get(v);
		if(count == null){
			count = new Integer(0);
			vertices.put(v, count);
		}
		count++;
		
	}
	
	public Collection<Vector2d> getAround(Vector2d v){
		AreaPosition ap = new AreaPosition(v);
		return getVertices(ap).keySet();
	}
	
	public void remove(Vector2d v){
		AreaPosition ap = new AreaPosition(v);
		
		Integer count = getVertices(ap).get(v);
		
		if(count == null){
			return;
		}

		count--;
		
		if(count <= 0){
			getVertices(ap).remove(v);
		}
	}
	
	
	private double prev_leftDiv, prev_rightDiv, 
		prev_topDiv, prev_bottomDiv, prev_distance;
	
	private Collection<Collection<Vector2d>> prev_area;
	/**
	 * 
	 * @param x
	 * @param y
	 * @param distance
	 * @return
	 */
	public Collection<Collection<Vector2d>> getArea(
			double x, double y, double distance){

		
		int leftDiv   = toDiv(x - distance);
		int rightDiv  = toDiv(x + distance);
		int topDiv    = toDiv(y - distance);
		int bottomDiv = toDiv(y + distance);

		if(prev_leftDiv == leftDiv && prev_rightDiv == rightDiv && prev_topDiv == topDiv && prev_bottomDiv == bottomDiv
				&& prev_distance == distance){
			return prev_area;
		}

		Collection<Collection<Vector2d>> area = new HashSet<>();		

		prev_leftDiv = leftDiv;
		prev_rightDiv = rightDiv;
		prev_topDiv = topDiv;
		prev_bottomDiv = bottomDiv;
		prev_distance = distance;
		
		
		for(int xDiv = leftDiv; xDiv <= rightDiv; xDiv++){
			for(int yDiv = topDiv; yDiv <= bottomDiv; yDiv++){
				area.add(vertices[xDiv][yDiv].keySet());
			}
		}

		prev_area = area;
		
		return area;
	}
	
	public void load(Collection<OriLine> lines){
		this.clear();
		for(OriLine line : lines){
			add(line.p0);
			add(line.p1);
		}
		
	}
	
	public void addLineVertices(OriLine line){
		this.add(line.p0);
		this.add(line.p1);
	}
	
	public void removeLineVertices(OriLine line){
		this.remove(line.p0);
		this.remove(line.p1);
	}
}
