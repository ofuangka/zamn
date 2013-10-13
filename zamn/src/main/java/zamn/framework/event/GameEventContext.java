package zamn.framework.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameEventContext extends AbstractEventContext {

	public enum GameEventType implements IEventType {
		BEGIN_ROUND, COMBAT_ACTION_MENU_REQUEST, COORDINATES_REQUEST, CRITTER_ADDED_TO_BOARD, CRITTER_ASSIGNED_CONTROL, CRITTER_DEATH, CRITTER_TARGETED_ACTION_REQUEST, EXIT_GAME_REQUEST, HERO_JOIN_REQUEST, LOCATION_CHANGE, LOSE_CONDITION, MAIN_MENU_REQUEST, MAP_EDITOR_REQUEST, NEW_GAME_REQUEST, NEXT_TURN_REQUEST, PLAY_SOUND_REQUEST, PREVIOUS_IN_GAME_MENU_REQUEST, PUSH_IN_GAME_SUBMENU_REQUEST, RETURN_TO_GAME_REQUEST, SHOW_MESSAGE_REQUEST, SYSTEM_MENU_REQUEST, TRIGGER_ACTIONS_REQUEST, UNIT_PURCHASE_REQUEST, WIN_CONDITION
	}

	public List<IEventType> getAllEventTypes() {
		List<IEventType> ret = new ArrayList<IEventType>();
		ret.addAll(Arrays.asList(GameEventType.values()));
		return ret;
	}

}
