    public void checkUtfReader(File testFile, String encoding) throws Exception {
        String nonUtfEncoding = encoding.toLowerCase();
        if (nonUtfEncoding.startsWith("utf") || nonUtfEncoding.endsWith("ascii")) {
            nonUtfEncoding = "invalid-encoding";
        } else {
            nonUtfEncoding = encoding;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(out, encoding);
        InputStream in = new FileInputStream(testFile);
        StreamUtilImpl.getInstance().transfer(in, out, false);
        writer.close();
        byte[] data = out.toByteArray();
        in = new FileInputStream(testFile);
        StringWriter stringWriter = new StringWriter();
        Reader reader = new InputStreamReader(in, encoding);
        String expectedData = StreamUtilImpl.getInstance().read(reader);
        in = new ByteArrayInputStream(data);
        EncodingDetectionReader utfReader = getEncodingUtil().createUtfDetectionReader(in, nonUtfEncoding);
        int len = expectedData.length();
        for (int i = 0; i < len; i++) {
            int c = utfReader.read();
            assertEquals((int) expectedData.charAt(i), c);
        }
        int c = utfReader.read();
        assertEquals(-1, c);
        assertEquals(encoding, utfReader.getEncoding());
        utfReader.close();
        in = new ByteArrayInputStream(data);
        utfReader = getEncodingUtil().createUtfDetectionReader(in, nonUtfEncoding);
        String dataString = StreamUtilImpl.getInstance().read(utfReader);
        assertEquals(expectedData, dataString);
    }
