package nl.bioinf.vktalen.thema09_ml;

import org.apache.commons.cli.*;

import java.util.Arrays;

public class ApacheCliOptionsProvider {
    private final String[] cli_arguments;
    private final Options cli_options;

    public ApacheCliOptionsProvider(final String[] args) {
        // Save arguments to object attribute and create Options object to class property
        this.cli_arguments = args;
        this.cli_options = new Options();

        // Create and add help option
        Option helpOption = new Option("h", "help", false, "print this message");
        this.cli_options.addOption(helpOption);

        try {
            // Perform first parse that indicates if help should be printed
            boolean printHelp = this.checkForHelp();

            // Build all options for application
            this.buildOptions();

            // If help was specified or no options used, print formatted help and stop application
            if (printHelp) {printFormattedHelp(); return;}

            // Create CommandLine object that's parsed given options+arguments
            CommandLine cmd = new DefaultParser().parse(this.cli_options, this.cli_arguments);

            // Handle stuffs
            String ageStr = cmd.getOptionValue("alpha", "42");
            int alpha = Integer.parseInt(ageStr);
        } catch (ParseException ex) {
            System.err.println("ERROR: Parsing failed! Reason: " + ex.getMessage() + "\n");
            this.printFormattedHelp();
        }
    }

    private void buildOptions() {
        Option alphaOption = new Option("a", "alpha", true, "test alpha");

        this.cli_options.addOption(alphaOption);
    }

    private boolean checkForHelp() throws ParseException {
        // False by default
        boolean hasHelp = false;

        // New parsed CommandLine object with all options + arguments in it
        CommandLine cmd = new DefaultParser().parse(this.cli_options, this.cli_arguments, true);

        // If the no options or only the help option is called
        // set hasHelp to true indicating help needs to be printed
        if (cmd.hasOption("help") || cmd.getArgList().isEmpty()) {
            hasHelp = true;
        }
        return hasHelp;
    }

    private void printFormattedHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Theme09-ML-Application-0.1.jar", this.cli_options, true);
    }
}
