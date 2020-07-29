    @Test
    public void testWriteAndReadFirstLevel() throws Exception {
        JCFSFileServer server = new JCFSFileServer(defaultTcpPort, defaultTcpAddress, defaultUdpPort, defaultUdpAddress, dir, 0, 0);
        JCFS.configureDiscovery(defaultUdpAddress, defaultUdpPort);
        try {
            server.start();
            RFile directory1 = new RFile("directory1");
            RFile file = new RFile(directory1, "testreadwrite1st.txt");
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
        } finally {
            server.stop();
        }
    }
