package oripa.doc.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import oripa.geom.OriLine;

public class PasteLines {

	/**
	 * 
	 * @param toBePasted	lines to be added into current lines.
	 * @param currentLines	it will be affected as 
	 * 						new lines are added and unnecessary lines are deleted.
	 */
	public void paste(
			Collection<OriLine> toBePasted, Collection<OriLine> currentLines){

		//----------------------------------------------------------
		// Find lines possible to cross to new lines.
		// found lines are removed from the current list.
		//----------------------------------------------------------
		
		Domain domain = new Domain(toBePasted);
		List<OriLine> crossables = new ArrayList<>(currentLines.size());
				
		for(Iterator<OriLine> itrator = currentLines.iterator();
				itrator.hasNext();){
			OriLine line = itrator.next();

			// skip lines with no intersection
			if(line.p0.x < domain.left && line.p1.x < domain.left ||
					line.p0.x > domain.right && line.p1.x > domain.right  ||
					line.p0.y < domain.top && line.p1.y < domain.top ||
					line.p0.y > domain.bottom && line.p1.y > domain.bottom){
				continue;
			}
			
			crossables.add(line);
			itrator.remove();
		}

		//-----------------------------------------------
		// make them crossed
		//-----------------------------------------------
		
		AddLine adder = new AddLine();
		
		for(OriLine line : toBePasted){
			// the result is stored into crossables.
			adder.addLine(line, crossables);
		}
		
		//-----------------------------------------------
		// set the result to the current list
		//-----------------------------------------------
		for(OriLine line : crossables){
			currentLines.add(line);
		}

		
	}
	
}
