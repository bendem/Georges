package be.bendem.bendembot.usermanagement;

import be.bendem.bendembot.Georges;
import fr.ribesg.alix.api.event.ClientJoinChannelEvent;
import fr.ribesg.alix.api.event.EventHandler;
import fr.ribesg.alix.api.event.UserJoinChannelEvent;

/**
 * @author bendem
 */
public class UserManagerEvents {

    private final UserManager manager;

    public UserManagerEvents(UserManager manager) {
        this.manager = manager;
    }

    @EventHandler(ignoreConsumed = false)
    public void onJoin(UserJoinChannelEvent e) {
        if(!manager.getUsers().containsKey(e.getUser().getName())) {
            Georges.getLogger().info("Adding " + e.getUser().getName() + "!" + e.getUser().getUserName() + " to known list");
            manager.getUsers().put(e.getUser().getName(), new User(e.getUser()));
        }
    }

    @EventHandler(ignoreConsumed = false)
    public void onClientJoin(ClientJoinChannelEvent e) {
        // TODO Use the other constructor, check if he's not known as someone else (known nicks)
        e.getChannel().getUserNicknames().stream()
            .filter(nick -> !manager.getUsers().containsKey(nick))
            .forEach(nick -> manager.getUsers().put(nick, new User(nick)));
    }

    /*@EventHandler
    public void onUserRename() {
        // TODO Update user known nicks
    }*/

}
