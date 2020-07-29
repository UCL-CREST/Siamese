    public static void copyFile(File from, File to) throws Exception {
        if (!from.exists()) return;
        FileInputStream in = new FileInputStream(from);
        FileOutputStream out = new FileOutputStream(to);
        byte[] buffer = new byte[8192];
        int bytes_read;
        while (true) {
            bytes_read = in.read(buffer);
            if (bytes_read == -1) break;
            out.write(buffer, 0, bytes_read);
        }
        out.flush();
        out.close();
        in.close();
    }
