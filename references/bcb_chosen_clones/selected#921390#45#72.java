    public static void fileCopy(File src, File dst) throws FileNotFoundException, IOException {
        if (src.isDirectory() && (!dst.exists() || dst.isDirectory())) {
            if (!dst.exists()) {
                if (!dst.mkdirs()) throw new IOException("unable to mkdir " + dst);
            }
            File dst1 = new File(dst, src.getName());
            if (!dst1.exists() && !dst1.mkdir()) throw new IOException("unable to mkdir " + dst1);
            dst = dst1;
            File[] files = src.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    dst1 = new File(dst, f.getName());
                    if (!dst1.exists() && !dst1.mkdir()) throw new IOException("unable to mkdir " + dst1);
                } else {
                    dst1 = dst;
                }
                fileCopy(f, dst1);
            }
            return;
        } else if (dst.isDirectory()) {
            dst = new File(dst, src.getName());
        }
        FileChannel ic = new FileInputStream(src).getChannel();
        FileChannel oc = new FileOutputStream(dst).getChannel();
        ic.transferTo(0, ic.size(), oc);
        ic.close();
        oc.close();
    }
