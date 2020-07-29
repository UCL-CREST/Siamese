    public byte[] getRawParameter(final String name) throws IOException {
        if (name == null) {
            return null;
        }
        final File tempFile = tempFileNames.get(name);
        if (tempFile == null) {
            return null;
        }
        final InputStream inFile = new FileInputStream(tempFile);
        final ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        final byte buffer[] = new byte[2048];
        int readBytes = inFile.read(buffer);
        while (readBytes != -1) {
            byteArray.write(buffer, 0, readBytes);
            readBytes = inFile.read(buffer);
        }
        inFile.close();
        final byte output[] = byteArray.toByteArray();
        byteArray.close();
        return output;
    }
