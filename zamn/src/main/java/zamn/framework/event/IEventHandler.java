package zamn.framework.event;

/**
 * Implement this interface to be able to subscribe to Events
 * 
 * @author ofuangka
 * 
 */
public interface IEventHandler {

	/**
	 * Implement this method to handle Events (you must first subscribe to the
	 * Event to be aware of the Event firing). Make sure to return true to
	 * continue the Event handling chain
	 * 
	 * @param event
	 *            A reference to the event that fired
	 * @param arg
	 *            The argument that was fired along with the event
	 * @return
	 */
	public boolean handleEvent(Event event, Object arg);
}
