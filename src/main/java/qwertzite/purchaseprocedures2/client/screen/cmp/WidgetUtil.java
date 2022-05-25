package qwertzite.purchaseprocedures2.client.screen.cmp;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class WidgetUtil {
	
	/**
	 * Text style must be MULTI
	 * @param text
	 * @return
	 */
	public static Text makeTextAutoScroll(Text text) {
		Listener scrollBarListener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				Text t = (Text) event.widget;
				Rectangle r1 = t.getClientArea();
				Rectangle r2 = t.computeTrim(r1.x, r1.y, r1.width, r1.height);
				Point p = t.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
				if ((t.getStyle() & SWT.H_SCROLL) != 0) t.getHorizontalBar().setVisible(r2.width <= p.x);
				if ((t.getStyle() & SWT.V_SCROLL) != 0) t.getVerticalBar().setVisible(r2.height <= p.y);
				if (event.type == SWT.Modify) {
					t.getParent().layout(true);
					t.showSelection();
				}
			}
		};
		text.addListener(SWT.Resize, scrollBarListener);
		text.addListener(SWT.Modify, scrollBarListener);
		
		return text;
	}
	
}
