package zamn.ui.menu;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import zamn.framework.event.GameEventContext;
import zamn.framework.event.IEventContext;

public abstract class AbstractMenuItemWithSubMenu extends AbstractMenuItem {

	private static final long serialVersionUID = 3288949207624820781L;

	private static final int X_ARROW_OFFSET = 6;
	private static final int X_ARROW_WIDTH = 3;
	private static final int Y_ARROW_HEIGHT_RATIO = 2;
	private static final int Y_ARROW_OFFSET = 10;

	public AbstractMenuItemWithSubMenu() {
		super();
	}

	public AbstractMenuItemWithSubMenu(IEventContext eventContext, String label) {
		super(eventContext, label);
	}

	@Override
	public void execute() {
		getEventContext().fire(
				GameEventContext.GameEventType.PUSH_IN_GAME_SUBMENU_REQUEST,
				getSubMenu());
	}

	public abstract Menu getSubMenu();

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		Dimension actualSize = getSize();

		int[] xPoints = new int[] { actualSize.width - X_ARROW_OFFSET,
				actualSize.width - X_ARROW_WIDTH,
				actualSize.width - X_ARROW_OFFSET };
		int[] yPoints = new int[] { Y_ARROW_OFFSET,
				actualSize.height / Y_ARROW_HEIGHT_RATIO,
				actualSize.height - Y_ARROW_OFFSET };

		g2d.setColor(FONT_COLOR);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.fillPolygon(xPoints, yPoints, xPoints.length);
	}

}
