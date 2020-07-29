    public static void copyFiles(String strPath, String dstPath) throws IOException {
        File src = new File(strPath);
        File dest = new File(dstPath);
        if (src.isDirectory()) {
            dest.mkdirs();
            String list[] = src.list();
            for (int i = 0; i < list.length; i++) {
                String dest1 = dest.getAbsolutePath() + File.separatorChar + list[i];
                String src1 = src.getAbsolutePath() + File.separatorChar + list[i];
                copyFiles(src1, dest1);
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
