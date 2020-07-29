    public static final void zip(final ZipOutputStream out, final File f, String base) throws Exception {
        if (f.isDirectory()) {
            final File[] fl = f.listFiles();
            base = base.length() == 0 ? "" : base + File.separator;
            for (final File element : fl) {
                zip(out, element, base + element.getName());
            }
        } else {
            out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
            final FileInputStream in = new FileInputStream(f);
            IOUtils.copyStream(in, out);
            in.close();
        }
        Thread.sleep(10);
    }
