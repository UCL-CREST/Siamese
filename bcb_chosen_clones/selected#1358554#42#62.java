    public static File createZip(File tmpDir, String[] filenames, String zipName, String text) throws IOException {
        File zip = new File(tmpDir, zipName);
        byte[] buf = new byte[1024];
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));
        for (int i = 0; i < filenames.length; i++) {
            out.putNextEntry(new ZipEntry(filenames[i]));
            if (filenames[i].endsWith("/")) continue;
            if (text == null) {
                text = "this is file " + filenames[i];
            }
            InputStream in = new ByteArrayInputStream(text.getBytes());
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
            in.close();
        }
        out.close();
        return zip;
    }
