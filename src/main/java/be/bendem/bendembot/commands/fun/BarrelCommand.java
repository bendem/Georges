package be.bendem.bendembot.commands.fun;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.commands.BaseCommand;

import java.util.List;

public class BarrelCommand extends BaseCommand {

    public BarrelCommand() {
        super("barrel");
    }

    @Override
    protected void exec(Context context, String primaryArgument, List<String> args) {
        context.action("rolls");
    }

}
