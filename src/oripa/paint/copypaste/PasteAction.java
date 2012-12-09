package oripa.paint.copypaste;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.vecmath.Vector2d;

import oripa.ORIPA;
import oripa.doc.command.Domain;
import oripa.geom.OriLine;
import oripa.paint.EditMode;
import oripa.paint.GraphicMouseAction;
import oripa.paint.PaintContext;
import oripa.paint.geometry.GeometricOperation;
import oripa.paint.geometry.nearest.point.NearestPoint;

public class PasteAction extends GraphicMouseAction {


	private Image linesImage;
	
	private FilledOriLineArrayList shiftedLines = new FilledOriLineArrayList(0);

	private OriginHolder originHolder = OriginHolder.getInstance();


	public PasteAction(){
		setEditMode(EditMode.INPUT);
		setNeedSelect(true);

		setActionState(new PastingOnVertex());

	}


	@Override
	public void recover(PaintContext context) {
		context.clear(false);


		context.startPasting();

		for(OriLine line : ORIPA.doc.creasePattern){
			if(line.selected){
				context.pushLine(line);
			}
		}

		shiftedLines = new FilledOriLineArrayList(context.getLines());

		//createImage(context);
	}

	private void updateImage(PaintContext context){
		Domain domain = new Domain(context.getLines());
		
		linesImage = new BufferedImage(
				(int)Math.ceil(domain.right - domain.left), 
				(int)Math.ceil(domain.bottom - domain.top), 
				BufferedImage.TYPE_4BYTE_ABGR);
		
		Graphics2D g2d = (Graphics2D)linesImage.getGraphics();

		
		Vector2d origin = originHolder.getOrigin(context);
		double ox = origin.x;
		double oy = origin.y;
		
		g2d.setColor(Color.MAGENTA);

		GeometricOperation.shiftLines(context.getLines(), shiftedLines,
				- ox, -oy);
		
		for(OriLine line : shiftedLines){
			this.drawLine(g2d, line);
		}
		
	}


	/**
	 * Clear context and mark lines as unselected.
	 */
	@Override
	public void destroy(PaintContext context) {
		context.clear(true);
		context.finishPasting();
	}


	@Override
	public void onDrag(PaintContext context, AffineTransform affine,
			boolean differentAction) {

	}


	@Override
	public void onRelease(PaintContext context, AffineTransform affine, boolean differentAction) {


	}


	@Override
	public Vector2d onMove(PaintContext context, AffineTransform affine,
			boolean differentAction) {

		// vertex-only super's action
		setCandidateVertexOnMove(context, differentAction);
		Vector2d closeVertex = context.pickCandidateV;


		Vector2d closeVertexOfLines = 
				GeometricOperation.pickVertexFromPickedLines(context);

		if(closeVertex == null){
			closeVertex = closeVertexOfLines;
		}


		Vector2d current = new Vector2d(context.getLogicalMousePoint().x, context.getLogicalMousePoint().y);
		if(closeVertex != null && closeVertexOfLines != null){
			// get the nearest to current
			NearestPoint nearest = new NearestPoint();
			nearest.update(current, closeVertex);
			nearest.update(current, closeVertexOfLines);

		}

		context.pickCandidateV = closeVertex;

//		if (context.getLineCount() > 0) {
//			if(closeVertex == null) {
//				closeVertex = new Vector2d(current.x, current.y);
//			}
//
//		}		
		return closeVertex;
	}



	Line2D.Double g2dLine = new Line2D.Double();
	double diffX, diffY;
	@Override
	public void onDraw(Graphics2D g2d, PaintContext context) {

		super.onDraw(g2d, context);

		drawPickCandidateVertex(g2d, context);

		Vector2d origin = originHolder.getOrigin(context);

		if(origin == null){
			return;
		}


		double ox = origin.x;
		double oy = origin.y;

		g2d.setColor(Color.GREEN);
		drawVertex(g2d, context, ox, oy);

		if(context.pickCandidateV != null){
			diffX = context.pickCandidateV.x - ox;
			diffY = context.pickCandidateV.y - oy;
		}
		else {
			diffX = context.getLogicalMousePoint().x - ox;
			diffY = context.getLogicalMousePoint().y -oy;
		}

		
		
		g2d.setColor(Color.MAGENTA);
		// a little faster
		for(OriLine l : context.getLines()){

			g2dLine.x1 = l.p0.x + diffX;
			g2dLine.y1 = l.p0.y + diffY;

			g2dLine.x2 = l.p1.x + diffX;
			g2dLine.y2 = l.p1.y + diffY;

			g2d.draw(g2dLine);
		}

	}

	
	
	@Override
	public void onPress(PaintContext context, AffineTransform affine,
			boolean differentAction) {
	}



}
