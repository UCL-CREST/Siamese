    public static void main(String args[]) throws Exception {
        File file = new File("D:/work/love.txt");
        @SuppressWarnings("unused") ZipFile zipFile = new ZipFile("D:/work/test1.zip");
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream("D:/work/test1.zip"));
        zos.setEncoding("GBK");
        ZipEntry entry = null;
        if (file.isDirectory()) {
            entry = new ZipEntry(getAbsFileName(source, file) + "/");
        } else {
            entry = new ZipEntry(getAbsFileName(source, file));
        }
        entry.setSize(file.length());
        entry.setTime(file.lastModified());
        zos.putNextEntry(entry);
        int readLen = 0;
        byte[] buf = new byte[2048];
        if (file.isFile()) {
            InputStream in = new BufferedInputStream(new FileInputStream(file));
            while ((readLen = in.read(buf, 0, 2048)) != -1) {
                zos.write(buf, 0, readLen);
            }
            in.close();
        }
        zos.close();
    }
