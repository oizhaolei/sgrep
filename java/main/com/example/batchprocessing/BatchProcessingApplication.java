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
        options.addOption("d", "data", true, "palo alto config xml file or any data file");
        options.addOption("h", "help", false, "print help info");

        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);
            List<String> argList = line.getArgList();

            // validate that block-size has been set
            if (line.hasOption("help") || !line.hasOption("data") || argList.size() == 0) {
                // automatically generate the help statement
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("sgrep", options);

                return;
            }

            String data = line.getOptionValue("data");
            File xmlFile = new File(data);

            if (argList.size() > 0) {
            	File matchFile = new File(argList.get(0));
            	if (matchFile.exists()) {
                    SuperGrep.grep(xmlFile, matchFile);
            	} else {
                    SuperGrep.grep(xmlFile, argList);
            	}
            }

        } catch (ParseException exp) {
            System.out.println("Unexpected exception:" + exp.getMessage());
        }
    }
}
