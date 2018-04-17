    private void addFile(File aFile) throws IOException {
        if (aFile.isDirectory()) {
            throw new IOException(aFile.getName() + " is a directory.");
        }
        String zipName = getZipName(aFile);
        ZipEntry ze = new ZipEntry(zipName);
        ze.setTime(aFile.lastModified());
        if (aFile.length() < 51) {
            mZos.setMethod(ZipOutputStream.STORED);
            ze.setSize(aFile.length());
            ze.setCompressedSize(aFile.length());
            ze.setCrc(calcCRC32(aFile));
        } else {
            mZos.setMethod(ZipOutputStream.DEFLATED);
        }
        mZos.putNextEntry(ze);
        byte[] buf = new byte[2048];
        int read;
        InputStream is = new FileInputStream(aFile);
        while ((read = is.read(buf, 0, buf.length)) > -1) {
            mZos.write(buf, 0, read);
        }
        is.close();
    }
