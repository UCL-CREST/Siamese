    public static byte[] decodeFromFile(final String fileName) throws IOException {
        CDebug.checkParameterNotEmpty(fileName, "fileName");
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        ByteArrayOutputStream encoded = null;
        try {
            final File file = new File(fileName);
            encoded = new ByteArrayOutputStream((int) file.length());
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            final byte[] input = new byte[10 * 10];
            int readBytes = bufferedInputStream.read(input);
            while (readBytes != -1) {
                encoded.write(input, 0, readBytes);
                readBytes = bufferedInputStream.read(input);
            }
            return decode(encoded.toByteArray());
        } finally {
            InputOutputUtil.close(bufferedInputStream, fileInputStream, encoded);
        }
    }
