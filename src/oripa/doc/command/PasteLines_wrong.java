//package oripa.doc.command;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.concurrent.ExecutionException;
//
//
//import oripa.concurrent.ArrayParallelProcessor;
//import oripa.concurrent.PartialCollectionProcess;
//import oripa.concurrent.PartialCollectionProcessFactory;
//import oripa.geom.OriLine;
//
//public class PasteLines_wrong {
//
//	private class PasteProcess extends PartialCollectionProcess<OriLine, OriLine>{
//		private Collection<OriLine> toBePasted;
//
//		public PasteProcess(Collection<OriLine> toBePasted) {
//			super();
//			this.toBePasted = toBePasted;
//		}
//
//		public PasteProcess(Collection<OriLine> lines, Collection<OriLine> toBePasted){
//			super(lines);
//			this.toBePasted = toBePasted;
//
//		}
//		
//		public void setLinesToBepasted(Collection<OriLine> lines) {
//			toBePasted = lines;
//		}
//		
//		/**
//		 * @param lines crossable lines.
//		 */
//		@Override
//		public Collection<OriLine> run(Collection<OriLine> lines) {
//			
//			//-----------------------------------------------
//			// make them crossed
//			//-----------------------------------------------
//
//			AddLine adder = new AddLine();
//
//			for(OriLine line : toBePasted){
//				// the result is stored into crossables.
//				adder.addLine(line, lines);
//			}
//			
//			return lines;
//		}
//	}
//
//	private class PasteProcessFactory implements PartialCollectionProcessFactory<OriLine, OriLine>{
//		private Collection<OriLine> toBePasted;
//		
//		public PasteProcessFactory(Collection<OriLine> toBePasted) {
//			this.toBePasted = toBePasted;
//		}
//		@Override
//		public PartialCollectionProcess<OriLine, OriLine> create() {
//			return new PasteProcess(toBePasted);
//		}
//		@Override
//
//		public PartialCollectionProcess<OriLine, OriLine> create(
//				Collection<OriLine> values) {
//			return new PasteProcess(values, toBePasted);
//		}
//	}
//	
//	/**
//	 * 
//	 * @param toBePasted	lines to be added into current lines.
//	 * @param currentLines	it will be affected as 
//	 * 						new lines are added and unnecessary lines are deleted.
//	 */
//	public void paste(
//			Collection<OriLine> toBePasted, Collection<OriLine> currentLines){
//
//		//----------------------------------------------------------
//		// Find lines possible to cross to new lines.
//		// Found lines are removed from the current list.
//		//----------------------------------------------------------
//
//		Domain domain = new Domain(toBePasted);
//		ArrayList<OriLine> crossables = new ArrayList<>();
//
//		for(Iterator<OriLine> itrator = currentLines.iterator();
//				itrator.hasNext();){
//			OriLine line = itrator.next();
//
//			// skip lines with no intersection
//			if(line.p0.x < domain.left && line.p1.x < domain.left ||
//					line.p0.x > domain.right && line.p1.x > domain.right  ||
//					line.p0.y < domain.top && line.p1.y < domain.top ||
//					line.p0.y > domain.bottom && line.p1.y > domain.bottom){
//				continue;
//			}
//
//			crossables.add(line);
//			itrator.remove();
//		}
//
//		//-----------------------------------------------
//		// make them crossed in parallel process
//		//-----------------------------------------------
//
//		ArrayParallelProcessor<OriLine, OriLine> processor = new ArrayParallelProcessor<>(new PasteProcessFactory(toBePasted));
//		ArrayList<OriLine> crossedlines;
//		try {
//			crossedlines = processor.execute(crossables, 4);
//			
//		} catch (InstantiationException | IllegalAccessException
//				| InterruptedException | ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return;
//		}
//		
//
//		//-----------------------------------------------
//		// set the result to the current list
//		//-----------------------------------------------
//		currentLines.addAll(crossedlines);
//
//		
//	}
//
//}
