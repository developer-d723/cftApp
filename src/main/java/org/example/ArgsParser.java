package org.example;

import org.example.file.InputFileHandler;
import org.example.file.OutputFileHandler;
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


@Command(name = "util", mixinStandardHelpOptions = true, version = "1.0",
        description = " ")
public class ArgsParser implements Callable<Integer> {

    @Parameters(index = "0..*", description = "input files")
    private List<File> inputFiles;

    @Option(names = "-o", description = "output path for resulting files, default is current directory")
    private Path outputPath = Paths.get("");

    @Option(names = "-p", description = "prefix for output file names")
    private String prefix = "";

    @Option(names = "-a", description = "append to existing files instead of overwriting")
    private boolean appendMode = false;

    // mulitiplicity = "1" means that statistics arguments are mutually exclusive
    @CommandLine.ArgGroup(multiplicity = "1")
    StatisticsArguments statisticsArguments;

    static class StatisticsArguments {
        @Option(names = "-s", required = true, description = "display short statistics")
        boolean shortStatistics;
        @Option(names = "-f", required = true, description = "display full statistics")
        boolean fullStatistics;
    }


    @Override
    public Integer call() {

        boolean isFullStatistics = statisticsArguments.fullStatistics;
        StatisticsManager statisticsManager = new StatisticsManager(isFullStatistics);
        InputFileHandler inputFileHandler = new InputFileHandler(inputFiles);
        LineFormatChecker lineFormatChecker = new LineFormatChecker();


        try (OutputFileHandler outputHandler = new OutputFileHandler(outputPath, prefix, appendMode)) {

            inputFileHandler.handleLines(line -> {

                CheckLineFormatResult result = lineFormatChecker.check(line);
                try {
                    outputHandler.writeToFile(result);
                    statisticsManager.collectStatistics(result);
                } catch (IOException e) {
                    System.err.println("Could not write line " + line + ", skipping. Error message: "
                            + e.getMessage());
                }
            });
        } catch (IOException e) {

            System.err.println("A fatal error occurred with the output files: " + e.getMessage());
            return 1; // fatal error code
        }
        System.out.println("\nStatistics:");
        statisticsManager.printStatistics();
        return 0; // success code
    }
}