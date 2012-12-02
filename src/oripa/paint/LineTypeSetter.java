package oripa.paint;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LineTypeSetter implements ActionListener {

	protected int lineType;
	
	public LineTypeSetter(int type) {
		lineType = type;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Globals.inputLineType = lineType;

	}

}
