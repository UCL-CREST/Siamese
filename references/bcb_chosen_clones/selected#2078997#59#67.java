    @Test
    public void testCopy_inputStreamToOutputStream_nullOut() throws Exception {
        InputStream in = new ByteArrayInputStream(inData);
        try {
            IOUtils.copy(in, (OutputStream) null);
            fail();
        } catch (NullPointerException ex) {
        }
    }
