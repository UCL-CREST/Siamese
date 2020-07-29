    public static void copy(File src, File dest, boolean overwrite) throws IOException {
        if (!src.exists()) throw new IOException("File source does not exists");
        if (dest.exists()) {
            if (!overwrite) throw new IOException("File destination already exists");
            dest.delete();
        } else {
            dest.createNewFile();
        }
        InputStream is = new FileInputStream(src);
        OutputStream os = new FileOutputStream(dest);
        byte[] buffer = new byte[1024 * 4];
        int len = 0;
        while ((len = is.read(buffer)) > 0) {
            os.write(buffer, 0, len);
        }
        os.close();
        is.close();
    }
