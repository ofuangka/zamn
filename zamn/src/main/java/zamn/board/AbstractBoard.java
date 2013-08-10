package zamn.board;

import java.awt.Dimension;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import zamn.common.Direction;
import zamn.framework.event.IEventContext;
import zamn.ui.IDelegatingKeySink;
import zamn.ui.ILayer;

/**
 * This class should be used to access the 2 dimension Tile array and can place
 * pieces
 * 
 * @author ofuangka
 * 
 */
public abstract class AbstractBoard extends JComponent implements ILayer,
		IDelegatingKeySink {

	public static final int INVALID_X = -1;

	public static final int INVALID_Y = -1;
	private static final Logger LOG = Logger.getLogger(AbstractBoard.class);

	private static final long serialVersionUID = -2949485746939158973L;

	private IEventContext eventContext;

	private Dimension spriteSize;

	protected Tile[][] tiles;

	public AbstractBoard(IEventContext eventContext) {
		this.eventContext = eventContext;
	}

	@Override
	public void backspace() {
		getCurrentKeySink().backspace();
	}

	/**
	 * A hook to perform any additional checks before executing a move
	 * 
	 * @param piece
	 * @param nextTile
	 * @return
	 */
	protected boolean doBeforeMoveExecution(AbstractBoardPiece piece,
			Tile nextTile, Direction dir) {
		return true;
	}

	@Override
	public void down() {
		getCurrentKeySink().down();
	}

	@Override
	public void enter() {
		getCurrentKeySink().enter();
	}

	@Override
	public void esc() {
		getCurrentKeySink().esc();
	}

	public Tile[] getAdjacentTiles(int x, int y) {
		Tile[] ret = new Tile[Tile.NUM_TILE_EDGES];
		if (isInBounds(x, y)) {
			ret = getTile(x, y).getAdjacentTiles();
		}
		return ret;
	}

	public Tile[] getAdjacentTiles(PositionedSpriteSheetSprite piece) {
		return getAdjacentTiles(piece.getX(), piece.getY());
	}

	public Tile[] getAdjacentTiles(Tile tile) {
		return tile.getAdjacentTiles();
	}

	public IEventContext getEventContext() {
		return eventContext;
	}

	public Dimension getSpriteSize() {
		return spriteSize;
	}

	public Tile getTile(int x, int y) {
		return tiles[x][y];
	}

	public Tile[][] getTiles() {
		return tiles;
	}
	
	/**
	 * The Board layer is never empty
	 * 
	 * @return
	 */
	@Override
	public boolean isEmpty() {
		return false;
	}

	/**
	 * Checks if the given coordinates are within the bounds of the board
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isInBounds(int x, int y) {
		return !(x < 0) && x < tiles.length
				&& isInBounds(x, y, 0, 0, tiles.length, tiles[x].length);
	}

	/**
	 * Convenience method checks if the given coordinates are within the bounds
	 * also given
	 * 
	 * @param x
	 * @param y
	 * @param minX
	 * @param minY
	 * @param maxX
	 * @param maxY
	 * @return
	 */
	public boolean isInBounds(int x, int y, int minX, int minY, int dx, int dy) {
		return !(x < minX) && !(y < minY) && (x < minX + dx) && (y < minY + dy);
	}

	@Override
	public void left() {
		getCurrentKeySink().left();
	}

	public void placePiece(AbstractBoardPiece piece, int x, int y) {
		removePiece(piece);
		if (tiles[x][y] == null) {
			throwNullTileException(x, y);
		} else {
			tiles[x][y].add(piece);
			piece.setXY(x, y);
			repaint();
		}

	}

	public void removePiece(AbstractBoardPiece piece) {
		int x = piece.getX();
		int y = piece.getY();

		if (isInBounds(x, y)) {
			if (tiles[x][y] == null) {
				throwNullTileException(x, y);
			} else {
				tiles[x][y].remove(piece);
				piece.setXY(INVALID_X, INVALID_Y);
			}

			repaint();
		}
		// else do nothing (removing a piece that's not on the board)
	}

	@Override
	public void right() {
		getCurrentKeySink().right();
	}

	@Required
	public void setSpriteSize(Dimension spriteSize) {
		this.spriteSize = spriteSize;
	}

	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}

	@Override
	public void space() {
		getCurrentKeySink().space();
	}

	/**
	 * Convenience method that throws an NullPointerException with the
	 * coordinates of the null Tile
	 * 
	 * @param x
	 * @param y
	 */
	private void throwNullTileException(int x, int y) {
		throw new IllegalArgumentException("Tile was null at coordinates (" + x
				+ ", " + y + ")!");
	}

	/**
	 * Moves an arbitrary piece one space in the given direction unless the Tile
	 * in the direction is invalid, disabled or occupied. Returns true if the
	 * move was successful
	 * 
	 * @param piece
	 * @param dir
	 */
	public boolean tryMove(AbstractBoardPiece piece, Direction dir) {
		LOG.debug("Attempting to move piece " + piece + " " + dir);
		int nextX = piece.getX();
		int nextY = piece.getY();
		switch (dir) {
		case UP: {
			nextY--;
			break;
		}
		case RIGHT: {
			nextX++;
			break;
		}
		case DOWN: {
			nextY++;
			break;
		}
		case LEFT: {
			nextX--;
			break;
		}
		default: {
			break;
		}
		}
		if (isInBounds(nextX, nextY)) {
			Tile nextTile = getTile(nextX, nextY);
			if (nextTile.isWalkable() && nextTile.isEnabled()
					&& !nextTile.isOccupied()
					&& doBeforeMoveExecution(piece, nextTile, dir)) {
				placePiece(piece, nextX, nextY);
				return true;

			}
		}
		return false;
	}

	@Override
	public void up() {
		getCurrentKeySink().up();
	}

	@Override
	public void x() {
		getCurrentKeySink().x();
	}
}
