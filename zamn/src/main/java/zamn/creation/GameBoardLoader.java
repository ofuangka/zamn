package zamn.creation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;

import zamn.board.AbstractBoard;
import zamn.board.GameBoard;
import zamn.board.Tile;
import zamn.board.controlmode.Action;
import zamn.board.piece.Critter;
import zamn.framework.event.Event;
import zamn.framework.event.GameEventContext;
import zamn.framework.event.IEventContext;
import zamn.framework.event.IEventHandler;

public class GameBoardLoader extends BoardLoader implements IEventHandler {

	private CritterFactory critterFactory;

	private IEventContext eventContext;
	private List<Critter> heroes = new ArrayList<Critter>();

	public GameBoardLoader(ObjectMapper objectMapper,
			Resource tileSpriteMapResource, IEventContext eventContext)
			throws IOException {
		super(objectMapper, tileSpriteMapResource);
		this.eventContext = eventContext;
		eventContext.onAll(this);
	}

	@Override
	protected void doAfterLoad(BoardDefinition boardDefinition,
			AbstractBoard board) {

		GameBoard gameBoard = ((GameBoard) board);

		Tile[][] tiles = gameBoard.getTiles();

		CritterPositionDefinition[] critterPositionDefinitions = boardDefinition
				.getCritters();
		for (int i = 0; i < critterPositionDefinitions.length; i++) {
			CritterPositionDefinition critterPositionDefinition = critterPositionDefinitions[i];
			Critter critter = critterFactory.get(critterPositionDefinition
					.getId());
			int seedX = critterPositionDefinition.getSeedX();
			int seedY = critterPositionDefinition.getSeedY();
			if (tiles[seedX] != null && tiles[seedX][seedY] != null
					&& !tiles[seedX][seedY].isOccupied()) {
				tiles[seedX][seedY].add(critter);
				critter.setXY(seedX, seedY);
				eventContext.fire(
						GameEventContext.GameEventType.CRITTER_ADDED_TO_BOARD,
						critter);
			}
		}

		ExitDefinition[] exitDefinitions = boardDefinition.getExits();
		for (int i = 0; i < exitDefinitions.length; i++) {
			ExitDefinition exitDefinition = exitDefinitions[i];
			gameBoard.addExit(
					exitDefinition.getX(),
					exitDefinition.getY(),
					Action.valueOf(exitDefinition.getDir().toUpperCase()),
					new String[] { exitDefinition.getBoardId(),
							String.valueOf(exitDefinition.getEntryPoint()) });
		}

		gameBoard.setEntrances(boardDefinition.getEntrances());
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
		Tile[][] tiles = gameBoard.getTiles();
		Integer[][] entrances = gameBoard.getEntrances();
		Tile entranceTile = tiles[entrances[entryPoint][0]][entrances[entryPoint][1]];

		for (Critter hero : heroes) {
			gameBoard.placePieceInGeneralArea(hero, entranceTile.getX(),
					entranceTile.getY());
			eventContext
					.fire(GameEventContext.GameEventType.CRITTER_ADDED_TO_BOARD,
							hero);
		}
	}

	@Required
	public void setCritterFactory(CritterFactory critterFactory) {
		this.critterFactory = critterFactory;
	}
}
