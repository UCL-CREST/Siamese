    public void testFullReadSixSmall() throws IOException {
        GZIPMembersInputStream gzin = new GZIPMembersInputStream(new ByteArrayInputStream(sixsmall_gz));
        int count = IOUtils.copy(gzin, new NullOutputStream());
        assertEquals("wrong length uncompressed data", 1 + 5 + 1 + 5 + 1 + 5, count);
    }
