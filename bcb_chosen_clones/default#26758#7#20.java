    static void copy(String src, String dest) throws IOException {
        File ifp = new File(src);
        File ofp = new File(dest);
        if (ifp.exists() == false) {
            throw new IOException("file '" + src + "' does not exist");
        }
        FileInputStream fis = new FileInputStream(ifp);
        FileOutputStream fos = new FileOutputStream(ofp);
        byte[] b = new byte[1024];
        int readBytes;
        while ((readBytes = fis.read(b)) > 0) fos.write(b, 0, readBytes);
        fis.close();
        fos.close();
    }
