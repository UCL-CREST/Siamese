    @Test
    public void testCopy_inputStreamToWriter_Encoding() throws Exception {
        InputStream in = new ByteArrayInputStream(inData);
        in = new YellOnCloseInputStreamTest(in);
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        YellOnFlushAndCloseOutputStreamTest out = new YellOnFlushAndCloseOutputStreamTest(baout, true, true);
        Writer writer = new OutputStreamWriter(baout, "US-ASCII");
        IOUtils.copy(in, writer, "UTF8");
        out.off();
        writer.flush();
        assertTrue("Not all bytes were read", in.available() == 0);
        byte[] bytes = baout.toByteArray();
        bytes = new String(bytes, "UTF8").getBytes("US-ASCII");
        assertTrue("Content differs", Arrays.equals(inData, bytes));
    }
