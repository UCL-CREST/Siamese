    public static void copyFile(File src, File dest, int bufSize, boolean force) throws IOException {
        if (dest.exists()) if (force) dest.delete(); else throw new IOException("Cannot overwrite existing file: " + dest.getName());
        byte[] buffer = new byte[bufSize];
        int read = 0;
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dest);
            while (true) {
                read = in.read(buffer);
                if (read == -1) break;
                out.write(buffer, 0, read);
            }
        } finally {
            if (in != null) try {
                in.close();
            } finally {
                if (out != null) out.close();
            }
        }
    }
