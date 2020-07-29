    private byte[] readFile(File file) throws IOException {
        File gzFile;
        InputStream in;
        if (file.exists()) {
            in = new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE);
        } else {
            gzFile = new File(file.getPath() + ".gz");
            if (!gzFile.exists()) {
                gzFile = new File(file.getPath() + ".dz");
            }
            if (gzFile.exists()) {
                in = new GZIPInputStream(new BufferedInputStream(new FileInputStream(gzFile), BUFFER_SIZE));
            } else {
                throw new FileNotFoundException(file.getPath());
            }
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
        try {
            LFileCopy.copy(in, out);
        } finally {
            in.close();
        }
        return out.toByteArray();
    }
