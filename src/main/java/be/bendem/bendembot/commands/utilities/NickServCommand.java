package be.bendem.bendembot.commands.utilities;

import be.bendem.bendembot.IrcClient;
import be.bendem.bendembot.commands.BaseCommand;
import be.bendem.bendembot.Context;
import be.bendem.bendembot.utils.NickServ;

import java.util.List;

/**
 * @author bendem
 */
public class NickServCommand extends BaseCommand {

    private final IrcClient bot;

    public NickServCommand(IrcClient bot) {
        super("nickserv", new String[] {
            "Check if a user if authenticated to NickServ - Usage ## [name]"
        }, "ns");
        this.bot = bot;
    }

    @Override
    protected void exec(Context context, String primaryArgument, List<String> args) {
        if("auth".equals(primaryArgument)) {
            bot.auth(context.getServer());
            return;
        }
        String nick = args.size() == 0 ? context.getUser().getName() : args.get(0);
        NickServ.Response response;
        try {
            response = NickServ.check(context.getServer(), nick);
        } catch(InterruptedException e) {
            context.error("Timeout :(");
            return;
        }
        StringBuilder message = new StringBuilder(response.getNick());
        switch(response.getStatus()) {
            case UserOffline:
                message.append(" is offline");
                break;
            case UserUnrecognized:
                message.append(" is not authenticated");
                break;
            case UserRecognized:
                message.append(" was recognized (if you get that, ping me, that should be impossible :P)");
                break;
            case UserLoggedIn:
                message.append(" is authenticated (").append(response.getAccount()).append(')');
                break;
            default:
                throw new AssertionError("a wild enum value appeared");
        }
        context.message(message.append('.').toString());
    }
}
