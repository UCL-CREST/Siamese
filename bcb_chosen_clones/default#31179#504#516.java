    static void conditionalCopyFile(File dst, File src) throws IOException {
        if (dst.equals(src)) return;
        if (!dst.isFile() || dst.lastModified() < src.lastModified()) {
            System.out.println("Copying " + src);
            InputStream is = new FileInputStream(src);
            OutputStream os = new FileOutputStream(dst);
            byte[] buf = new byte[8192];
            int len;
            while ((len = is.read(buf)) > 0) os.write(buf, 0, len);
            os.close();
            is.close();
        }
    }
