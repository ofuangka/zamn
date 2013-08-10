package zamn.ui.menu;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import zamn.board.controlmode.TargetedAction;
import zamn.board.piece.Critter;
import zamn.creation.TargetedActionFactory;
import zamn.framework.event.GameEventContext;
import zamn.framework.event.IEventContext;

public class CritterMenuFactory {

	public class NoopMenuItem extends AbstractMenuItem {
		private static final long serialVersionUID = -8766319162177106177L;

		@Override
		public void execute() {
			eventContext
					.fire(GameEventContext.GameEventType.PREVIOUS_IN_GAME_MENU_REQUEST);
		}
	}

	public static final String ATTACK_LABEL = "Attack";
	public static final String ITEM_LABEL = "Items";
	public static final String NO_ITEM_LABEL = "No items";
	public static final String NO_TALENT_LABEL = "No talents";
	public static final String TALENT_LABEL = "Talents";
	public static final String WAIT_LABEL = "Wait";

	private IEventContext eventContext;
	private TargetedActionFactory targetedActionFactory;

	public CritterMenuFactory(IEventContext eventContext) {
		this.eventContext = eventContext;
	}

	public Menu getCombatActionMenu(final Critter critter) {

		Menu ret = new Menu();
		List<AbstractMenuItem> menuItems = new ArrayList<AbstractMenuItem>();
		TargetedAction attack = targetedActionFactory.get(critter.getAttack(),
				critter);
		AbstractMenuItem attackMenuItem = new EventMenuItem(eventContext,
				ATTACK_LABEL,
				GameEventContext.GameEventType.CRITTER_TARGETED_ACTION_REQUEST,
				attack);

		AbstractMenuItemWithSubMenu talentSubMenuItem = new AbstractMenuItemWithSubMenu(
				eventContext, TALENT_LABEL) {

			private static final long serialVersionUID = 815195033986998056L;

			@Override
			public Menu getSubMenu() {
				return getTalentMenu(critter);
			}

		};
		/*
		 * AbstractSubMenuItem itemSubMenuItem = new AbstractSubMenuItem(
		 * eventContext, ITEM_LABEL) {
		 * 
		 * private static final long serialVersionUID = 6369030028324018292L;
		 * 
		 * @Override public Menu getSubMenu() { return getItemMenu(critter); }
		 * 
		 * };
		 */
		menuItems.add(attackMenuItem);
		menuItems.add(talentSubMenuItem);
		// menuItems.add(itemSubMenuItem);
		menuItems.add(new AbstractMenuItem(eventContext, WAIT_LABEL) {
			private static final long serialVersionUID = -4070052990440134098L;

			@Override
			public void execute() {
				eventContext.fire(GameEventContext.GameEventType.END_OF_TURN);
			}

		});
		ret.setItems(menuItems);
		return ret;
	}

	/*
	 * public Menu getItemMenu(Critter critter) { Menu ret = new Menu();
	 * List<AbstractMenuItem> menuItems = new ArrayList<AbstractMenuItem>();
	 * 
	 * List<String> items = critter.getItems();
	 * 
	 * if (items == null || items.isEmpty()) { menuItems.add(new
	 * NoopMenuItem()); } else { for (String item : items) { TargetedAction
	 * critterAction = targetedActionLoader.get(item, critter); menuItems
	 * .add(new EventMenuItem( eventContext, critterAction.getName(),
	 * GameEventContext.GameEventType.CRITTER_TARGETED_ACTION_REQUEST,
	 * critterAction)); } }
	 * 
	 * ret.setItems(menuItems); return ret; }
	 */

	public Menu getTalentMenu(Critter critter) {
		Menu ret = new Menu();
		List<AbstractMenuItem> menuItems = new ArrayList<AbstractMenuItem>();

		List<String> talents = critter.getTalents();

		if (talents == null || talents.isEmpty()) {
			menuItems.add(new NoopMenuItem());
		} else {
			for (String talent : talents) {
				TargetedAction critterAction = targetedActionFactory.get(talent,
						critter);
				menuItems
						.add(new EventMenuItem(
								eventContext,
								critterAction.getName(),
								GameEventContext.GameEventType.CRITTER_TARGETED_ACTION_REQUEST,
								critterAction));
			}
		}

		ret.setItems(menuItems);
		return ret;
	}

	@Required
	public void setTargetedActionFactory(
			TargetedActionFactory targetedActionFactory) {
		this.targetedActionFactory = targetedActionFactory;
	}
}
