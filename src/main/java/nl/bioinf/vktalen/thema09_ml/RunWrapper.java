package nl.bioinf.vktalen.thema09_ml;

import org.apache.commons.cli.ParseException;

public final class RunWrapper {
    private RunWrapper() {}

    public static void main(String[] args) {
        ApacheCliOptionsProvider optProvider = new ApacheCliOptionsProvider(args);
        try {
            optProvider.initialize();

            // Collect variables
            String inputFile = optProvider.getInputFile();
            String outputFile = optProvider.getOutputFile();
        } catch (ParseException ex) {
            System.err.println("ERROR: Parsing failed!\n   Reason: " + ex.getMessage() + "\n");
            optProvider.printFormattedHelp();
        }
    }
}
