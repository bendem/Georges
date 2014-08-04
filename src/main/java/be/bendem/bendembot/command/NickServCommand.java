package be.bendem.bendembot.command;

import be.bendem.bendembot.utils.NickServ;

import java.util.List;

/**
 * @author bendem
 */
public class NickServCommand extends BaseCommand {

    public NickServCommand() {
        super("nickserv", new String[] {
            "Check if a user if authenticated to NickServ - Usage ## [name]"
        }, "ns");
    }

    @Override
    protected void exec(String primaryArgument, List<String> args) {
        String nick = args.size() == 0 ? user.getName() : args.get(0);
        NickServ.Response response;
        try {
            response = NickServ.check(server, nick);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
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
        message(message.append('.').toString());
    }
}
