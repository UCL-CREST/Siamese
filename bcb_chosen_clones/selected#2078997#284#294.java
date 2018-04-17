    @Test
    public void testCopy_readerToWriter_nullIn() throws Exception {
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        OutputStream out = new YellOnFlushAndCloseOutputStreamTest(baout, true, true);
        Writer writer = new OutputStreamWriter(baout, "US-ASCII");
        try {
            IOUtils.copy((Reader) null, writer);
            fail();
        } catch (NullPointerException ex) {
        }
    }
