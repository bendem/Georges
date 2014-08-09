package be.bendem.bendembot.automatedmessages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author bendem
 */
public class Message {

    private final String                    name;
    private final Set<MessageManager.Event> events;
    private final List<String>              messageData;
    private       String                    text;

    public Message(String name, String text) {
        this.name = name.toLowerCase();
        this.text = text;
        events = new HashSet<>();
        messageData = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<String> getMessageData() {
        return messageData;
    }

    public Set<MessageManager.Event> getEvents() {
        return events;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
