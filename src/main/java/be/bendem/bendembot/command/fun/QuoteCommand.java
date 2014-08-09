package be.bendem.bendembot.command.fun;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.command.BaseCommand;
import be.bendem.bendembot.utils.EnumUtils;
import be.bendem.bendembot.utils.Language;

import java.util.List;

/**
 * @author bendem
 */
public class QuoteCommand extends BaseCommand {

    public QuoteCommand() {
        super("quote", new String[] {
            "Random quote - Usage ##.<" + EnumUtils.joinValues(Type.class, "|")
        });
    }

    @Override
    protected void exec(Context context, String primaryArgument, List<String> args) {
        Type type = EnumUtils.getIgnoreCase(Type.class, primaryArgument, Type.Programming);
    }

    private enum Type {
        Programming("dev_quotes.txt", Language.English),
        Philosophy("philo_quotes.txt", Language.French);

        private final String filename;
        private final Language language;

        private Type(String filename, Language language) {
            this.filename = filename;
            this.language = language;
        }

        public String getFilename() {
            return filename;
        }

        public Language getLanguage() {
            return language;
        }
    }

}
