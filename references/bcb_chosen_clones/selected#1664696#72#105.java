    @Test
    public void testWriteAndReadBiggerUnbuffered() throws Exception {
        JCFSFileServer server = new JCFSFileServer(defaultTcpPort, defaultTcpAddress, defaultUdpPort, defaultUdpAddress, dir, 0, 0);
        JCFS.configureDiscovery(defaultUdpAddress, defaultUdpPort);
        try {
            server.start();
            RFile file = new RFile("testreadwriteb.txt");
            RFileOutputStream out = new RFileOutputStream(file);
            String body = "";
            int size = 50 * 1024;
            for (int i = 0; i < size; i++) {
                body = body + "a";
            }
            out.write(body.getBytes("utf-8"));
            out.close();
            File expected = new File(dir, "testreadwriteb.txt");
            assertTrue(expected.isFile());
            assertEquals(body.length(), expected.length());
            RFileInputStream in = new RFileInputStream(file);
            ByteArrayOutputStream tmp = new ByteArrayOutputStream();
            int b = in.read();
            while (b != -1) {
                tmp.write(b);
                b = in.read();
            }
            byte[] buffer = tmp.toByteArray();
            in.close();
            assertEquals(body.length(), buffer.length);
            String resultRead = new String(buffer, "utf-8");
            assertEquals(body, resultRead);
        } finally {
            server.stop();
        }
    }
