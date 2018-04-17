    private void writeGif(String filename, String outputFile) throws IOException {
        File file = new File(filename);
        InputStream in = new FileInputStream(file);
        FileOutputStream fout = new FileOutputStream(outputFile);
        int totalRead = 0;
        int readBytes = 0;
        int blockSize = 65000;
        long fileLen = file.length();
        byte b[] = new byte[blockSize];
        while ((long) totalRead < fileLen) {
            readBytes = in.read(b, 0, blockSize);
            totalRead += readBytes;
            fout.write(b, 0, readBytes);
        }
        in.close();
        fout.close();
    }
