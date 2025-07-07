package org.example;

import org.example.file.InputFileHandler;
import org.example.file.OutputFileHandler;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
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

    @CommandLine.ArgGroup(multiplicity = "1")
    StatisticsArguments statisticsArguments;

    @Option(names = "-p", description = "prefix for output file names")
    private String prefix = "";

    @Option(names = "-a", description = "append to existing files instead of overwriting")
    private boolean appendMode = false;


    static class StatisticsArguments {
        @Option(names = "-s", required = true, description = "display short statistics")
        boolean shortStatistics;

        @Option(names = "-f", required = true, description = "display full statistics")
        boolean fullStatistics;
    }





    @Override
    public Integer call() throws Exception {
        InputFileHandler inputFileHandler = new InputFileHandler(inputFiles);
        OutputFileHandler outputFileHandler = new OutputFileHandler(outputPath, prefix);
        return 0;
    }
}