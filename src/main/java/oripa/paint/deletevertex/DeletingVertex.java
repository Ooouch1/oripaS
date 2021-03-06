package oripa.paint.deletevertex;

import oripa.ORIPA;
import oripa.paint.core.PaintContext;
import oripa.paint.core.PickingVertex;

public class DeletingVertex extends PickingVertex {

	@Override
	protected void initialize() {

	}

	@Override
	protected void onResult(PaintContext context) {

		if(context.getVertexCount() > 0){
			ORIPA.doc.pushUndoInfo();
			
			ORIPA.doc.removeVertex(context.popVertex());

		}
		
		context.clear(false);
	}

}
