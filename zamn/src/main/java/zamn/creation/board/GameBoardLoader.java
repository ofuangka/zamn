package zamn.creation.board;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import zamn.board.AbstractBoard;
import zamn.board.Critter;
import zamn.board.GameBoard;
import zamn.framework.event.Event;
import zamn.framework.event.GameEventContext;
import zamn.framework.event.IEventContext;
import zamn.framework.event.IEventHandler;

/**
 * This BoardLoader also loads Critters, Entrances and Exits
 * 
 * @author ofuangka
 * 
 */
public class GameBoardLoader extends BoardLoader implements IEventHandler {

	private List<Critter> heroes = new ArrayList<Critter>();

	public GameBoardLoader(IEventContext eventContext) {
		eventContext.onAll(this);
	}

	protected void handleCritterDeath(Critter deadCritter) {
		heroes.remove(deadCritter);
	}

	@Override
	public boolean handleEvent(Event event, Object arg) {
		switch ((GameEventContext.GameEventType) event.getType()) {
		case NEW_GAME_REQUEST: {
			handleNewGameRequest();
			break;
		}
		case HERO_JOIN_REQUEST: {
			handleHeroJoinRequest((Critter) arg);
			break;
		}
		case CRITTER_DEATH: {
			handleCritterDeath((Critter) arg);
			break;
		}
		default: {
			break;
		}
		}
		return true;
	}

	protected void handleHeroJoinRequest(Critter hero) {
		heroes.add(hero);
	}

	protected void handleNewGameRequest() {
		heroes.clear();
	}

	public void load(URI boardId, AbstractBoard board, int entryPoint)
			throws JsonParseException, JsonMappingException,
			MalformedURLException, IOException {
		super.load(boardId, board);
		GameBoard gameBoard = (GameBoard) board;
		int[][] entrances = gameBoard.getEntrances();

		for (Critter hero : heroes) {
			gameBoard.addBoardPiece(hero, entrances[entryPoint][0],
					entrances[entryPoint][1]);
		}
	}
}
