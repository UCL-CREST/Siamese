    public static void copyFileOrDir(String in, String out) {
        for (String ex : exclude) {
            if (in.endsWith(ex)) return;
        }
        File fin = new File(in);
        if (fin.isFile()) {
            Util.copyFile(in, out);
        } else if (fin.isDirectory()) {
            new File(out).mkdirs();
            for (File file : fin.listFiles()) {
                copyFileOrDir(in + "/" + file.getName(), out + "/" + file.getName());
            }
        }
    }
