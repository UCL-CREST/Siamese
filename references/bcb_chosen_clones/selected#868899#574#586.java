    public static void copyTo(File inFile, File outFile) throws IOException {
        char[] cbuff = new char[32768];
        BufferedReader reader = new BufferedReader(new FileReader(inFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
        int readedBytes = 0;
        long absWrittenBytes = 0;
        while ((readedBytes = reader.read(cbuff, 0, cbuff.length)) != -1) {
            writer.write(cbuff, 0, readedBytes);
            absWrittenBytes += readedBytes;
        }
        reader.close();
        writer.close();
    }
