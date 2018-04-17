    public static void copyFile(File in, File out) {
        int len;
        byte[] buffer = new byte[1024];
        try {
            FileInputStream fin = new FileInputStream(in);
            FileOutputStream fout = new FileOutputStream(out);
            while ((len = fin.read(buffer)) >= 0) fout.write(buffer, 0, len);
            fin.close();
            fout.close();
        } catch (IOException ex) {
        }
    }
