package be.bendem.bendembot.commands.utilities;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.Georges;
import be.bendem.bendembot.commands.BaseCommand;
import org.apache.commons.lang3.StringUtils;

import java.security.Permission;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author bendem
 */
public class NashornCommand extends BaseCommand {

    private static final String[] TYPE_MAPPINGS = new String[] {
        "String", "Integer", "Character", "Double", "Float", "Number"
    };
    private static final Pattern SANITIZE;
    static {
        String[] blacklist = {
            "file", "stream",
            "reader", "writer",
            "input", "output",
            "system",
            "java\\.n?io",
            "be\\.", "fr\\."
        };
        Georges.getLogger().debug('(' + StringUtils.join(blacklist, "|") + ')');
        SANITIZE = Pattern.compile(".*(?:" + StringUtils.join(blacklist, "|") + ").*", Pattern.CASE_INSENSITIVE);
    }

    public NashornCommand() {
        super("nashorn", new String[] {
            "Uses the nashorn command thingy - Usage: ## [script]",
            "To display something, use Out.print(value)"
        }, "calc");
    }

    // `calc var list = new java.util.ArrayList(); list.add('a'); list.add('b'); list.add('a'); for each (var el in list) Out.print(el);

    @Override
    @SuppressWarnings("deprecation")
    protected void exec(Context context, String primaryArgument, List<String> args) {
        String script = String.join(" ", args);
        // TODO Use a SecurityManager to deny all permissions of a separate thread executing engine.eval(script) instead
        if(SANITIZE.matcher(script).matches()) {
            context.error("Content not sane to execute :(");
            return;
        }

        new SecurityManager() {
            @Override
            public void checkPermission(Permission permission, Object o) {
                throw new SecurityException();
            }
        };

        // One engine eval at a time, this way, Nashorn.output is safe from a concurrency POV
        synchronized(this) {
            ScriptEngine engine = prepareNewEngine();
            Runnable task = () -> {
                try {
                    Object eval = engine.eval(script);
                    if(eval != null) {
                        context.message(eval.toString());
                    }
                    if(OutputHandler.output.size() != 0) {
                        context.message("STDOUT: " + String.join(" | ", OutputHandler.output));
                    }
                } catch(ScriptException e) {
                    Georges.getLogger().error("Error while executing nashorn script", e);
                    context.error(e.getMessage().split("\r?\n")[0]);
                } finally {
                    OutputHandler.output.clear();
                }
            };
            Thread thread = new Thread(task);
            thread.start();
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch(InterruptedException e) {
                Georges.getLogger().error("Interrupted while waiting script end", e);
            }
            // force them to quit by interrupting
            if(thread.isAlive()) {
                thread.stop();
                context.error("Execution too long");
            }
        }
    }

    private static ScriptEngine prepareNewEngine() {
        // New engine
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

        // Build default mappings for primitives
        StringBuilder sb = new StringBuilder();
        for(String name : TYPE_MAPPINGS) {
            sb.append("var ").append(name).append(" = Java.type('java.lang.").append(name).append(";");
        }
        sb.append("var Out = Java.type('be.bendem.bendembot.commands.utilities.NashornCommand.OutputHandler');");

        // Eval default code
        try {
            engine.eval(sb.toString());
        } catch(ScriptException e) {
            Georges.getLogger().error("Error while evaluating nashorn prescript", e);
        }
        return engine;
    }

    public static class OutputHandler {
        private static List<String> output = new LinkedList<>();
        public static void print(Object o) {
            output.add(o.toString());
        }
    }

}
