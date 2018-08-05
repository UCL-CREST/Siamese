    public static void main(final String[] args) throws RecognitionException, TokenStreamException, IOException, IllegalOptionValueException, UnknownOptionException {
        try {
            CmdLineParser cmdLineParser = new CmdLineParser();
            Option formatOption = cmdLineParser.addStringOption('f', "format");
            Option outputEncodingOption = cmdLineParser.addStringOption('c', "outcharset");
            Option inputEncodingOption = cmdLineParser.addStringOption('i', "incharset");
            cmdLineParser.parse(args);
            String format = (String) cmdLineParser.getOptionValue(formatOption);
            String outputEncoding = (String) cmdLineParser.getOptionValue(outputEncodingOption);
            if (outputEncoding == null || outputEncoding.trim().equals("")) {
                outputEncoding = "utf-8";
                System.out.println("Defaulting to output charset utf-8 as argument -c is missing or not valid.");
            }
            String inputEncoding = (String) cmdLineParser.getOptionValue(inputEncodingOption);
            if (inputEncoding == null || outputEncoding.trim().equals("")) {
                inputEncoding = "utf-8";
                System.out.println("Defaulting to input charset utf-8 as argument -i is missing or not valid.");
            }
            String[] remainingArgs = cmdLineParser.getRemainingArgs();
            if (remainingArgs.length != 2) {
                printUsage("Input and output file are not specified correctly. ");
            }
            File inputFile = new File(remainingArgs[0]);
            if (!inputFile.exists()) {
                printUsage("Input file " + remainingArgs[0] + " does not exist. ");
            }
            if (format == null || format.trim().equals("")) {
                format = (String) FileUtil.cutExtension(inputFile.getName()).getValue();
            }
            File outputFile = new File(remainingArgs[1]);
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            System.out.println("format detected: " + format);
            if ("html".equals(format)) {
                Reader reader = new HtmlEntityDecoderReader(new InputStreamReader(new FileInputStream(inputFile), inputEncoding));
                OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(outputFile), outputEncoding);
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
                printUsage("Format not specified via argument -f. Also guessing for the extension of input file " + inputFile.getName() + " failed");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            printUsage(ex.getMessage());
        }
    }
