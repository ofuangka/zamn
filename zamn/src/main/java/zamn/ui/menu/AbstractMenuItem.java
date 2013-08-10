package zamn.ui.menu;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import zamn.framework.event.IEventContext;

public abstract class AbstractMenuItem extends JLabel {

	public static final Color DESELECTED = Color.black;

	public static final Color FONT_COLOR = Color.white;
	public static final int PADDING_HORIZONTAL = 10;
	public static final int PADDING_VERTICAL = 5;
	public static final Color SELECTED = Color.red;
	private static final long serialVersionUID = -7617853828536450022L;

	private IEventContext eventContext;

	public AbstractMenuItem() {
		setOpaque(true);
		setForeground(FONT_COLOR);
		setBackground(DESELECTED);
		setBorder(BorderFactory.createEmptyBorder(PADDING_VERTICAL,
				PADDING_HORIZONTAL, PADDING_VERTICAL, PADDING_HORIZONTAL));
	}
	
	public AbstractMenuItem(IEventContext eventContext, String label) {
		this();
		setEventContext(eventContext);
		setText(label);
	}

	public abstract void execute();

	public IEventContext getEventContext() {
		return eventContext;
	}

	public void setEventContext(IEventContext eventContext) {
		this.eventContext = eventContext;
	}

	public void uiDeselect() {
		setBackground(DESELECTED);
	}

	public void uiSelect() {
		setBackground(SELECTED);
	}

}
