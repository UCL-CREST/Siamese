    private static void zipDirctory(ZipOutputStream out, File file, String base) throws Exception {
        if (file.isDirectory()) {
            File[] fl = file.listFiles();
            if (!base.equals("")) out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fl.length; i++) {
                zipDirctory(out, fl[i], base + fl[i].getName());
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream in = new FileInputStream(file);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            in.close();
        }
    }
