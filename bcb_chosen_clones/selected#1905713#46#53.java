    @org.junit.Test
    public void simpleRead() throws Exception {
        final InputStream istream = StatsInputStreamTest.class.getResourceAsStream("/testFile.txt");
        final StatsInputStream ris = new StatsInputStream(istream);
        assertEquals("read size", 0, ris.getSize());
        IOUtils.copy(ris, new NullOutputStream());
        assertEquals("in the end", 30, ris.getSize());
    }
