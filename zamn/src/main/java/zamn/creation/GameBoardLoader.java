package zamn.creation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;

import zamn.board.AbstractBoard;
import zamn.board.GameBoard;
import zamn.board.Tile;
import zamn.board.piece.Critter;
import zamn.common.Direction;
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
	protected void doLoad(BoardDefinition boardDefinition, AbstractBoard board,
			int entryPoint) {

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
					Direction.valueOf(exitDefinition.getDir().toUpperCase()),
					new String[] { exitDefinition.getBoardId(),
							String.valueOf(exitDefinition.getEntryPoint()) });
		}

		Integer[][] entrances = boardDefinition.getEntrances();
		Tile entranceTile = tiles[entrances[entryPoint][0]][entrances[entryPoint][1]];
		gameBoard.placeHeroes(entranceTile);
	}

	protected void doOnCritterDeath(Critter deadCritter) {
		heroes.remove(deadCritter);
	}

	protected void doOnHeroJoinRequest(Critter hero) {
		heroes.add(hero);
	}

	protected void doOnNewGameRequest() {
		heroes.clear();
	}

	@Override
	public boolean handleEvent(Event event, Object arg) {
		switch ((GameEventContext.GameEventType) event.getType()) {
		case NEW_GAME_REQUEST: {
			doOnNewGameRequest();
		}
		case HERO_JOIN_REQUEST: {
			doOnHeroJoinRequest((Critter) arg);
			break;
		}
		case CRITTER_DEATH: {
			doOnCritterDeath((Critter) arg);
			break;
		}
		default: {
			break;
		}
		}
		return true;
	}

	@Required
	public void setCritterFactory(CritterFactory critterFactory) {
		this.critterFactory = critterFactory;
	}
}
