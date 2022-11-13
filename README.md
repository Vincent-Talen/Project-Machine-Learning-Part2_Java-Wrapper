# Introduction to Machine Learning (Part 2: Java Wrapper)
**Hanzehogeschool Groningen: Bioinformatics Project Year 3, Period 9**

Creating a Java Wrapper around a machine learning model to make classifying instances easy.

## About the application
This application was created to make classifying instances with a created machine learning model easier.
The final result is a standalone Java Archiver (.jar) application that can be used from the command line.


## Data
The model that is built into the application predicts if a breast mass sample is malignant or benign.  
A machine learning algorithm was used on the [Breast Cancer Wisconsin (Diagnostic)](https://archive.ics.uci.edu/ml/datasets/Breast+Cancer+Wisconsin+%28Diagnostic%29) dataset, which has 30 features about the cell nucleus boundaries of the samples. 
The data was gathered by taking fine needle aspirates from breast masses which were then imaged through a microscope and processed with the program Xcyt to compute the nucleus boundary features. 
Machine learning algorithms were then applied on the 569 samples of the dataset to train the final model with 98% accuracy.


## Repository File Structure
### Project Tree
```bash
Project-Machine-Learning-Part2_Java-Wrapper
├── build.gradle
├── settings.gradle
├── LICENSE
├── README.md
├── testdata
│   └── unlabeled_data.arff
└── src
    └── main
        ├── java
        │   ├── ApacheCliOptionsProvider.java
        │   ├── RunWrapper.java
        │   └── WekaClassifier.java
        └── resources
            └── SimpleLogistic.model
```

### .gradle files
These files create the structure of the project, they are used by Gradle to include and use the correct dependencies and build the final .jar.

### testdata/
In here is a single .arff file that can be used for testing the application, it has a diagnosis class column but all values are NA's.

### src/main/
All the scripts and resources that make up this project reside in this directory. 
It has two subdirectories, one called `java` with all the class scripts and `resources`, which has the classification model.  

#### java/
  - `ApacheCliOptionsProvider.java`: This class takes all command line arguments and creates a parser with options for the application. 
  - `RunWrapper.java`: The main class that's designated by Gradle to get called when running the final application.
  - `WekaClassifier.java`: Uses the model from the `resources` directory to classify instances and optionally save them to a new .arff file.


## Installation
To run the final .jar application only Java is required to be on your system, which can be downloaded from [the Java website download page](https://www.java.com/en/download/).

> If it is desired to create a new build of the .jar application and possibly change the code of the scripts, a few more things are needed. 
It is recommended to use [JetBrains IntelliJ IDEA](https://www.jetbrains.com/idea/) with Java SDK 17, since this is how this project was developed. 
In IntelliJ a project with Gradle structure should be created from the repository directory.  


## Usage
To use the final application download the latest release from [here](https://github.com/Vincent-Talen/Project-Machine-Learning-Part2_Java-Wrapper/releases).
There are 3 command line options excluding the help, an option to specify the input-file, one for the output-file and a flag to enable showing the distributions of class labels.

### Examples
The most basic usage is to only show the help, which can be done like this:
> $java -jar Theme09-ML-Application-0.3.0.jar -h

To simply classify the instances of a file and have the results printed to the console:
> $java -jar Theme09-ML-Application-0.3.0.jar -i <testdata/unlabeled_data.arff>

To show distributions as well, use the `-d` or `--showDistribution` flag:
> $java -jar Theme09-ML-Application-0.3.0.jar -i <testdata/unlabeled_data.arff> -d

The application can also save the new dataset to an arff file when the `-o` option is used
> $java -jar Theme09-ML-Application-0.3.0.jar -i <testdata/unlabeled_data.arff> -o <classified.arff>

If the showDistribution option flag is used together with the output option the dataset saved to the file will have two new attributes for the distributions at the end of the file.

### Building the .jar again
When a working IntelliJ project has been created with Gradle structure a vertical `Gradle` button should be on the right side of the application.  
Then with `Tasks > shadow > shadowJar` a jar can be created that has all the dependencies included in it, enabling for standalone use.


## Useful links
* [Project Info Page](https://michielnoback.github.io/bincourses/data_mining_project.html)
* [Link to original data set on UCI Machine Learning Repository](https://archive.ics.uci.edu/ml/datasets/Breast+Cancer+Wisconsin+%28Diagnostic%29)
* [Project Repository 1: Analysis](https://github.com/Vincent-Talen/Project-Machine-Learning-Part1_Analysis)
* [Project Repository 2: Java Wrapper](https://github.com/Vincent-Talen/Project-Machine-Learning-Part2_Java-Wrapper)


## Contact
Vincent Talen  
v.k.talen@st.hanze.nl