    public void test3() throws Exception {
        BufferedImage img = getImage();
        TempFileDataBuffer buf = new TempFileDataBuffer();
        ZipOutputStream zipout = new ZipOutputStream(buf.getOutputStream());
        for (int i = 0; i < 100; i++) {
            ZipEntry entry = new ZipEntry("hoge" + i + ".bmp");
            zipout.putNextEntry(entry);
            ImageIO.write(img, "bmp", zipout);
        }
        zipout.flush();
        zipout.close();
        File tempFile = File.createTempFile("test", ".zip");
        FileOutputStream fout = new FileOutputStream(tempFile);
        ResourceUtil.copyStream(buf.getInputStream(), fout);
        fout.flush();
        fout.close();
        assertEquals(1409202, tempFile.length());
        tempFile.delete();
        buf.dispose();
    }
