    public static void copyFile(File src, File dest, boolean force) throws IOException, InterruptedIOException {
        if (dest.exists()) {
            if (force) {
                dest.delete();
            } else {
                throw new IOException("Cannot overwrite existing file!");
            }
        }
        byte[] buffer = new byte[5 * 1024 * 1024];
        int read = 0;
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(src));
            out = new BufferedOutputStream(new FileOutputStream(dest));
            while (true) {
                read = in.read(buffer);
                if (read == -1) {
                    break;
                }
                out.write(buffer, 0, read);
            }
        } finally {
            buffer = null;
            if (in != null) {
                try {
                    in.close();
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
            }
        }
    }
