package nl.bioinf.vktalen.thema09_ml;

import java.io.InputStream;
import java.util.Arrays;

import weka.classifiers.AbstractClassifier;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

public class WekaClassifier {
    private final String modelFile = "/SimpleLogistic.model";
    private AbstractClassifier classifier;
    private String outputFile;

    public WekaClassifier(String inputFile, String outputFile, boolean showDistribution) {
        try {
            // Load classifier
            this.classifier = loadClassifier();

            // Save option values from optProvider
            this.outputFile = outputFile;

            // Load data into Instances object
            Instances unclassifiedInstances = loadArff(inputFile);

            // Classify all instances
            Instances classifiedInstances;
            if (!showDistribution) {
                classifiedInstances = classifyInstances(unclassifiedInstances);
            } else {
                classifiedInstances = distributionClassifyInstances(unclassifiedInstances);
            }
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
            throw new Exception(String.format("Failed to load classifier -> (%s) %s",
                    ex.getClass().getSimpleName(), ex.getMessage())
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
            throw new Exception(String.format("Given data file could not be read -> (%s) %s",
                    ex.getClass().getSimpleName(), ex.getMessage())
            );
        }
    }

    private void addDistributionAttributes(Instances instancesObject) {
        // Get class attribute object
        Attribute classAttribute = instancesObject.classAttribute();

        // For each label in class attribute
        for (int i = 0; i < classAttribute.numValues(); i++) {
            String curLabel = classAttribute.value(i);
            Attribute newAttribute = new Attribute(curLabel + "_distribution");
            instancesObject.insertAttributeAt(newAttribute, instancesObject.numAttributes());
        }
    }

    private Instances classifyInstances(Instances unclassifiedInstances) throws Exception {
        // Copy dataset that will get instances labeled
        Instances labeledInstances = new Instances(unclassifiedInstances);

        // Classify instances and set their class label in the new dataset
        for (int i = 0; i < unclassifiedInstances.numInstances(); i++) {
            double classIndex = classifier.classifyInstance(unclassifiedInstances.instance(i));
            labeledInstances.instance(i).setClassValue(classIndex);

            // Print to either console or save to file
            if (outputFile == null) {
                String classLabel = labeledInstances.classAttribute().value((int) classIndex);
                System.out.println("Instance " + (i + 1) + ": classified as " + classLabel);
            }
        }
        System.out.println("\nLabeled dataset: \n" + labeledInstances);
        return labeledInstances;
    }

    private Instances distributionClassifyInstances(Instances unclassifiedInstances) throws Exception {
        // Copy dataset that will get instances labeled
        Instances labeledInstances = new Instances(unclassifiedInstances);

        // Add attribute columns and get their indices in the dataset
        addDistributionAttributes(labeledInstances);

        // Classify instances and set their class label in the new dataset
        for (int i = 0; i < unclassifiedInstances.numInstances(); i++) {
            // Get distributions for classification labels
            double[] distributions = classifier.distributionForInstance(unclassifiedInstances.instance(i));

            // Get the index of the highest distribution present
            int maxAtIndex = 0;
            for (int j = 0; j < distributions.length; j++) {
                maxAtIndex = distributions[j] > distributions[maxAtIndex] ? j : maxAtIndex;
                int curDatasetAttributeIndex = unclassifiedInstances.numAttributes() + j;
                labeledInstances.instance(i).setValue(curDatasetAttributeIndex, distributions[j]);
            }

            labeledInstances.instance(i).setClassValue(maxAtIndex);

            if (outputFile == null) {
                String classLabel = labeledInstances.classAttribute().value(maxAtIndex);
                System.out.println("Instance " + (i + 1) + ": classified as " + classLabel);
                System.out.println("\tDistributions: " + Arrays.toString(distributions));
            }
        }
        System.out.println("\nLabeled dataset: \n" + labeledInstances);
        return labeledInstances;
    }
}
