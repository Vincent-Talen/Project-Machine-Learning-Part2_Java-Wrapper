package nl.bioinf.vktalen.thema09_ml;

import org.apache.commons.cli.*;

import java.util.List;

/**
 * Class that creates an Apache Commons Command Line Interface.
 * Command line arguments are parsed and options' values saved as class attributes accessible with getter methods.
 *
 * @author Vincent Talen (389015)
 */
public class ApacheCliOptionsProvider {
    private final String[] cli_arguments;
    private final Options cli_options;

    private String inputFile;
    private String outputFile;
    private boolean showDistribution;

    /**
     * Constructor method.
     * Command line arguments are saved in class attribute and
     * a new Options object is also created and saved to class attribute.
     *
     * @param args Array with provided command line arguments
     */
    public ApacheCliOptionsProvider(final String[] args) {
        // Save arguments to class object attribute
        this.cli_arguments = args;
        this.cli_options = new Options();
    }

    /**
     * Initializer that activates and runs entire process for ApacheCliOptionsProvider class objects.
     * The following actions are performed (by calling the respective methods):
     * checks if help is required and if so prints it whilst stopping application, adds all options,
     * parses the supplied command line arguments and saves the values as class attributes.
     *
     * @throws ParseException When an argument is supplied in the command line that is not
     * supported, or when wrongly formatted/typed values are supplied, this error is thrown.
     */
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

    /**
     * Creates and adds options to the Options object class attribute
     */
    private void buildOptions() {
        // Create options
        Option inputFileOption = Option.builder("f").longOpt("input-file").argName("file-name")
                .desc(".arff type file with unclassified data to be classified")
                .hasArg().required().build();
        Option outputFileOption = Option.builder("o").longOpt("output-file").argName("file-name")
                .desc("name of the output file the classified dataset should be saved to")
                .hasArg().build();
        Option showDistributionOption = Option.builder("d").longOpt("show-distribution")
                .desc("Show prediction distributions for the classes and not only the prediction")
                .build();

        // Add options to Options object
        cli_options.addOption(inputFileOption);
        cli_options.addOption(outputFileOption);
        cli_options.addOption(showDistributionOption);
    }

    /**
     * Parses the command line arguments saved to the class attribute when constructing.
     * Values of options are saved to the respective class attributes.
     *
     * @throws ParseException When an argument is supplied in the command line that is not
     * supported, or when wrongly formatted/typed values are supplied, this error is thrown.
     */
    private void parseOptions() throws ParseException{
        // Create CommandLine object that's parsed given options+arguments
        CommandLine cmd = new DefaultParser().parse(cli_options, cli_arguments);

        // Save option values to class attributes
        this.inputFile = cmd.getOptionValue("input-file");
        this.outputFile = cmd.getOptionValue("output-file", null);
        this.showDistribution = cmd.hasOption("show-distribution");
    }

    /**
     * Checks if the help option is called or if there were no arguments supplied at all.
     *
     * @return true when either help option is called or when there were no arguments supplied at all
     * @throws ParseException When an argument is supplied in the command line that is not
     * supported, or when wrongly formatted/typed values are supplied, this error is thrown.
     */
    private boolean checkForHelp() throws ParseException {
        // Printing help is false by default
        boolean hasHelp = false;

        // New parsed CommandLine object that does not throw an exception when encountering unknown argument
        CommandLine cmd = new DefaultParser().parse(cli_options, cli_arguments, true);

        // Get unparsed arguments to check if it's empty or if help has been called behind another argument
        List<String> unparsedArgs = cmd.getArgList();
        boolean helpFoundInUnparsed = unparsedArgs.contains("-h") || unparsedArgs.contains("--help");

        // If the help option is used or no options are given the help should be printed
        if (cmd.hasOption("help") || helpFoundInUnparsed || unparsedArgs.isEmpty()) {
            hasHelp = true;
        }
        return hasHelp;
    }

    /**
     * Method that prints the formatted help for the command line interface
     * of the application. Shows all options and default usage.
     */
    public void printFormattedHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar Theme09-ML-Application-0.1.5.jar", cli_options, true);
        System.out.println(); // blank line
    }

    /**
     * @return inputFile The name that was provided in command line
     */
    public String getInputFile() {
        return inputFile;
    }
    /**
     * @return outputFile The name that was provided in command line
     */
    public String getOutputFile() {
        return outputFile;
    }
    /**
     * @return showDistribution If the prediction distributions for the classes should be shown
     */
    public boolean getShowDistribution() {
        return showDistribution;
    }
}
