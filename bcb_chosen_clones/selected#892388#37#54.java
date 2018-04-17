    private static final void createZip(File f, ZipOutputStream out) throws IOException {
        if (f.isFile()) {
            FileInputStream in = new FileInputStream(f);
            out.putNextEntry(new ZipEntry(f.getAbsolutePath().substring(1)));
            byte[] buf = new byte[2048];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
            in.close();
        } else if (f.isDirectory()) {
            File fs[] = f.listFiles();
            for (int i = 0; i < fs.length; i++) {
                createZip(fs[i], out);
            }
        }
    }
