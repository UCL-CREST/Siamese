    private static void copy(File source, File target) throws IOException {
        FileInputStream in = new FileInputStream(source);
        FileOutputStream out = new FileOutputStream(target);
        byte[] buffer = new byte[4096];
        int no = 0;
        try {
            while ((no = in.read(buffer)) != -1) out.write(buffer, 0, no);
        } finally {
            in.close();
            out.close();
        }
    }
