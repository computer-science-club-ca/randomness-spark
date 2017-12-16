import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import randomX.*;

/**
 * Exercise : Generate random values samples from different random generators
 * then analyze data with Spark :)
 */
public class App {

    private static final int ITERATIONS = 250000;
    private static final String PATH = "d:/random-generator-analysis/";

    public static void main(String[] args) {
        // List of the data files
        String[] files = { "JavaRandom.txt", "LEcuyer.txt", "LCG.txt", "MCG.txt", "HotBits.txt" };

        generateSamples(files);

        Logger.getLogger("org").setLevel(Level.ERROR);
        SparkConf conf = new SparkConf().setAppName("countOccurences").setMaster("local[*]");
        JavaSparkContext sparkContext = new JavaSparkContext(conf);

        analyseData(PATH + files[0], sparkContext);
        analyseData(PATH + files[1], sparkContext);
        analyseData(PATH + files[2], sparkContext);
        analyseData(PATH + files[3], sparkContext);
        analyseData(PATH + files[4], sparkContext);

        System.out.println("\n\nTHE PROCESSES SUCCESSFULLY ENDED! :)");
    }

    /**
     * Generate samples : write random numbers in data files.
     * 
     * @param files
     *            (String[]), list of data filenames
     */
    private static void generateSamples(String[] files) {
        // using the java standard random library
        String content = generateNumbers(new Random(), ITERATIONS);
        writeInFile(PATH + files[0], content);

        // Creates a new pseudorandom number generator, seeded from the current time.
        content = generateNumbers(new randomLEcuyer(), ITERATIONS, false, false);
        writeInFile(PATH + files[1], content);

        // using the simple linear congruential generator
        // given as an example in the ANSI C specification.
        content = generateNumbers(new randomLCG(), ITERATIONS, false, false);
        writeInFile(PATH + files[2], content);

        // using the "Minimal Standard" multiplicative congruential generator of Park
        // and Miller.
        content = generateNumbers(new randomMCG(), ITERATIONS, false, false);
        writeInFile(PATH + files[3], content);

        // radioactively-generated random data from the HotBits generator... It's a long
        // process since the app calls their api
        content = generateNumbers(new randomHotBits("Pseudorandom"), ITERATIONS, true, true);
        writeInFile(PATH + files[4], content);

        System.out.println("Data files are ready! See the files : " + PATH);
    }

    /**
     * Generate random values of positive numbers
     * 
     * @param randomGenerator
     *            (Random), the Java random generator
     * @param numberOfValues
     *            (int), number of random values
     * @return randomNumbers (String), string of numbers separated with spaces
     */
    private static String generateNumbers(Random randomGenerator, int numberOfValues) {
        StringBuilder randomNumbers = new StringBuilder();
        for (int i = 0; i < numberOfValues; i++) {
            int randomInt = randomGenerator.nextInt();
            randomInt = Math.abs(randomInt);
            randomNumbers.append(String.valueOf(randomInt) + " ");
        }
        return randomNumbers.toString();
    }

    /**
     * Generate random values of positive numbers
     * 
     * @param randomGenerator
     *            (randomX), the Java random generator
     * @param numberOfValues
     *            (int), number of random values
     * @param isShowProgress
     *            (boolean), token to show progression in the console
     * @param isTakePauses
     *            (boolean), token to take a pause every 250000 iterations
     * @return randomNumbers(String), string of numbers separated with spaces
     */
    private static String generateNumbers(randomX randomGenerator, int numberOfIterations, boolean isShowProgress,
            boolean isTakePauses) {
        StringBuilder randomNumbers = new StringBuilder();
        for (int i = 0; i < numberOfIterations; i++) {
            int randomInt = randomGenerator.nextInt();
            randomInt = Math.abs(randomInt);
            randomNumbers.append(String.valueOf(randomInt) + " ");

            if (isShowProgress && i % 1000 == 0) {
                System.out.println("Please wait. The system process the request... Period # " + i);
            }

            if (isTakePauses && i % 250000 == 0) {
                try {
                    System.out.println("Please wait. The system takes a 10 seconds break...");
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return randomNumbers.toString();
    }

    /**
     * Write content in a file
     * 
     * @param path
     *            (String), path and filename
     * @param content
     *            (String), content to write
     */
    private static void writeInFile(String path, String content) {
        try {
            Files.write(Paths.get(path), content.getBytes());
            System.out.println("Data have been compile : " + path);
        } catch (IOException error) {
            System.err.println(error.getMessage());
        }
    }

    /**
     * Analyze data : write the list the values that occurred more than once and
     * count them.
     * 
     * @param path
     *            (String), path and data filename
     * @param sparkContext
     *            (JavaSparkContext), the instance to analyze data with Spark
     */
    private static void analyseData(String path, JavaSparkContext sparkContext) {
        JavaRDD<String> lines = sparkContext.textFile(path);
        JavaRDD<String> numbers = lines.flatMap(line -> Arrays.asList(line.split(" ")).iterator());
        StringBuilder analysis = new StringBuilder();
        analysis.append("Sample : " + path);
        analysis.append("\nSample's size : " + numbers.count());

        Map<String, Long> wordCounts = numbers.countByValue();

        analysis.append("\nNumbers with more than 1 occurences : \n");
        for (Map.Entry<String, Long> entry : wordCounts.entrySet()) {
            if (entry.getValue() > 1) {
                analysis.append(entry.getKey() + " : " + entry.getValue() + "\n");
            }
        }
        writeInFile(path + ".results.txt", analysis.toString());
        System.out.println("Results files are ready! See the files : " + PATH);
    }

}
