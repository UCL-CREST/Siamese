    public void addFile(File file, ZipOutputStream zos) throws IOException {
        if (Thread.currentThread().isInterrupted()) return;
        compressStarted(file);
        String enname = file.getAbsolutePath().substring(archiveRoot.length() + 1);
        ZipEntry en = new ZipEntry(enname);
        CRC32 crc32 = new CRC32();
        byte[] chs = new byte[1024];
        FileInputStream fis = new FileInputStream(file);
        int len = 0;
        while ((len = fis.read(chs)) > -1) crc32.update(chs, 0, len);
        fis.close();
        en.setSize(file.length());
        en.setTime(file.lastModified());
        en.setCrc(crc32.getValue());
        zos.putNextEntry(en);
        fis = new FileInputStream(file);
        while ((len = fis.read(chs)) > -1) zos.write(chs, 0, len);
        fis.close();
        zos.closeEntry();
        compressComplete(file);
    }
