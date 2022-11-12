package nl.bioinf.vktalen.thema09_ml;

import java.io.InputStream;

import weka.classifiers.AbstractClassifier;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

public class WekaClassifier {
    private final String modelFile = "/SimpleLogistic.model";
    private AbstractClassifier classifier;

    public WekaClassifier(String inputFile) {
        try {
            // Load classifier
            this.classifier = loadClassifier();

            // Load data into Instances object
            Instances unclassifiedInstances = loadArff(inputFile);
            // Classify all instances
            classifyInstances(unclassifiedInstances);
        } catch (Exception ex) {
            System.err.println("ERROR: WekaClassifier failed!");
            System.err.printf("  Reason: %s %n%n", ex.getMessage());
            System.exit(1);
        }
    }

    private AbstractClassifier loadClassifier() throws Exception {
        try {
            // In a java archive (.jar) the file is already a stream
            InputStream in = getClass().getResourceAsStream(modelFile);
            return (AbstractClassifier) SerializationHelper.read(in);
        } catch (Exception ex) {
            throw new Exception(String.format(
                    "Failed to load classifier -> (%s) %s",
                    ex.getClass().getSimpleName(),
                    ex.getMessage())
            );
        }
    }

    private Instances loadArff(String datafile) throws Exception {
        try {
            // Load data from file and turn into Instances object
            DataSource source = new DataSource(datafile);
            Instances data = source.getDataSet();

            // Set index of class attribute to last column
            data.setClassIndex(data.numAttributes() - 1);
            return data;
        } catch (Exception ex) {
            throw new Exception(String.format(
                    "Given data file could not be read -> (%s) %s",
                    ex.getClass().getSimpleName(),
                    ex.getMessage())
            );
        }
    }

    private Instances classifyInstances(Instances unclassifiedInstances) throws Exception {
        // Copy dataset that will get instances labeled
        Instances labeledInstances = new Instances(unclassifiedInstances);

        // Classify instances and label them in the new dataset
        for (int i = 0; i < unclassifiedInstances.numInstances(); i++) {
            double clsLabel = classifier.classifyInstance(unclassifiedInstances.instance(i));
            labeledInstances.instance(i).setClassValue(clsLabel);
        }
        // Print to terminal
        System.out.println("\nLabeled dataset: \n" + labeledInstances);
        return labeledInstances;
    }
}
