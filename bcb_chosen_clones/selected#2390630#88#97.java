    @org.junit.Test
    public void testReadWrite() throws Exception {
        final String reference = "testString";
        final Reader reader = new StringReader(reference);
        final StringWriter osString = new StringWriter();
        final Reader teeStream = new TeeReaderWriter(reader, osString);
        IOUtils.copy(teeStream, new NullWriter());
        teeStream.close();
        osString.toString();
    }
