    public void testGetInputStream() throws Exception {
        ByteArrayOutputStream desireOut = new ByteArrayOutputStream();
        ByteArrayOutputStream adapterOut = new ByteArrayOutputStream();
        File f = new File(getWorkDir() + File.separator + FILE_TEST_1_TAR_GZ);
        assertNotNull(m_adapter);
        InputStream in = null;
        byte[] buff = new byte[512];
        try {
            in = new FileInputStream(f);
            int len = in.read(buff);
            while (len != -1) {
                desireOut.write(buff, 0, len);
                len = in.read(buff);
            }
            in.close();
            in = null;
            in = new FileInputStream(f);
            len = in.read(buff);
            while (len != -1) {
                adapterOut.write(buff, 0, len);
                len = in.read(buff);
            }
            in.close();
            in = null;
            byte[] desireBytes = desireOut.toByteArray();
            byte[] adapterBytes = adapterOut.toByteArray();
            assertEquals(desireBytes.length, adapterBytes.length);
            for (int i = 0; i < desireBytes.length; i++) {
                assertEquals("Byte[" + i + "]", desireBytes[i], adapterBytes[i]);
            }
        } finally {
            if (in != null) in.close();
        }
    }
