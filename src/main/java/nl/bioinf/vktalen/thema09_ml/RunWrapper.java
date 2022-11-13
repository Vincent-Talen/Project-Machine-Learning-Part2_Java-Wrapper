package nl.bioinf.vktalen.thema09_ml;

import org.apache.commons.cli.ParseException;

/**
 * Main class for the Java wrapper around the machine learning model.
 * Designed to work with user input provided through the command line
 * that is parsed using an Apache Command Line Interface.
 * The class is final since it is not made to be extended.
 *
 * @author Vincent Talen (389015)
 */
public final class RunWrapper {
    /**
     * Constructor is private because this class is not supposed to be instantiated.
     */
    private RunWrapper() {}

    /**
     * @param args Array with provided command line arguments
     */
    public static void main(String[] args) {
        ApacheCliOptionsProvider optProvider = new ApacheCliOptionsProvider(args);
        try {
            optProvider.initialize();

            // Collect variables
            String inputFile = optProvider.getInputFile();
            String outputFile = optProvider.getOutputFile();
            boolean showDistribution = optProvider.getShowDistribution();

            // Run weka classifier
            WekaClassifier wekaClassifier = new WekaClassifier(inputFile, outputFile, showDistribution);
        } catch (ParseException ex) {
            System.err.println("ERROR: Parsing failed!\n   Reason: " + ex.getMessage() + "\n");
            optProvider.printFormattedHelp();
        }
    }
}
