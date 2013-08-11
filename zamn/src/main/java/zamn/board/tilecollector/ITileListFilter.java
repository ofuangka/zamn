package zamn.board.tilecollector;

import java.util.List;

import zamn.board.Tile;

public interface ITileListFilter {
	public boolean apply(Tile tile);
	public List<Tile> filter(List<Tile> unfiltered);
}
