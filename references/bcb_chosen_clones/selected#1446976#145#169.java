    private void copyTemplateFile(String sourceRoot, String targetRoot, String extension) throws Exception {
        String inputFileName = sourceRoot + extension;
        String outputFileName = targetRoot + extension;
        System.out.println("Copying resource file: " + outputFileName);
        File inputFile = new File(inputFileName);
        if (!inputFile.exists() || !inputFile.canRead()) {
            throw new Exception("Could not read from the file " + inputFileName);
        }
        File outputFile = new File(outputFileName);
        if (!outputFile.exists()) {
            if (!outputFile.createNewFile() || !outputFile.canWrite()) throw new Exception("Could not write to the file " + outputFileName);
        }
        FileReader in = new FileReader(inputFile);
        FileWriter out = new FileWriter(outputFile);
        try {
            char[] buffer = new char[1024];
            int nread = 0;
            while ((nread = in.read(buffer)) != -1) {
                out.write(buffer, 0, nread);
            }
        } finally {
            in.close();
            out.close();
        }
    }
