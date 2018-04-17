    private void writeFile(FileInputStream inFile, FileOutputStream outFile) throws IOException {
        byte[] buf = new byte[2048];
        int read;
        while ((read = inFile.read(buf)) > 0) outFile.write(buf, 0, read);
        inFile.close();
    }
