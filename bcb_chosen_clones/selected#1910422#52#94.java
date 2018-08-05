    public void zipFile(String baseDir, String fileName, boolean encrypt) throws Exception {
        List fileList = getSubFiles(new File(baseDir));
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(fileName + ".temp"));
        ZipEntry ze = null;
        byte[] buf = new byte[BUFFER];
        byte[] encrypByte = new byte[encrypLength];
        int readLen = 0;
        for (int i = 0; i < fileList.size(); i++) {
            if (stopZipFile) {
                zos.close();
                File zipFile = new File(fileName + ".temp");
                if (zipFile.exists()) zipFile.delete();
                break;
            }
            File f = (File) fileList.get(i);
            if (f.getAbsoluteFile().equals(fileName + ".temp")) continue;
            ze = new ZipEntry(getAbsFileName(baseDir, f));
            ze.setSize(f.length());
            ze.setTime(f.lastModified());
            zos.putNextEntry(ze);
            InputStream is = new BufferedInputStream(new FileInputStream(f));
            readLen = is.read(buf, 0, BUFFER);
            if (encrypt) {
                if (readLen >= encrypLength) {
                    System.arraycopy(buf, 0, encrypByte, 0, encrypLength);
                } else if (readLen > 0) {
                    Arrays.fill(encrypByte, (byte) 0);
                    System.arraycopy(buf, 0, encrypByte, 0, readLen);
                    readLen = encrypLength;
                }
                byte[] temp = CryptionControl.getInstance().encryptoECB(encrypByte, rootKey);
                System.arraycopy(temp, 0, buf, 0, encrypLength);
            }
            while (readLen != -1) {
                zos.write(buf, 0, readLen);
                readLen = is.read(buf, 0, BUFFER);
            }
            is.close();
        }
        zos.close();
        File zipFile = new File(fileName + ".temp");
        if (zipFile.exists()) zipFile.renameTo(new File(fileName + ".zip"));
    }
