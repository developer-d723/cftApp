package org.example;

import org.example.file.InputFileHandler;
import org.example.file.OutputFileHandler;
import org.example.file.validation.ErrorType;
import org.example.file.validation.PathValidator;
import org.example.file.validation.ValidationException;
import org.example.line.CheckLineFormatResult;
import org.example.line.LineFormatChecker;
import org.example.statistics.StatisticsManager;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * This class uses the Picocli CLI library to process text files.
 * <p>
 * It parses text files, checks the format of each line, and writes them to output files based on their type.
 * Supported types are integer, string and float. Supports prefix for the output files names.
 * The output files can either be appended to or overwritten, depending on the user's choice.
 * The class also displays processing statistics.
 */

@Command(name = "util", mixinStandardHelpOptions = true, version = "1.0",
        description = " ")
public class ArgsParser implements Callable<Integer> {

    @Parameters(index = "0..*", description = "input files")
    private List<File> inputFiles;

    @Option(names = "-o", description = "output path for resulting files, default is current directory")
    private final Path outputPath = Paths.get("");

    @Option(names = "-p", description = "prefix for output file names")
    private final String prefix = "";

    @Option(names = "-a", description = "append to existing files instead of overwriting")
    private final boolean appendMode = false;

    // mulitiplicity = "1" means that statistics arguments are mutually exclusive
    @CommandLine.ArgGroup(multiplicity = "1")
    StatisticsArguments statisticsArguments;

    static class StatisticsArguments {
        @Option(names = "-s", required = true, description = "display short statistics")
        boolean shortStatistics;
        @Option(names = "-f", required = true, description = "display full statistics")
        boolean fullStatistics;
    }


    /**
     * The main entry point called by Picocli after parsing the command-line arguments.
     * Responsible for coordinating the entire file filtering process.
     *
     * @return 0 if successful, 1 in case of a critical failure.
     */

    @Override
    public Integer call() throws ValidationException { // Объявляем, что метод может выбросить наше исключение

        allPathsValidation();

        boolean isFullStatistics = statisticsArguments.fullStatistics;
        StatisticsManager statisticsManager = new StatisticsManager(isFullStatistics);
        InputFileHandler inputFileHandler = new InputFileHandler(inputFiles);

        System.out.printf("Output path: '%s', Prefix: '%s', Append mode: %s%n",
                outputPath.toAbsolutePath().normalize(), prefix, appendMode);


        try (OutputFileHandler outputHandler = new OutputFileHandler(outputPath, prefix, appendMode)) {
            inputFileHandler.handleLines(line -> {
                CheckLineFormatResult result = LineFormatChecker.check(line);
                try {
                    outputHandler.writeToFile(result);
                    statisticsManager.collectStatistics(result);
                } catch (IOException e) {
                    System.err.println("Could not write line '" + line + "', skipping. Error: " + e.getMessage());
                }
            });
        } catch (IOException e) {
            System.err.println("A critical I/O error occurred with the output files: " + e.getMessage());
            return 1; // fatal error
        }

        System.out.println("\nFinished. Statistics:");
        statisticsManager.printStatistics();
        return 0; // success
    }

    /**
     * Validation checks for command-line paths.
     *
     * @throws ValidationException if any check fails.
     */
    private void allPathsValidation() throws ValidationException {
        PathValidator pathValidator = new PathValidator();
        pathValidator.validateNameSyntax(prefix);
        pathValidator.validateOutputPath(outputPath);

        if (inputFiles == null || inputFiles.isEmpty()) {

            throw new ValidationException(ErrorType.INPUT_FILE_NOT_PROVIDED,
                    "At least one input file must be specified.");
        }
        for (File inputFile : inputFiles) {
            pathValidator.validateInputFile(inputFile);
        }
    }
}

















