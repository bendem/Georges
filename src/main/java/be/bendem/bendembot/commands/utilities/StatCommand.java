package be.bendem.bendembot.commands.utilities;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.commands.BaseCommand;
import be.bendem.bendembot.utils.StrUtils;
import fr.ribesg.alix.api.enums.Codes;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Locale;

/**
 * @author bendem
 */
public class StatCommand extends BaseCommand {

    private static final NumberFormat FORMAT = new DecimalFormat("#,##0.##", new DecimalFormatSymbols(Locale.FRANCE));

    public StatCommand() {
        super("stat", new String[] { "Tiny statistic informations - Usage: ## <value 1>[, next values...]" });
    }

    @Override
    protected void exec(Context context, String primaryArgument, List<String> args) {
        if(args.isEmpty()) {
            context.error("No data provided");
        }

        DoubleSummaryStatistics stats;
        try {
            stats = Arrays.stream(StrUtils.commaSplit(args, " "))
                .mapToDouble(Double::parseDouble)
                .summaryStatistics();
        } catch(NumberFormatException e) {
            context.error("One of the values was not a number");
            return;
        }

        context.message(
            "min: " + Codes.BOLD + stats.getMin() + Codes.RESET
            + ", max: " + Codes.BOLD + stats.getMax() + Codes.RESET
            + ", avg: " + Codes.BOLD + stats.getAverage() + Codes.RESET
            + ", sum: " + Codes.BOLD + stats.getSum() + Codes.RESET
        );
    }

}
