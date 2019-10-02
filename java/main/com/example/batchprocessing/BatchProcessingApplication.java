package com.example.batchprocessing;

import org.apache.commons.cli.*;

import java.io.File;
import java.util.List;

public class BatchProcessingApplication {

    public static void main(String[] args) throws Exception {
// create the command line parser
        CommandLineParser parser = new DefaultParser();

// create the Options
        Options options = new Options();
        options.addOption("d", "data", true, "palo alto config xml file");
        options.addOption("m", "match", true, "file with multi line ");
        options.addOption("h", "help", false, "print help info");

        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);
            List<String> argList = line.getArgList();

            // validate that block-size has been set
            if (line.hasOption("help") || !line.hasOption("data") || (argList.size() == 0 && !line.hasOption("match"))) {
                // automatically generate the help statement
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("sgrep", options);

                return;
            }

            String data = line.getOptionValue("data");
            String match = line.getOptionValue("match");

            File xmlFile = new File(data);

            if (argList.size() > 0) {
                SuperGrep.grep(xmlFile, argList);
            } else {
                File matchFile = new File(match);
                SuperGrep.grep(xmlFile, matchFile);
            }

        } catch (ParseException exp) {
            System.out.println("Unexpected exception:" + exp.getMessage());
        }
    }
}
