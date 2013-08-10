package zamn.framework.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameEventContext extends AbstractEventContext {

	public enum GameEventType implements IEventType {
		COMBAT_ACTION_MENU_REQUEST, CRITTER_ADDED_TO_BOARD, CRITTER_DEATH, CRITTER_TARGETED_ACTION_REQUEST, END_OF_TURN, EXIT_GAME_REQUEST, HERO_JOIN_REQUEST, LOSE_CONDITION, NEW_GAME_REQUEST, PREVIOUS_IN_GAME_MENU_REQUEST, PUSH_IN_GAME_SUBMENU_REQUEST, SYSTEM_MENU_REQUEST, WIN_BATTLE_CONDITION, COORDINATES_REQUEST
	}

	public List<IEventType> getAllEventTypes() {
		List<IEventType> ret = new ArrayList<IEventType>();
		ret.addAll(Arrays.asList(GameEventType.values()));
		return ret;
	}

}
