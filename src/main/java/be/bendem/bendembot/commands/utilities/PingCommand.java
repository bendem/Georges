package be.bendem.bendembot.commands.utilities;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.commands.BaseCommand;
import be.bendem.bendembot.utils.EnumUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author bendem
 */
public class PingCommand extends BaseCommand {

    private static final Pattern EXTRACT_DOMAIN_FROM_URL = Pattern.compile("^https?://(?<target>[^/]+).*$", Pattern.CASE_INSENSITIVE);

    public PingCommand() {
        super("ping", new String[] {
            "Pings a user or a website - Usage: ## [user|site]"
        }, "pi");
    }

    @Override
    protected void exec(Context context, String primaryArgument, List<String> args) {
        if(args.size() == 0) {
            context.message("Pong.", false);
            return;
        }
        Target target = EnumUtils.getIgnoreCase(Target.class, primaryArgument, Target.User);
        switch(target) {
            case User:
                pingUser(args.get(0), context);
                break;
            case Site:
                pingSite(args.get(0), context);
                break;
        }
    }

    private void pingUser(String nick, Context context) {
        context.error("Not implemented yet");
    }

    private void pingSite(String url, Context context) {
        Matcher matcher = EXTRACT_DOMAIN_FROM_URL.matcher(url);
        String target;
        if(matcher.matches()) {
            target = matcher.group("target");
        } else {
            target = url;
        }

        Process process;
        try {
            process = Runtime.getRuntime().exec("ping -c 2 -W 1 -q " + target);
        } catch(IOException e) {
            context.error(e.getMessage());
            return;
        }
        try {
            process.waitFor();
        } catch(InterruptedException e) {
            context.error(e.getMessage());
            return;
        }
        if(process.exitValue() != 0) {
            context.error("Command exited with error code " + process.exitValue());
            return;
        }
        try(BufferedReader bf = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            for(int i = 0; i < 4; i++) {
                bf.readLine(); // ignore first lines, we only need the summary
            }
            String output = bf.readLine();
            if(output == null) {
                context.error("no output :(");
            } else {
                context.message(output);
            }
        } catch(IOException e) {
            context.error(e.getMessage());
        }
    }

    private enum Target {
        User, Site
    }

}
