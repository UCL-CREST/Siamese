    @Test
    public void testCopy_inputStreamToWriter_Encoding_nullOut() throws Exception {
        InputStream in = new ByteArrayInputStream(inData);
        try {
            IOUtils.copy(in, (Writer) null, "UTF8");
            fail();
        } catch (NullPointerException ex) {
        }
    }
