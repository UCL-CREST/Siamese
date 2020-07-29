    private static void zip(ZipOutputStream out, File file, String base) throws Exception {
        if (file.isDirectory()) {
            File[] fl = file.listFiles();
            base = base.length() == 0 ? "" : base + File.separator;
            for (int i = 0; i < fl.length; i++) {
                zip(out, fl[i], base + fl[i].getName());
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            log.debug("添加压缩文件：" + base);
            FileInputStream in = new FileInputStream(file);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            in.close();
        }
    }
