    private void checkLocalFiles() throws Exception {
        File file;
        CRC32 crc;
        byte[] buffer = new byte[1024];
        int n;
        FileInputStream is;
        Assert.assertTrue(this.temp.listFiles().length > 0);
        for (TestOpenFile of : this.transactable.getPvr().getFiles().values()) {
            Assert.assertFalse(of.isOpen());
            file = new File(this.temp, this.LOCAL_NAME + of.getFile().getName().substring(this.recording.getName().length()));
            Assert.assertTrue(file.exists());
            Assert.assertEquals("Bad size for " + file, of.getFile().getSize(), file.length());
            crc = new CRC32();
            is = new FileInputStream(file);
            while ((n = is.read(buffer)) > 0) crc.update(buffer, 0, n);
            is.close();
            Assert.assertEquals(of.getCheckSum().getValue(), crc.getValue());
        }
    }
