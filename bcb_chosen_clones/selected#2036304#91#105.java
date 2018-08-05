    protected static void copyDeleting(File source, File dest) throws IOException {
        byte[] buf = new byte[8 * 1024];
        FileInputStream in = new FileInputStream(source);
        try {
            FileOutputStream out = new FileOutputStream(dest);
            try {
                int count;
                while ((count = in.read(buf)) >= 0) out.write(buf, 0, count);
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }
