    public static void copyFile(String inputFile, String outputFile) throws IOException {
        FileInputStream fis = new FileInputStream(inputFile);
        FileOutputStream fos = new FileOutputStream(outputFile);
        for (int b = fis.read(); b != -1; b = fis.read()) fos.write(b);
        fos.close();
        fis.close();
    }
