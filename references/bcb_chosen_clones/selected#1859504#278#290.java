    private void zipFileInternal(File fileToZip, String zipName) throws IOException {
        byte[] buf = new byte[2048];
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipName));
        FileInputStream in = new FileInputStream(fileToZip);
        out.putNextEntry(new ZipEntry(fileToZip.getName()));
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.closeEntry();
        in.close();
        out.close();
    }
