package zamn.board.tilecollector;

import java.util.List;

import zamn.board.Tile;

public interface ITileListFilter {
	public List<Tile> filter(List<Tile> unfiltered);
	public boolean apply(Tile tile);
}
