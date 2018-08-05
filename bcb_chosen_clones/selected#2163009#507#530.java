    protected static void copyFile(File in, File out) throws IOException {
        java.io.FileWriter filewriter = null;
        java.io.FileReader filereader = null;
        try {
            filewriter = new java.io.FileWriter(out);
            filereader = new java.io.FileReader(in);
            char[] buf = new char[4096];
            int nread = filereader.read(buf, 0, 4096);
            while (nread >= 0) {
                filewriter.write(buf, 0, nread);
                nread = filereader.read(buf, 0, 4096);
            }
            buf = null;
        } finally {
            try {
                filereader.close();
            } catch (Throwable t) {
            }
            try {
                filewriter.close();
            } catch (Throwable t) {
            }
        }
    }
