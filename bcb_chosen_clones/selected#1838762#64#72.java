    public static void copyTo(File src, File dest) throws IOException {
        if (src.equals(dest)) throw new IOException("copyTo src==dest file");
        FileOutputStream fout = new FileOutputStream(dest);
        InputStream in = new FileInputStream(src);
        IOUtils.copyTo(in, fout);
        fout.flush();
        fout.close();
        in.close();
    }
