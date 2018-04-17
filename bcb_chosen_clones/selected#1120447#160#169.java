    public void writeToFile(File file, File source) throws IOException {
        BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(file));
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(source));
        bin.skip(header.getHeaderEndingOffset());
        for (long i = 0; i < this.streamLength; i++) {
            bout.write(bin.read());
        }
        bin.close();
        bout.close();
    }
