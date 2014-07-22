package be.bendem.bendembot.custompackets;

import fr.ribesg.alix.api.message.PrivMsgIrcPacket;

/**
 * @author bendem
 */
public class ActionIrcPacket extends PrivMsgIrcPacket {

    public ActionIrcPacket(String receiver, String message) {
        super(receiver, "\001ACTION " + message +  "\001");
    }

}
