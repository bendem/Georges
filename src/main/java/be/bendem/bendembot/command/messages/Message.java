package be.bendem.bendembot.command.messages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author bendem
 */
public class Message {

    private final String       name;
    private final Set<Event>   events;
    private final List<String> data;
    private       String       text;

    public Message(String name, String text) {
        this.name = name;
        this.text = text;
        events = new HashSet<>();
        data = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<String> getData() {
        return data;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private enum Event {
        UserJoinChannel,
        UserLeaveChannel,
        UserMessageChannel,
        UserGetBanned,
        UserGetKicked,
        UserGetVoiced,
        UserGetOpped,
        UserGetUnbanned,
        UserGetUnopped,
        UserGetUnvoiced
    }

}
