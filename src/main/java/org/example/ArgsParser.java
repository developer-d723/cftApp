package org.example;

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

    @Option(names = "-o", description = "output path for result files, default is current directory")
    private Path outputPath = Paths.get("");

    @Option(names = "-s", description = "display short statistics")
    private boolean shortStats = false;

    @Option(names = "-f", description = "display full statistics")
    private boolean fullStats = true;

    @Option(names = "-p", description = "prefix for output file names")
    private String prefix = "";

    @Option(names = "-a", description = "append to existing files instead of overwriting")
    private boolean appendMode = false;

    @Override
    public Integer call() throws Exception {
        return 0;
    }
}