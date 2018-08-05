    public static void copyFileTo(String src, String dest) throws FileNotFoundException, IOException {
        File destFile = new File(dest);
        InputStream in = new FileInputStream(new File(src));
        OutputStream out = new FileOutputStream(destFile);
        byte buf[] = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
        in.close();
        out.close();
    }
