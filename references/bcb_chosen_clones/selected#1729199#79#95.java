    private static void zip(ZipArchiveOutputStream zos, File efile, String base) throws IOException {
        if (efile.isDirectory()) {
            File[] lf = efile.listFiles();
            base = base + File.separator + efile.getName();
            for (File file : lf) {
                zip(zos, file, base);
            }
        } else {
            ZipArchiveEntry entry = new ZipArchiveEntry(efile, base + File.separator + efile.getName());
            zos.setEncoding("utf-8");
            zos.putArchiveEntry(entry);
            InputStream is = new FileInputStream(efile);
            IOUtils.copy(is, zos);
            is.close();
            zos.closeArchiveEntry();
        }
    }
