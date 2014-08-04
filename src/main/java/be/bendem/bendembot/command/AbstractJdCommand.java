package be.bendem.bendembot.command;

import be.bendem.bendembot.JavadocParser;
import fr.ribesg.alix.api.Channel;
import fr.ribesg.alix.api.Server;
import fr.ribesg.alix.api.Source;
import fr.ribesg.alix.api.bot.command.Command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author bendem
 */
public abstract class AbstractJdCommand extends Command {

    public AbstractJdCommand(String name, String[] usage, String... aliases) {
        super(name, usage, aliases);
    }

    public abstract String getUrl();

    @Override
    public boolean exec(Server server, Channel channel, Source source, String s, String[] args) {
        ArrayList<String> argList = new ArrayList<>();
        Collections.addAll(argList, args);
        search(channel, argList, getUrl());
        return true;
    }

    public void search(Channel channel, List<String> args, String url) {
        if(args.size() != 1) {
            channel.sendMessage("Not enough / too much argument(s)...");
            return;
        }

        JavadocParser parser = new JavadocParser(url, 3);
        ArrayList<String> links = null;
        try {
            if(args.get(0).contains(".")) {
                links = parser.searchByPackageName(args.get(0));
            } else {
                links = parser.searchAllClasses(args.get(0));
            }
        } catch(IllegalStateException | NullPointerException e) {
            e.printStackTrace();
        }

        if(links == null || links.size() == 0) {
            channel.sendMessage("Not found :'(");
            return;
        }

        channel.sendMessage("Result" + (links.size() < 2 ? "" : "s") + " found:");
        for(String link : links) {
            channel.sendMessage("| " + link);
        }
    }

}
