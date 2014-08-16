package be.bendem.bendembot.commands.bot;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.commands.BaseCommand;
import fr.ribesg.alix.api.enums.Codes;
import fr.ribesg.alix.api.message.PrivMsgIrcPacket;

import java.util.List;

/**
 * @author bendem
 */
public class TellCommand extends BaseCommand {

    public TellCommand() {
        super("tell", new String[] {
            "Tells something to someone"
        }, true, "t");
    }

    @Override
    protected void exec(Context context, String primaryArgument, List<String> args) {
        if(args.size() < 2) {
            context.error("Not enough argument");
            return;
        }
        String target = args.get(0);
        String text = String.join(" ", args.subList(1, args.size()));
        PrivMsgIrcPacket packet = new PrivMsgIrcPacket(target, text);
        context.getServer().send(packet);
        context.message("Message sent to " + Codes.BOLD + target);
    }

}
