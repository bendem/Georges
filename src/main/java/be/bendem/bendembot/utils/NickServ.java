package be.bendem.bendembot.utils;

import be.bendem.bendembot.Georges;
import fr.ribesg.alix.api.Server;
import fr.ribesg.alix.api.Source;
import fr.ribesg.alix.api.callback.Callback;
import fr.ribesg.alix.api.enums.Command;
import fr.ribesg.alix.api.event.ReceivedPacketEvent;
import fr.ribesg.alix.api.message.IrcPacket;
import fr.ribesg.alix.api.message.PrivMsgIrcPacket;
import org.apache.commons.lang3.Validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author bendem
 */
public class NickServ {

    private static final Pattern ACC_PARSER = Pattern.compile("^(?<nick>[^\\s]*)\\s+->\\s+(?<account>[^\\s]*)\\s+ACC\\s+(?<code>[0-3])(?:\\s+(?:\\((?<precision>[^)]+)\\)|(?<eid>[A-Z0-9]+)))$");

    public static Response check(Server server, String nick) throws InterruptedException {
        final Object lock = new Object();
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
            super(10_000, Command.NOTICE.name());
            this.nick = nick;
            this.lock = lock;
        }

        @Override
        public boolean onReceivedPacket(ReceivedPacketEvent e) {
            IrcPacket packet = e.getPacket();
            Source source = packet.getPrefixAsSource(e.getSource());
            if(source == null || !source.getName().equals("NickServ")) {
                return false;
            }

            Matcher matcher = ACC_PARSER.matcher(packet.getTrail().trim());
            if(!matcher.matches()) {
                Georges.getLogger().error("Error while matching " + packet.getTrail());
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
                matcher.group("eid"),
                matcher.group("precision")
            );
            e.consume();

            synchronized(lock) {
                lock.notify();
            }
            return true;
        }

        public Response getResponse() {
            return response;
        }

        @Override
        public void onTimeout() {
            synchronized(lock) {
                lock.notify();
            }
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
        private final String eid;

        public Response(String nick, String account, Status status, String eid, String precision) {
            Validate.notNull(nick, "nick can't be null");
            Validate.notNull(account, "account can't be null");
            Validate.notNull(status, "status can't be null");
            Validate.isTrue((eid == null) != (precision == null), "either eid or precision should be null, not none, not the two of them");

            Georges.getLogger().debug("NickServ answer");
            Georges.getLogger().debug(eid);
            Georges.getLogger().debug(precision);

            this.nick = nick;
            this.account = account;
            if(status == Status.UserOffline) {
                this.status = "offline".equalsIgnoreCase(precision) ? status : Status.UserUnrecognized;
            } else {
                this.status = status;
            }
            this.eid = eid;
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

        public String getEid() {
            return eid;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "nick='" + nick + '\'' +
                    ", account='" + account + '\'' +
                    ", status=" + status +
                    ", eid='" + eid + '\'' +
                    '}';
        }

    }

}
