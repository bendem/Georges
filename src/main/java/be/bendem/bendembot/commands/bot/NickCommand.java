package be.bendem.bendembot.commands.bot;

import be.bendem.bendembot.commands.BaseCommand;
import be.bendem.bendembot.Context;
import fr.ribesg.alix.api.message.NickIrcPacket;

import java.util.List;

/**
 * @author bendem
 */
public class NickCommand extends BaseCommand {

    public NickCommand() {
        super("nick", new String[]{
            "Change bot nick"
        }, true);
    }

    @Override
    protected void exec(Context context, String primaryArgument, List<String> args) {
        if(args.size() == 0) {
            context.error("Not enough arguments");
            return;
        }
        context.getServer().send(new NickIrcPacket(args.get(0)));
    }

}
