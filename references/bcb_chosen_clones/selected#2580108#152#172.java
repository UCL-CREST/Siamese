    public static byte[] encodeFromFile(final String fileName, final int lineSize) throws IOException {
        CDebug.checkParameterNotEmpty(fileName, "fileName");
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        ByteArrayOutputStream unencoded = null;
        try {
            final File file = new File(fileName);
            unencoded = new ByteArrayOutputStream((int) file.length());
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            final byte[] input = new byte[10 * 10];
            int readBytes = bufferedInputStream.read(input);
            while (readBytes != -1) {
                unencoded.write(input, 0, readBytes);
                readBytes = bufferedInputStream.read(input);
            }
            return encode(unencoded.toByteArray(), lineSize);
        } finally {
            InputOutputUtil.close(bufferedInputStream, fileInputStream, unencoded);
        }
    }
