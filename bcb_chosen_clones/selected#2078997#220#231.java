    @Test
    public void testCopy_readerToOutputStream_Encoding() throws Exception {
        InputStream in = new ByteArrayInputStream(inData);
        in = new YellOnCloseInputStreamTest(in);
        Reader reader = new InputStreamReader(in, "US-ASCII");
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        OutputStream out = new YellOnFlushAndCloseOutputStreamTest(baout, false, true);
        IOUtils.copy(reader, out, "UTF16");
        byte[] bytes = baout.toByteArray();
        bytes = new String(bytes, "UTF16").getBytes("US-ASCII");
        assertTrue("Content differs", Arrays.equals(inData, bytes));
    }
