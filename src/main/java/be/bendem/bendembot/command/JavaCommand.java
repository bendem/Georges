package be.bendem.bendembot.command;

import be.bendem.bendembot.BendemBot;
import be.bendem.bendembot.IrcClient;

/**
 * @author bendem
 */
public class JavaCommand extends AbstractJdCommand {

    public JavaCommand(IrcClient bot) {
        super("java", new String[]{"Search the java jds"}, "j");
    }

    @Override
    public String getUrl() {
        return BendemBot.JAVA_URL;
    }
}
