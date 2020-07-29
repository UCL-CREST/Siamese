    private void checkRoundtrip(byte[] content) throws Exception {
        InputStream in = new ByteArrayInputStream(content);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CodecUtil.encodeQuotedPrintableBinary(in, out);
        in = new QuotedPrintableInputStream(new ByteArrayInputStream(out.toByteArray()));
        out = new ByteArrayOutputStream();
        IOUtils.copy(in, out);
        assertEquals(content, out.toByteArray());
    }
