    public static void main(final String[] args) throws RecognitionException, TokenStreamException, IOException, IllegalOptionValueException, UnknownOptionException {
        try {
            CmdLineParser cmdLineParser = new CmdLineParser();
            Option formatOption = cmdLineParser.addStringOption('f', "format");
            Option encodingOption = cmdLineParser.addStringOption('c', "charset");
            cmdLineParser.parse(args);
            String format = (String) cmdLineParser.getOptionValue(formatOption);
            String encoding = (String) cmdLineParser.getOptionValue(encodingOption);
            if (encoding == null || encoding.trim().equals("")) {
                encoding = "utf-8";
                System.out.println("Defaulting to output charset utf-8 as argument -c is missing or not valid.");
            }
            String[] remainingArgs = cmdLineParser.getRemainingArgs();
            if (remainingArgs.length != 2) {
                printUsage("Input and output file are not specified correctly. ");
            }
            File inputFile = new File(remainingArgs[0]);
            if (!inputFile.exists()) {
                printUsage("Input file " + remainingArgs[0] + " does not exist. ");
            }
            File outputFile = new File(remainingArgs[1]);
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            if (format == null || format.trim().equals("")) {
                format = (String) FileUtil.cutExtension(outputFile.getName()).getValue();
            }
            if ("tex".equals(format)) {
                Reader reader = new LatexEncoderReader(new FileReader(inputFile));
                OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(outputFile), encoding);
                char[] buffer = new char[1024];
                int read;
                do {
                    read = reader.read(buffer);
                    if (read > 0) {
                        out.write(buffer, 0, read);
                    }
                } while (read != -1);
                out.flush();
                out.close();
            } else {
                printUsage("Format not specified via argument -f. Also guessing for the extension of output file " + outputFile.getName() + " failed");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            printUsage(ex.getMessage());
        }
    }
