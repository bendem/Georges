//package be.bendem.ircjavadoc;
//
//import org.apache.commons.lang3.StringUtils;
//import fr.ribesg.alix.api.Channel;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.logging.Logger;
//
///**
// * @author bendem
// */
//public class UserCommand implements Runnable {
//
//    private final Channel      channel;
//    private final String       author;
//    private final String       commandName;
//    private final List<String> args;
//
//    public UserCommand(Channel channel, String author, String message) {
//        this.channel = channel;
//        this.author = author;
//        message = message.substring(1);
//
//        args = new ArrayList<>(Arrays.asList(message.split(" ")));
//        commandName = args.get(0);
//        args.remove(0);
//
//        Logger.getLogger("IrcJavaDoc").info(channel.getName() + " - " + author
//                + ": " + commandName + "->'" + StringUtils.join(args, ":") + "'");
//    }
//
//    @Override
//    public void run() {
//        // Public commands come here
//        if(commandName.equalsIgnoreCase("b") || commandName.equalsIgnoreCase("bukkit")) {
//            search(IrcJavaDoc.BUKKIT_URL);
//        }
//        if(commandName.equalsIgnoreCase("j") || commandName.equalsIgnoreCase("java")) {
//            search(IrcJavaDoc.JAVA_URL);
//        }
//        if(commandName.equalsIgnoreCase("a") || commandName.equalsIgnoreCase("apache")) {
//            search(IrcJavaDoc.APACHE_URL);
//        }
//
//        if(!author.equalsIgnoreCase("bendem")) {
//            // TODO Check if user is identified by NickServ so that user can't
//            // send commands in the 30 seconds attributed by NickServ to auth
//            return;
//        }
//        if(commandName.equalsIgnoreCase("plugins")) {
//            plugins();
//        }
//    }
//
//    private void plugins() {
//        channel.sendMessage("See bendem's plugins:");
//        channel.sendMessage("ChatMuffler: http://dev.bukkit.org/bukkit-plugins/chatmuffler/");
//        channel.sendMessage("OreBroadcast: http://dev.bukkit.org/bukkit-plugins/ore-broadcast/");
//        channel.sendMessage("OnDeathCoord: http://dev.bukkit.org/bukkit-plugins/on-death-coord/");
//        channel.sendMessage("More on github (look for [Bukkit]: https://github.com/bendem?tab=repositories");
//    }
//
//}
