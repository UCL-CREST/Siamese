    public final byte[] setDataFromFile(String filename) throws IOException {
        final File file = new File(filename);
        final ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream((int) file.length());
        new StreamCopier(4096, true).copy(new FileInputStream(file), byteArrayStream);
        final byte[] defaultData = byteArrayStream.toByteArray();
        m_defaultData = defaultData;
        return defaultData;
    }
