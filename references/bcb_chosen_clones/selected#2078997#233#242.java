    @Test
    public void testCopy_readerToOutputStream_Encoding_nullIn() throws Exception {
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        OutputStream out = new YellOnFlushAndCloseOutputStreamTest(baout, true, true);
        try {
            IOUtils.copy((Reader) null, out, "UTF16");
            fail();
        } catch (NullPointerException ex) {
        }
    }
