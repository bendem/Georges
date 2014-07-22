package be.bendem.bendembot.filters;

import fr.ribesg.alix.api.Channel;
import fr.ribesg.alix.api.Source;

/**
 * A ChatFilter should handle user messages and take actions against them
 * if needed (spam, caps...)
 *
 * @author bendem
 */
public interface ChatFilter {

    /**
     * Handle a message from a specific author in a
     * specific channel and take action if needed
     *
     * @param channel The channel the message came from
     * @param author  The message author
     * @param message The message
     * @return True if an action has been taken, false otherwise
     */
    public boolean handleMessage(Channel channel, Source author, String message);

    /**
     * Forget a specific user in a channel (ie because he left)
     *
     * @param user    The user to forget
     * @param channel The channel the user should be forgotten form
     */
    public void forgetUser(Source user, Channel channel);

}
