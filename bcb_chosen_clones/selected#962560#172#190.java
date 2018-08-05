    private static void doZip(ZipOutputStream out, File f, String base, FilenameFilter filter) throws Exception {
        if (f.isDirectory()) {
            File[] fl = f.listFiles(filter);
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fl.length; i++) {
                doZip(out, fl[i], base + fl[i].getName(), filter);
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream in = new FileInputStream(f);
            int b;
            System.out.println(base);
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            in.close();
        }
    }
