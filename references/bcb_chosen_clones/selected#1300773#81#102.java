    public void gzip(File from, File to) {
        OutputStream out_zip = null;
        ArchiveOutputStream os = null;
        try {
            try {
                out_zip = new FileOutputStream(to);
                os = new ArchiveStreamFactory().createArchiveOutputStream("zip", out_zip);
                os.putArchiveEntry(new ZipArchiveEntry(from.getName()));
                IOUtils.copy(new FileInputStream(from), os);
                os.closeArchiveEntry();
            } finally {
                if (os != null) {
                    os.close();
                }
            }
            out_zip.close();
        } catch (IOException ex) {
            fatal("IOException", ex);
        } catch (ArchiveException ex) {
            fatal("ArchiveException", ex);
        }
    }
