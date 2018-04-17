    void run(PseudolocalizerArguments arguments) throws IOException {
        List<String> fileNames = arguments.getFileNames();
        PseudolocalizationPipeline pipeline = arguments.getPipeline();
        if (arguments.isInteractive()) {
            runStdin(pipeline);
            return;
        }
        if (fileNames.size() == 0) {
            MessageCatalog msgCat = FormatRegistry.getMessageCatalog(arguments.getType());
            writeMessages(msgCat, readAndProcessMessages(pipeline, msgCat, System.in), System.out);
            return;
        }
        String suffix = arguments.getVariant();
        if (suffix == null) {
            suffix = "_pseudo";
        } else {
            suffix = "_" + suffix;
        }
        for (String fileName : fileNames) {
            File file = new File(fileName);
            if (!file.exists()) {
                System.err.println("File " + fileName + " not found");
                continue;
            }
            int lastDot = fileName.lastIndexOf('.');
            String extension;
            String outFileName;
            if (lastDot >= 0) {
                extension = fileName.substring(lastDot + 1);
                outFileName = fileName.substring(0, lastDot) + suffix + "." + extension;
            } else {
                extension = "";
                outFileName = fileName + suffix;
            }
            System.out.println("Processing " + fileName + " into " + outFileName);
            String fileType = arguments.getType();
            if (fileType == null) {
                fileType = extension;
            }
            MessageCatalog msgCat = FormatRegistry.getMessageCatalog(fileType);
            InputStream inputStream = new FileInputStream(file);
            List<Message> processedMessages = readAndProcessMessages(pipeline, msgCat, inputStream);
            OutputStream outputStream = new FileOutputStream(new File(outFileName));
            writeMessages(msgCat, processedMessages, outputStream);
        }
    }
