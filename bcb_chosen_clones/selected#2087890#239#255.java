    public static void copyFiles(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            dest.mkdirs();
            for (String f : src.list()) {
                String df = dest.getPath() + File.separator + f;
                String sf = src.getPath() + File.separator + f;
                copyFiles(new File(sf), new File(df));
            }
        } else {
            FileInputStream fin = new FileInputStream(src);
            FileOutputStream fout = new FileOutputStream(dest);
            int c;
            while ((c = fin.read()) >= 0) fout.write(c);
            fin.close();
            fout.close();
        }
    }
