package zamn.mapeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.beans.Transient;
import java.io.IOException;

import javax.swing.JPanel;

import zamn.creation.SpriteFactory;

public class MapEditorPalette extends JPanel implements MouseListener {

	private static final long serialVersionUID = 5684413780256691006L;

	private static final int DISABLED_CURSOR_XY = -1;

	private int cursorX;
	private int cursorY;
	private SpriteFactory spriteFactory;
	private Dimension spriteSize;

	public MapEditorPalette(SpriteFactory spriteFactory, Dimension spriteSize)
			throws IOException {
		this.spriteFactory = spriteFactory;
		this.spriteSize = spriteSize;
		addMouseListener(this);
	}

	public int getCursorX() {
		return cursorX;
	}

	public int getCursorY() {
		return cursorY;
	}

	@Override
	@Transient
	public Dimension getPreferredSize() {
		BufferedImage spriteSheet = spriteFactory.getSpriteSheet();
		return new Dimension(spriteSheet.getWidth(), spriteSheet.getHeight());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX() / spriteSize.width;
		int y = e.getY() / spriteSize.height;
		if (isCursorEnabled() && x == cursorX && y == cursorY) {
			disableCursor();
		} else {
			setCursorXY(x, y);
		}
		repaint();
	}

	private void setCursorXY(int x, int y) {
		cursorX = x;
		cursorY = y;
	}

	private void disableCursor() {
		cursorX = cursorY = DISABLED_CURSOR_XY;
	}

	private boolean isCursorEnabled() {
		return cursorX != DISABLED_CURSOR_XY;
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		BufferedImage spriteSheet = spriteFactory.getSpriteSheet();
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(spriteSheet, 0, 0, spriteSheet.getWidth(),
				spriteSheet.getHeight(), 0, 0, spriteSheet.getWidth(),
				spriteSheet.getHeight(), null);

		// draw the cursor
		if (isCursorEnabled()) {
			g2d.setColor(Color.red);
			g2d.drawRect(cursorX * spriteSize.width, cursorY
					* spriteSize.height, spriteSize.width - 1,
					spriteSize.height - 1);
		}
	}

}
