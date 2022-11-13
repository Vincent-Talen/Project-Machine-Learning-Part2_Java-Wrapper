package nl.bioinf.vktalen.thema09_ml;

import java.io.InputStream;

import weka.classifiers.AbstractClassifier;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSink;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * Class that uses and implements part of the Weka Java API to classify instances.
 * It reads data from an .arff file, classifies the instances from it and
 * optionally saves the resulting dataset to a new .arff file.
 *
 * @author Vincent Talen (389015)
 */
public class WekaClassifier {
    private AbstractClassifier classifier;
    private String outputFile;
    private boolean showDistribution;

    /**
     * Constructor method that performs the entire classifying process.
     * It loads the dataset from the inputFile, classifies instances,
     * if specified by showDistribution it also calculates the distributions.
     * When outputFile is not null it is tried to save the new dataset to it.
     *
     * @param inputFile An .arff type file with unlabeled instances
     * @param outputFile File to save resulting new dataset to, can also be null
     * @param showDistribution If the distribution for classes should be shown
     */
    public WekaClassifier(String inputFile, String outputFile, boolean showDistribution) {
        try {
            // Load classifier
            this.classifier = loadClassifier();

            // Save option values from optProvider
            this.outputFile = outputFile;
            this.showDistribution = showDistribution;

            // Load data into Instances object
            Instances unclassifiedInstances = loadArff(inputFile);
            if (outputFile != null) {
                System.out.printf("Successfully loaded input-file '%s'%n", inputFile);
            }

            // Classify all instances
            Instances classifiedInstances = classifyInstances(unclassifiedInstances);

            // Save to output file
            if (outputFile != null) {
                System.out.println("All instances have been classified!");
                saveToOutputFile(classifiedInstances);
            }
        } catch (Exception ex) {
            System.err.println("ERROR: WekaClassifier failed!");
            System.err.printf("  Reason: %s %n%n", ex.getMessage());
            System.exit(1);
        }
    }

    /**
     * Loads a classifier from a stream in the java archive of a .model file.
     *
     * @return classifier The classifier that was loaded from a .model stream in the .jar
     * @throws Exception Has a formatted message specifying that the error happened in this method
     */
    private AbstractClassifier loadClassifier() throws Exception {
        String modelFile = "/SimpleLogistic.model";
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

    /**
     * Loads a dataset from an .arff file.
     *
     * @param datafile The name of the .arff file that needs to be read.
     * @return unclassifiedInstances The dataset from the given datafile as an Instances object.
     * @throws Exception Has a formatted message specifying that the error happened in this method
     */
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

    /**
     * Adds distribution attributes for each class label in an Instances object.
     *
     * @param instancesObject An instances object with a class index specified
     */
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

    /**
     * Classifies instances from a dataset. 
     * Checks the class property showDistribution if class distributions should be shown.
     *
     * @param unclassifiedInstances An Instances object whose instances need to be classified
     * @return Dataset that has all instances labeled and if specified distribution attributes added
     * @throws Exception Has a formatted message specifying that the error happened in this method
     */
    private Instances classifyInstances(Instances unclassifiedInstances) throws Exception {
        try {
            // Copy dataset that will get instances labeled
            Instances labeledInstances = new Instances(unclassifiedInstances);
            // Add distribution attributes for the class labels if needed
            if (showDistribution) {
                addDistributionAttributes(labeledInstances);
            }

            // For each instance, by index
            for (int i = 0; i < unclassifiedInstances.numInstances(); i++) {
                // Classify instance and set its class value
                double clsIndex = classifier.classifyInstance(unclassifiedInstances.instance(i));
                labeledInstances.instance(i).setClassValue(clsIndex);

                // Create StringBuilder object to print the distribution nicely
                StringBuilder distrStrBuilder = new StringBuilder();
                if (showDistribution) {
                    // Start distribution string
                    distrStrBuilder.append("\tDistribution: ");

                    // Get distribution for classification labels
                    double[] distribution = classifier.distributionForInstance(unclassifiedInstances.instance(i));

                    // Add each distribution value to it's corresponding attribute for the current instance
                    for (int j = 0; j < distribution.length; j++) {
                        // Get the index in dataset of the current attribute and with it set the value
                        int curDatasetAttributeIndex = unclassifiedInstances.numAttributes() + j;
                        labeledInstances.instance(i).setValue(curDatasetAttributeIndex, distribution[j]);

                        // Add class label name and distribution to the distribution string
                        String curClassLabel = labeledInstances.classAttribute().value(j);
                        distrStrBuilder.append(String.format("(%s=%.4f) ", curClassLabel, distribution[j]));
                    }
                }
                // If no output file is given print results to command line
                if (outputFile == null) {
                    String clsLabel = labeledInstances.classAttribute().value((int) clsIndex);
                    System.out.printf("Instance %d: classified as %s%s%n", (i + 1), clsLabel, distrStrBuilder);
                }
            }
            return labeledInstances;
        } catch (Exception ex) {
            throw new Exception(String.format("Classifying instances was unsuccessful! -> (%s) %s",
                    ex.getClass().getSimpleName(), ex.getMessage())
            );
        }
    }

    /**
     * Saves the given dataset to the output file in the outputFile class property.
     *
     * @param dataset The dataset that should be saved.
     * @throws Exception Has a formatted message specifying that the error happened in this method
     */
    private void saveToOutputFile(Instances dataset) throws Exception {
        try {
            DataSink.write(outputFile, dataset);
            System.out.printf("Successfully saved data to '%s'!%n", outputFile);
        } catch (Exception ex) {
            throw new Exception(String.format("Saving data to '%s' was unsuccessful! -> (%s) %s",
                    outputFile, ex.getClass().getSimpleName(), ex.getMessage())
            );
        }
    }
}
