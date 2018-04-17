    public static void copyFile(File src, File dest) throws IOException, IllegalArgumentException {
        if (src.isDirectory()) throw new IllegalArgumentException("Source file is a directory");
        if (dest.isDirectory()) throw new IllegalArgumentException("Destination file is a directory");
        int bufferSize = 4 * 1024;
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dest);
        byte[] buffer = new byte[bufferSize];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) >= 0) out.write(buffer, 0, bytesRead);
        out.close();
        in.close();
    }
