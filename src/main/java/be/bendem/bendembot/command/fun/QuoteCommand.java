package be.bendem.bendembot.command.fun;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.command.BaseCommand;
import be.bendem.bendembot.utils.EnumUtils;
import be.bendem.bendembot.utils.Language;
import be.bendem.bendembot.utils.ResourceUtils;
import fr.ribesg.alix.api.Log;
import fr.ribesg.alix.api.enums.Codes;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author bendem
 *
 * Quotes were found at
 * + http://www.programmerexcuses.com/
 * + http://api.icndb.com/jokes/random/100
 * TODO find more quotes
 */
public class QuoteCommand extends BaseCommand {

    private final Map<Type, List<String>> quotes;
    private final Random random;

    public QuoteCommand() {
        super("quote", new String[] {
            "Random quote - Usage ##.<" + EnumUtils.joinValues(Type.class, "|") + '>'
        });

        quotes = new EnumMap<>(Type.class);
        random = new Random();
        loadThemFilez();
        //debugThemQuotez();
    }

    private void debugThemQuotez() {
        quotes.forEach((type, list) -> Log.debug(type.name() + ": " + StringUtils.join(list, "\n\t")));
    }

    private void loadThemFilez() {
        for(Type type : Type.values()) {
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(ResourceUtils.getStream(type.getFilename()), Charset.forName("utf-8")))) {
                List<String> collected = reader.lines().collect(Collectors.toList());
                quotes.put(type, collected);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void exec(Context context, String primaryArgument, List<String> args) {
        Type type = EnumUtils.getIgnoreCase(Type.class, primaryArgument, Type.RandomEn);
        if(type == null) {
            context.error("Quote type not found, type `h.quotes to see them");
            return;
        }
        context.message(Codes.ITALIC + random(type));
    }

    private String random(Type type) {
        List<String> strings = quotes.get(type);
        return strings.get(random.nextInt(strings.size()));
    }

    private enum Type {
        ProgrammingExcuses("devexcuses_en_quotes.txt", Language.English),
        RandomFr("philo_fr_quotes.txt", Language.French),
        RandomEn("philo_en_quotes.txt", Language.English),
        Chuck("chuck_en_quotes.txt", Language.English);

        private final String   filename;
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
