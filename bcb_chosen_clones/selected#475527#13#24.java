    public static void copy(File src, File dest) throws FileNotFoundException, IOException {
        FileInputStream in = new FileInputStream(src);
        FileOutputStream out = new FileOutputStream(dest);
        try {
            byte[] buf = new byte[1024];
            int c = -1;
            while ((c = in.read(buf)) > 0) out.write(buf, 0, c);
        } finally {
            in.close();
            out.close();
        }
    }
