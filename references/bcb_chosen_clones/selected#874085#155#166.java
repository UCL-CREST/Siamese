    private CharBuffer decodeToFile(ReplayInputStream inStream, String backingFilename, String encoding) throws IOException {
        CharBuffer charBuffer = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, encoding));
        File backingFile = new File(backingFilename);
        this.decodedFile = File.createTempFile(backingFile.getName(), WRITE_ENCODING, backingFile.getParentFile());
        FileOutputStream fos;
        fos = new FileOutputStream(this.decodedFile);
        IOUtils.copy(reader, fos, WRITE_ENCODING);
        fos.close();
        charBuffer = getReadOnlyMemoryMappedBuffer(this.decodedFile).asCharBuffer();
        return charBuffer;
    }
