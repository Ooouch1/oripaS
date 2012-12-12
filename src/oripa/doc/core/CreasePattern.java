package oripa.doc.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.vecmath.Vector2d;

import oripa.geom.OriLine;
import oripa.util.LazyArrayList;
import oripa.util.LazyList;

public class CreasePattern implements LazyList<OriLine> {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -6919017534440930379L;

	private LazyArrayList<OriLine> lines;
	public boolean addAll(int index, Collection<? extends OriLine> c) {
		return lines.addAll(index, c);
	}

	public CreasePattern(double paperSize) {
		//lines = new ArrayList<>();
		lines = new LazyArrayList<>();
		
		vertices = new VerticesManager(paperSize);
	}
	

	public boolean equals(Object o) {
		return lines.equals(o);
	}


	public int hashCode() {
		return lines.hashCode();
	}


	public OriLine get(int index) {
		return lines.get(index);
	}


	public OriLine set(int index, OriLine element) {
		vertices.removeLineVertices(this.get(index));
		return lines.set(index, element);
	}


	public int indexOf(Object o) {
		return lines.indexOf(o);
	}


	public int lastIndexOf(Object o) {
		return lines.lastIndexOf(o);
	}


	public ListIterator<OriLine> listIterator() {
		return lines.listIterator();
	}


	public ListIterator<OriLine> listIterator(int index) {
		return lines.listIterator(index);
	}


	public List<OriLine> subList(int fromIndex, int toIndex) {
		return lines.subList(fromIndex, toIndex);
	}

	private VerticesManager vertices;
	
	
	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return lines.contains(o);
	}

	@Override
	public int size() {
		return lines.size();
	}

	@Override
	public boolean add(OriLine e) {
		vertices.addLineVertices(e);
		return lines.add(e);
	}

	public void add(int index, OriLine element) {
		vertices.addLineVertices(element);
		lines.add(index, element);
	}

	@Override
	public boolean remove(Object o) {
		OriLine line = (OriLine) o;
		vertices.removeLineVertices(line);

		return lines.remove(o);
	}
	
	public OriLine remove(int index) {
		OriLine line = lines.get(index);
		vertices.removeLineVertices(line);
		
		return lines.remove(index);
	}


	@Override
	public void clear() {
		lines.clear();
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return lines.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return lines.toArray(a);
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return lines.isEmpty();
	}

	@Override
	public Iterator<OriLine> iterator() {
		// TODO Auto-generated method stub
		return lines.iterator();
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return lines.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends OriLine> c) {
		
		for(OriLine line : c){
			vertices.addLineVertices(line);
		}
		
		return lines.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		
		
		for(OriLine line : (Collection<OriLine>)c){
			vertices.removeLineVertices(line);
		}
		return lines.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		
		for(OriLine line : lines){
			Collection<OriLine> collection = (Collection<OriLine>)c;
			//removes from this collection 
			//all of its elements that are not contained in the specified collection c.
			if(! collection.contains(line)){
				vertices.removeLineVertices(line);
				
			}
		}
		
		
		return lines.retainAll(c);
	}
	
	public Collection<Vector2d> getVerticesAround(Vector2d v){
		return vertices.getAround(v);
	}

	public Collection<Collection<Vector2d>> getVerticesArea(
			double x, double y, double distance){
		
		return vertices.getArea(x, y, distance);
	}

	public VerticesManager getVerticesManager(){
		return vertices;
	}

	@Override
	public int lazyRemove(int index) {
		return lines.lazyRemove(index);	
	}


	
}
