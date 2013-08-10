package zamn.ui.menu;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import zamn.framework.event.GameEventContext;
import zamn.framework.event.IEventContext;

public abstract class AbstractMenuItemWithSubMenu extends AbstractMenuItem {

	private static final long serialVersionUID = 3288949207624820781L;

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

		int[] xPoints = new int[] { actualSize.width - 6, actualSize.width - 3,
				actualSize.width - 6 };
		int[] yPoints = new int[] { 10, actualSize.height / 2,
				actualSize.height - 10 };

		g2d.setColor(FONT_COLOR);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.fillPolygon(xPoints, yPoints, 3);
	}

}
