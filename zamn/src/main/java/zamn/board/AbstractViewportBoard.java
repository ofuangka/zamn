package zamn.board;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import org.apache.log4j.Logger;

public abstract class AbstractViewportBoard extends AbstractBoard {

	public static final int DEFAULT_VIEWPORT_PADDING_X = 2;
	public static final int DEFAULT_VIEWPORT_PADDING_Y = 2;

	private static final Logger LOG = Logger
			.getLogger(AbstractViewportBoard.class);
	private static final long serialVersionUID = 4713851524702696250L;

	private int viewportPaddingX = DEFAULT_VIEWPORT_PADDING_X;
	private int viewportPaddingY = DEFAULT_VIEWPORT_PADDING_Y;

	private int viewportX;
	private int viewportY;

	/**
	 * Given a point on the board, this method figures out where the viewport
	 * should be moved to minimize distance while staying within the bounds of
	 * the board.
	 * 
	 * @param x
	 * @param y
	 */
	public void alignViewport(int x, int y) {

		if (!isInViewport(x, y)) {

			int viewportWidth = getViewportWidth();
			int viewportHeight = getViewportHeight();

			int newViewportX = viewportX;
			int newViewportY = viewportY;

			// depending on where the point is in relation to the viewport, move
			// the viewport
			if (x < viewportX + viewportPaddingX) {
				newViewportX = x - viewportPaddingX;
			} else if (x > viewportX + viewportWidth - viewportPaddingX - 1) {
				newViewportX = x + viewportPaddingX - viewportWidth + 1;
			}
			if (y < viewportY + viewportPaddingY) {
				newViewportY = y - viewportPaddingY;
			} else if (y > viewportY + viewportHeight - viewportPaddingY - 1) {
				newViewportY = y + viewportPaddingY - viewportHeight + 1;
			}

			// the new offset should not be greater than the last possible
			// offset of
			// the viewport
			if (newViewportX < 0) {
				newViewportX = 0;
			}
			if (newViewportX > (tiles.length - viewportWidth)) {
				newViewportX = tiles.length - viewportWidth;
			}
			if (newViewportY < 0) {
				newViewportY = 0;
			} else if (newViewportY > (tiles[x].length - viewportHeight)) {
				newViewportY = tiles[x].length - viewportHeight;
			}

			setViewportXY(newViewportX, newViewportY);
		}
	}

	/**
	 * Returns the viewport height in sprites
	 * 
	 * @return
	 */
	protected int getViewportHeight() {
		return getPreferredSize().height / getSpriteSize().height;
	}

	/**
	 * Returns the viewport width in sprites
	 * 
	 * @return
	 */
	protected int getViewportWidth() {
		return getPreferredSize().width / getSpriteSize().width;
	}

	/**
	 * This method returns true if the xy coordinates lie within the viewport
	 * and viewport padding
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isInViewport(int x, int y) {

		int viewportWidth = getViewportWidth();
		int viewportHeight = getViewportHeight();

		warnIfViewportDimensionsAreZero(viewportWidth, viewportHeight);

		return AbstractBoard.isInBounds(x, y, viewportX + viewportPaddingX,
				viewportY + viewportPaddingY, viewportWidth - 2
						* viewportPaddingX, viewportHeight - 2
						* viewportPaddingY);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int viewportWidth = getViewportWidth();
		int viewportHeight = getViewportHeight();

		warnIfViewportDimensionsAreZero(viewportWidth, viewportHeight);

		Graphics2D g2d = (Graphics2D) g;
		for (int x = 0; x < viewportWidth; x++) {
			for (int y = 0; y < viewportHeight; y++) {
				Tile tile = tiles[viewportX + x][viewportY + y];
				Dimension tileSize = tile.getSpriteSize();
				int dx1 = x * tileSize.width;
				int dx2 = dx1 + tileSize.width;
				int dy1 = y * tileSize.height;
				int dy2 = dy1 + tileSize.height;
				g2d.drawImage(tile.getImage(), dx1, dy1, dx2, dy2, 0, 0,
						tileSize.width, tileSize.height, null);
			}
		}
	}

	public void setViewportPaddingX(int viewportPaddingX) {
		this.viewportPaddingX = viewportPaddingX;
	}

	public void setViewportPaddingY(int viewportPaddingY) {
		this.viewportPaddingY = viewportPaddingY;
	}

	public void setViewportXY(int viewportX, int viewportY) {
		this.viewportX = viewportX;
		this.viewportY = viewportY;
	}

	protected void warnIfViewportDimensionsAreZero(int viewportWidth,
			int viewportHeight) {

		if (viewportWidth == 0 || viewportHeight == 0) {
			LOG.warn("Viewport has dimensions (" + viewportWidth + ", "
					+ viewportHeight + "). Has its preferred size been set?");
		}
	}

}
