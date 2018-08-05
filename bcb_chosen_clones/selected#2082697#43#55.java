    public static void zipper(File fileIN, File fileOUT) throws IOException {
        byte[] buf = new byte[1024];
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(fileOUT));
        FileInputStream in = new FileInputStream(fileIN);
        out.putNextEntry(new ZipEntry(fileIN.getPath()));
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.closeEntry();
        in.close();
        out.close();
    }
