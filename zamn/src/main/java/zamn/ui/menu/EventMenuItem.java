package zamn.ui.menu;

import zamn.framework.event.IEventContext;
import zamn.framework.event.IEventType;

public class EventMenuItem extends AbstractMenuItem {

	private static final long serialVersionUID = 9148425736157375354L;

	private Object arg;
	private IEventType type;

	public EventMenuItem() {

	}

	public EventMenuItem(IEventContext eventContext, String label,
			IEventType type, Object arg) {
		super(eventContext, label);
		setType(type);
		setArg(arg);
	}

	@Override
	public void execute() {
		getEventContext().fire(type, this);

	}
	
	public Object getArg() {
		return arg;
	}

	public void setArg(Object arg) {
		this.arg = arg;
	}

	public void setType(IEventType type) {
		this.type = type;
	}

}
