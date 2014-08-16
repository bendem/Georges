package be.bendem.bendembot.automatedmessages;

import be.bendem.bendembot.Context;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author bendem
 */
public class Message {

    private final String                         name;
    private final Set<MessageEventHandler.Event> events;
    private       String                         channel;
    private       String                         text;


    public Message(String name, String text) {
        this(name, text, null);
    }
    public Message(String name, String text, String channel) {
        this.name = name.toLowerCase();
        this.text = text;
        this.channel = channel;
        this.events = new HashSet<>();
    }

    public String transform(Context context, Map<String, String> data) {
        String text = this.text;
        for(Map.Entry<String, String> entry : data.entrySet()) {
            text = StringUtils.replace(text, '{' + entry.getKey() + '}', entry.getValue());
        }
        for(MessageData messageData : MessageData.values()) {
            text = StringUtils.replace(text, '{' + messageData.name().toLowerCase() + '}', messageData.getData(context));
        }
        return text;
    }

    public String getName() {
        return name;
    }

    public boolean shouldBeTriggered(MessageEventHandler.Event event, String channel) {
        return (isGlobal() || this.channel.equalsIgnoreCase(channel)) && events.contains(event);
    }

    public void addEvent(MessageEventHandler.Event event) {
        events.add(event);
    }

    public void removeEvent(MessageEventHandler.Event event) {
        events.remove(event);
    }

    public int clearEvents() {
        int size = events.size();
        events.clear();
        return size;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public boolean isGlobal() {
        return channel == null;
    }

}
