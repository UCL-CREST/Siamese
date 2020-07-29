    public static void copy(File srcFile, File destFile) throws IOException {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            final byte[] buf = new byte[4096];
            int read;
            while ((read = in.read(buf)) >= 0) {
                out.write(buf, 0, read);
            }
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException ioe) {
            }
            try {
                if (out != null) out.close();
            } catch (IOException ioe) {
            }
        }
    }
