package oripa.paint.byvalue;

import java.awt.geom.Point2D.Double;

import javax.vecmath.Vector2d;

import oripa.ORIPA;
import oripa.geom.GeomUtil;
import oripa.geom.OriLine;
import oripa.paint.AbstractActionState;
import oripa.paint.Globals;
import oripa.paint.MouseContext;
import oripa.paint.PickingLine;
import oripa.paint.PickingVertex;

public class SelectingVertexToDrawLine extends PickingVertex {

	@Override
	protected void initialize() {

	}

	@Override
	protected void onResult(MouseContext context) {
		Vector2d vertex = context.getVertex(0);

		double length;
		double angle;
		try {
			ValueDB valDB = ValueDB.getInstance();
			length = valDB.getLength();
			angle = valDB.getAngle();


			if (length > 0) {
				OriLine vl = GeomUtil.getLineByValue(vertex, length, -angle, Globals.inputLineType);

				ORIPA.doc.pushUndoInfo();
				ORIPA.doc.addLine(vl);
			}
		} 
		catch (Exception ex) {
		}

		context.clear();
	}


}