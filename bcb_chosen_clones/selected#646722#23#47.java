    public static File fileToZipFile(File file) throws Exception {
        ZipOutputStream out = null;
        FileInputStream in = null;
        File retFile = changeFileNameSuffix(file, "zip");
        try {
            byte[] buf = new byte[1024];
            out = new ZipOutputStream(new FileOutputStream(retFile));
            in = new FileInputStream(file);
            out.putNextEntry(new ZipEntry(file.getName()));
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
        file.delete();
        return retFile;
    }
