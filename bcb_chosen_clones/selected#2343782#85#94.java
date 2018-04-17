    @org.junit.Test
    public void testReadWrite() throws Exception {
        final byte[] testBytes = "testString".getBytes();
        final InputStream istream = new ByteArrayInputStream(testBytes);
        final ByteArrayOutputStream destination = new ByteArrayOutputStream();
        final InputStream teeStream = new TeeInputStreamOutputStream(istream, destination);
        IOUtils.copy(teeStream, new NullOutputStream());
        teeStream.close();
        assertArrayEquals("array are equals", testBytes, destination.toByteArray());
    }
