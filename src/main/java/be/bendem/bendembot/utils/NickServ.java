package be.bendem.bendembot.utils;

import fr.ribesg.alix.api.Log;
import fr.ribesg.alix.api.Server;
import fr.ribesg.alix.api.Source;
import fr.ribesg.alix.api.callback.Callback;
import fr.ribesg.alix.api.enums.Command;
import fr.ribesg.alix.api.message.IrcPacket;
import fr.ribesg.alix.api.message.PrivMsgIrcPacket;
import org.apache.commons.lang3.Validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author bendem
 */
public class NickServ {

    private static final Pattern ACC_PARSER = Pattern.compile("^(?<nick>[^\\s]*)\\s+->\\s+(?<account>[^\\s]*)\\s+ACC\\s+(?<code>[0-3])(\\s+\\((?<precision>[^\\)]+)\\))?$");

    public static Response check(Server server, String nick) throws InterruptedException {
        Object lock = new Object();
        NickServCallBack callBack = new NickServCallBack(nick, lock);
        server.send(new PrivMsgIrcPacket("NickServ", "ACC " + nick + " *"), callBack);
        synchronized(lock) {
            lock.wait();
        }
        return callBack.getResponse();
    }

    private static class NickServCallBack extends Callback {

        private final String nick;
        private final Object lock;
        private       Response response;

        private NickServCallBack(String nick, Object lock) {
            super(Command.NOTICE.name());
            this.nick = nick;
            this.lock = lock;
        }

        @Override
        public boolean onIrcPacket(IrcPacket packet) {
            Source source = packet.getPrefixAsSource(server);
            if(source == null || !source.getName().equals("NickServ")) {
                return false;
            }

            Matcher matcher = ACC_PARSER.matcher(packet.getTrail().trim());
            if(!matcher.matches()) {
                Log.error("Error while matching " + packet.getTrail());
                return true;
            }

            // Matches another request
            if(!nick.equalsIgnoreCase(matcher.group("nick"))) {
                return false;
            }

            response = new Response(
                matcher.group("nick"),
                matcher.group("account"),
                Status.fromInt(Integer.parseInt(matcher.group("code"))),
                matcher.group("precision")
            );

            synchronized(lock) {
                lock.notify();
            }
            return true;
        }

        public Response getResponse() {
            return response;
        }

    }

    public enum Status {
        UserOffline,
        UserUnrecognized,
        UserRecognized,
        UserLoggedIn;

        public static Status fromInt(int ordinal) {
            Validate.inclusiveBetween(0, values().length - 1, ordinal);
            return values()[ordinal];
        }
    }

    public static class Response {

        private final String nick;
        private final String account;
        private final Status status;
        private final String precision;

        public Response(String nick, String account, Status status, String precision) {
            this.nick = nick;
            this.account = account;
            this.status = status;
            this.precision = precision;
        }

        public String getNick() {
            return nick;
        }

        public String getAccount() {
            return account;
        }

        public Status getStatus() {
            return status;
        }

        public String getPrecision() {
            return precision;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "nick='" + nick + '\'' +
                    ", account='" + account + '\'' +
                    ", status=" + status +
                    ", precision='" + precision + '\'' +
                    '}';
        }

    }

}
