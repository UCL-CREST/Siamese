    @Test
    public void testCopy_inputStreamToOutputStream_nullIn() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        try {
            IOUtils.copy((InputStream) null, out);
            fail();
        } catch (NullPointerException ex) {
        }
    }
