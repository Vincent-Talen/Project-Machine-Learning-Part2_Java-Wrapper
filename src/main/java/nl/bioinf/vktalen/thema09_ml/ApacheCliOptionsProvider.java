package nl.bioinf.vktalen.thema09_ml;

import org.apache.commons.cli.*;

public class ApacheCliOptionsProvider {
    private final String[] cli_arguments;
    private final Options cli_options;

    public ApacheCliOptionsProvider(final String[] args) {
        // Save arguments to class object attribute
        this.cli_arguments = args;
        this.cli_options = new Options();
    }

    public void initialize() throws ParseException {
        // Create and add help option
        Option helpOption = new Option("h", "help", false, "print this message");
        cli_options.addOption(helpOption);

        // Perform first parse that indicates if help should be printed
        boolean printHelp = checkForHelp();

        // Build all other options for application
        buildOptions();

        // If help was specified or no options used, print formatted help and stop application
        if (printHelp) {printFormattedHelp(); System.exit(0);}

        // Parse options and save values
        parseOptions();
    }

    private void buildOptions() {
        // Create options
    }

    private void parseOptions() throws ParseException{
        // Create CommandLine object that's parsed given options+arguments
        CommandLine cmd = new DefaultParser().parse(cli_options, cli_arguments);
        }
    }

    private boolean checkForHelp() throws ParseException {
        // Printing help is false by default
        boolean hasHelp = false;

        // New parsed CommandLine object that does not throw an exception when encountering unknown argument
        CommandLine cmd = new DefaultParser().parse(cli_options, cli_arguments, true);

        // If the help option is used or no options are given the help should be printed
        if (cmd.hasOption("help") || cmd.getArgList().isEmpty()) {
            hasHelp = true;
        }
        return hasHelp;
    }

    public void printFormattedHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar Theme09-ML-Application-0.1.jar", cli_options, true);
        System.out.println(); // blank line
    }
}
