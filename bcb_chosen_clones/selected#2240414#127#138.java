    public static void copyFromFileToFileUsingNIO(File inputFile, File outputFile) throws FileNotFoundException, IOException {
        FileChannel inputChannel = new FileInputStream(inputFile).getChannel();
        FileChannel outputChannel = new FileOutputStream(outputFile).getChannel();
        try {
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
        } catch (IOException e) {
            throw e;
        } finally {
            if (inputChannel != null) inputChannel.close();
            if (outputChannel != null) outputChannel.close();
        }
    }
