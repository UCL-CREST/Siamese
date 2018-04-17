    @Test
    public void testWriteAndRead() throws Exception {
        JCFS.configureLoopback(dir);
        RFile file = new RFile("testreadwrite.txt");
        RFileOutputStream out = new RFileOutputStream(file);
        out.write("test".getBytes("utf-8"));
        out.close();
        RFileInputStream in = new RFileInputStream(file);
        byte[] buffer = new byte[4];
        int readCount = in.read(buffer);
        in.close();
        assertEquals(4, readCount);
        String resultRead = new String(buffer, "utf-8");
        assertEquals("test", resultRead);
    }
