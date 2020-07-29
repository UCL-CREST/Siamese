    public static void zipFile(File file, ZipOutputStream out) throws Exception {
        byte[] buf = new byte[1024];
        out.putNextEntry(new ZipEntry(file.getName()));
        FileInputStream in = new FileInputStream(file);
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.closeEntry();
    }
