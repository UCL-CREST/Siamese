    public void testFullReadAllFour() throws IOException {
        GZIPMembersInputStream gzin = new GZIPMembersInputStream(new ByteArrayInputStream(allfour_gz));
        int count = IOUtils.copy(gzin, new NullOutputStream());
        assertEquals("wrong length uncompressed data", 1024 + (32 * 1024) + 1 + 5, count);
    }
