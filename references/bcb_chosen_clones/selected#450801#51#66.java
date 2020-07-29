    private static void processFile(StreamDriver driver, String sourceName) throws Exception {
        String destName = sourceName + ".xml";
        File dest = new File(destName);
        if (dest.exists()) {
            throw new IllegalArgumentException("File '" + destName + "' already exists!");
        }
        FileChannel sourceChannel = new FileInputStream(sourceName).getChannel();
        try {
            MappedByteBuffer sourceByteBuffer = sourceChannel.map(FileChannel.MapMode.READ_ONLY, 0, sourceChannel.size());
            CharsetDecoder decoder = Charset.forName("ISO-8859-15").newDecoder();
            CharBuffer sourceBuffer = decoder.decode(sourceByteBuffer);
            driver.generateXmlDocument(sourceBuffer, new FileOutputStream(dest));
        } finally {
            sourceChannel.close();
        }
    }
