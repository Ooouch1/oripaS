package oripa.doc.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import javax.vecmath.Vector2d;

import oripa.geom.OriLine;


/**
 * For a fast access to vertex
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
	private HashSet<Vector2d>[][] vertices = new HashSet[divNum][divNum];

	
	public VerticesManager(double paperSize) {
		interval = paperSize / divNum;
		paperCenter = paperSize/2;
		
		for(int x = 0; x < divNum; x++){
			for(int y = 0; y < divNum; y++){
				vertices[x][y] = new HashSet<Vector2d>();
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
	
	private HashSet<Vector2d> getVertices(AreaPosition ap){
		return vertices[ap.x][ap.y];
	}
	
	public void add(Vector2d v){
		HashSet<Vector2d> vertices = getVertices(new AreaPosition(v));

		vertices.add(v);
				
	}
	
	public Collection<Vector2d> getAround(Vector2d v){
		AreaPosition ap = new AreaPosition(v);
		return getVertices(ap);
	}
	
	public void remove(Vector2d v){
		AreaPosition ap = new AreaPosition(v);
		getVertices(ap).remove(v);
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

		Collection<Collection<Vector2d>> area = new LinkedList<>();		

		prev_leftDiv = leftDiv;
		prev_rightDiv = rightDiv;
		prev_topDiv = topDiv;
		prev_bottomDiv = bottomDiv;
		prev_distance = distance;
		
		
		for(int xDiv = leftDiv; xDiv <= rightDiv; xDiv++){
			for(int yDiv = topDiv; yDiv <= bottomDiv; yDiv++){
				area.add(vertices[xDiv][yDiv]);
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
	
	
}
