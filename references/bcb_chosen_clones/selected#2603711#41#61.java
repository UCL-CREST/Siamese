    private void zip(ZipOutputStream out, File f, String base) throws Exception {
        if (f.isDirectory()) {
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            File[] fl = f.listFiles();
            for (int i = 0; i < fl.length; i++) {
                zip(out, fl[i], base + fl[i].getName());
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            byte[] buf = new byte[1024];
            int readLen = 0;
            FileInputStream in = new FileInputStream(f);
            BufferedInputStream is = new BufferedInputStream(in);
            while ((readLen = is.read(buf, 0, 1024)) != -1) {
                out.write(buf, 0, readLen);
            }
            is.close();
            in.close();
        }
    }
