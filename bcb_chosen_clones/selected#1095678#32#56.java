    public static final byte[] loadFile(File file) throws IOException {
        if (file == null) {
            throw new IOException("The given file must not be null.");
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream is = null;
        try {
            is = new BufferedInputStream(fis, IO_BUFFER_SIZE);
            byte[] buffer = new byte[IO_BUFFER_SIZE];
            int numRead = is.read(buffer, 0, IO_BUFFER_SIZE);
            while (numRead > 0) {
                bos.write(buffer, 0, numRead);
                numRead = is.read(buffer, 0, IO_BUFFER_SIZE);
            }
        } finally {
            if (is != null) {
                is.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bos.toByteArray();
    }
