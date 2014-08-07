package be.bendem.bendembot.command.bot;

import be.bendem.bendembot.command.BaseCommand;
import be.bendem.bendembot.command.CommandContext;
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
    protected void exec(CommandContext context, String primaryArgument, List<String> args) {
        if(args.size() == 0) {
            context.error("Not enough arguments");
            return;
        }
        context.getServer().send(new NickIrcPacket(args.get(0)));
    }

}
