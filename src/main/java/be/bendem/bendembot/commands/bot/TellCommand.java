package be.bendem.bendembot.commands.bot;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.Georges;
import be.bendem.bendembot.commands.BaseCommand;
import fr.ribesg.alix.api.enums.Codes;
import fr.ribesg.alix.api.message.PrivMsgIrcPacket;

import java.util.List;

/**
 * @author bendem
 */
public class TellCommand extends BaseCommand {

    private final Georges bot;

    public TellCommand(Georges bot) {
        super("tell", new String[] {
            "Tells something to someone"
        }, "t");
        this.bot = bot;
    }

    @Override
    protected void exec(Context context, String primaryArgument, List<String> args) {
        if(args.size() < 2) {
            context.error("Not enough argument");
            return;
        }
        if(!bot.isBotAdmin(context.getUser().getName()) && !args.get(0).startsWith("#")) {
            context.error("You can't send private messages");
            return;
        }
        String target = args.get(0);
        String text = "From " + context.getUser().getName() + ": " + String.join(" ", args.subList(1, args.size()));
        PrivMsgIrcPacket packet = new PrivMsgIrcPacket(target, text);
        context.getServer().send(packet);
        if(context.getChannel() == null) {
            context.message("Message sent to " + Codes.BOLD + target);
        }
    }

}
