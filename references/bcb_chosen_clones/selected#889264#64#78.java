    public static void zip(ZipOutputStream out, File f, String base) throws Exception {
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            base = base.length() == 0 ? "" : base + File.separator;
            for (int i = 0; i < fl.length; i++) {
                zip(out, fl[i], base + fl[i].getName());
            }
        } else {
            out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
            FileInputStream in = new FileInputStream(f);
            IOUtils.copyStream(in, out);
            in.close();
        }
        Thread.sleep(10);
    }
