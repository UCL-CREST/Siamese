    public void overwriteTest() throws Exception {
        SRBAccount srbAccount = new SRBAccount("srb1.ngs.rl.ac.uk", 5544, this.cred);
        srbAccount.setDefaultStorageResource("ral-ngs1");
        SRBFileSystem client = new SRBFileSystem(srbAccount);
        client.setFirewallPorts(64000, 65000);
        String home = client.getHomeDirectory();
        System.out.println("home: " + home);
        SRBFile file = new SRBFile(client, home + "/test.txt");
        assertTrue(file.exists());
        File filefrom = new File("/tmp/from.txt");
        assertTrue(filefrom.exists());
        SRBFileOutputStream to = null;
        InputStream from = null;
        try {
            to = new SRBFileOutputStream((SRBFile) file);
            from = new FileInputStream(filefrom);
            byte[] buffer = new byte[4096];
            int bytes_read;
            while ((bytes_read = from.read(buffer)) != -1) {
                to.write(buffer, 0, bytes_read);
            }
            to.flush();
        } finally {
            try {
                if (to != null) {
                    to.close();
                }
            } catch (Exception ex) {
            }
            try {
                if (from != null) {
                    from.close();
                }
            } catch (Exception ex) {
            }
        }
    }
