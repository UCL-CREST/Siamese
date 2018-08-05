    protected void copyFile(File from, File to) throws IOException {
        to.getParentFile().mkdirs();
        InputStream in = new FileInputStream(from);
        try {
            OutputStream out = new FileOutputStream(to);
            try {
                byte[] buf = new byte[1024];
                int readLength;
                while ((readLength = in.read(buf)) > 0) {
                    out.write(buf, 0, readLength);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }
