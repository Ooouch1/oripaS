package oripa.paint.byvalue;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

import oripa.paint.GraphicMouseAction;
import oripa.paint.MouseContext;

public class LengthMeasuringAction extends GraphicMouseAction {

	public LengthMeasuringAction(){
		setActionState(new SelectingVertexForLength());
	}
	
	
	
	@Override
	public GraphicMouseAction onLeftClick(MouseContext context,
			AffineTransform affine, MouseEvent event) {

		GraphicMouseAction action;
		action = super.onLeftClick(context, affine, event);
		
		if(context.isMissionCompleted()){
			action = new LineByValueAction();
		}
		
		return action;
	}



	@Override
	public void onDrag(MouseContext context, AffineTransform affine,
			MouseEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRelease(MouseContext context, AffineTransform affine,
			MouseEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDraw(Graphics2D g2d, MouseContext context) {

		super.onDraw(g2d, context);
		
		drawPickCandidateVertex(g2d, context);


	}
}