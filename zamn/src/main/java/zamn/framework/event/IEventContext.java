package zamn.framework.event;

import java.util.List;
import java.util.Random;

public interface IEventContext {

	public void fire(IEventType type);

	public void fire(IEventType type, Object arg);

	public List<IEventType> getAllEventTypes();

	public Random getRandom();

	public void on(IEventType type, IEventHandler handler);

	public void onAll(IEventHandler handler);
}
