package be.bendem.bendembot.chathandling;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.Georges;
import be.bendem.bendembot.utils.MultiMap;
import fr.ribesg.alix.api.enums.Codes;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Url spec:
 *     Display Name .... (dn):
 *     eXact Length .... (xl):
 *     eXact Topic ..... (xt): urn:sha1:YNCKHTQCWBTRNJIV4WNAE52SJUQCZO5C
 *     Acceptable Source (as):
 *     eXact Source .... (xs):
 *     Keyword Topic ... (kt):
 *     Manifest Topic .. (mt):
 *     address TRacker . (tr):
 *
 * Example:
 *     magnet:?xt=urn:btih:Y3JFJ5TGS4X2E3UKHJVF6IW7FTPHU2IE&tr=http://open.nyaatorrents.info:6544/announce&tr=udp://open.demonii.com:1337/announce&tr=udp://tracker.openbittorrent.com:80/announce
 */
public class MagnetChat implements ChatHandler {

    private final Pattern MAGNET_PATTERN = Pattern.compile("magnet:\\?(?<params>[^\\s]+)");
    private final Pattern MAGNET_ARGS_PATTERN = Pattern.compile("(?<name>(?:xt|tr|dn|xl|as|xs|kt|mt))=(?<value>[^&]+)(?:&|$)");

    @Override
    public void onChat(Context context, String message, Matcher matcher) {
        Georges.getLogger().debug("Parsing magnet link");

        String params = matcher.group("params");
        context.message("Parsing '" + params + "'");

        Matcher paramMatcher = MAGNET_ARGS_PATTERN.matcher(params);
        MultiMap<String, String> args = new MultiMap<>();

        while(paramMatcher.find()) {
            args.put(paramMatcher.group("name"), paramMatcher.group("value"));
        }

        try {
            context.message(parseArgs(args));
        } catch(InvalidMagnetException e) {
            context.error(e.getMessage());
            Georges.getLogger().error("Invalid magnet parsed");
        }
    }

    private String parseArgs(MultiMap<String, String> args) throws InvalidMagnetException {
        StringBuilder builder = new StringBuilder();

        if(!args.containsKey("xt")) {
            throw new InvalidMagnetException("Invalid magnet, no hash (xt) parameter");
        }

        if(!args.containsKey("tr")) {
            throw new InvalidMagnetException("Invalid magnet, no tracker (tr) parameter");
        }

        builder
            .append("Hash type ")
            .append(Codes.BOLD)
            .append(parseHash(args.get("xt")))
            .append(Codes.RESET)
            .append(" ");

        if(args.containsKey("kt")) {
            String topic = args.get("kt").stream()
                .map(t -> t.replace("+", " "))
                .collect(Collectors.joining(", "));
            builder
                .append("Keywords: ")
                .append(Codes.BOLD)
                .append(topic)
                .append(Codes.RESET)
                .append(' ');
        }

        if(args.containsKey("xl")) {
            builder
                .append("Exact length: ")
                .append(Codes.BOLD)
                .append(args.getFirst("xl"))
                .append(Codes.RESET)
                .append(' ');
        }

        return builder.toString();
    }

    private String parseHash(List<String> xt) throws InvalidMagnetException {
        if(xt.size() != 1) {
            throw new InvalidMagnetException("Expected 1 xt, got <" + xt.size() + ">");
        }
        String hash = xt.get(0);
        String[] split = hash.split(":");
        if(split.length < 3) {
            throw new InvalidMagnetException("Invalid xt hash");
        }
        StringBuilder builder = new StringBuilder();
        for(int i = 1; i < split.length - 1; i++) {
            builder.append(split[i]).append(':');
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    @Override
    public Matcher matcher(String message) {
        return MAGNET_PATTERN.matcher(message);
    }

    class InvalidMagnetException extends Exception {

        public InvalidMagnetException(String message) {
            super(message);
        }

    }

}
