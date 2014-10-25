package be.bendem.bendembot.automatedmessages;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.utils.StrUtils;

import java.util.Arrays;
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
        StringBuilder builder = new StringBuilder(text);

        data.entrySet().stream()
            .filter(entry -> text.contains('{' + entry.getKey() + '}'))
            .forEach(entry -> StrUtils.replace(builder, '{' + entry.getKey() + '}', entry.getValue()));

        Arrays.stream(MessageData.values())
            .filter(d -> text.contains('{' + d.name().toLowerCase() + '}'))
            .forEach(d -> StrUtils.replace(builder, '{' + d.name().toLowerCase() + '}', d.getData(context)));

        return builder.toString();
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
