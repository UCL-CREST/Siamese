    @Test
    public void testCopy_inputStreamToWriter_nullOut() throws Exception {
        InputStream in = new ByteArrayInputStream(inData);
        try {
            IOUtils.copy(in, (Writer) null);
            fail();
        } catch (NullPointerException ex) {
        }
    }
