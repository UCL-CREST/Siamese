    public static boolean copyFileToDir(File inputFile, File outputDir) {
        try {
            String outputFileName = inputFile.getName();
            int index = 1;
            while (existFileInDir(outputFileName, outputDir)) {
                outputFileName = index + inputFile.getName();
                index++;
            }
            String directory = getDirectoryWithSlash(outputDir.getAbsolutePath());
            File outputFile = new File(directory + outputFileName);
            FileReader in = new FileReader(inputFile);
            FileWriter out = new FileWriter(outputFile);
            int c;
            while ((c = in.read()) != -1) out.write(c);
            in.close();
            out.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
