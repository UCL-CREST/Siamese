    public static void zip(File srcDir, File destFile, FileFilter filter) throws IOException {
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(destFile));
            Collection<File> files = FileUtils.listFiles(srcDir, TrueFileFilter.TRUE, TrueFileFilter.TRUE);
            for (File f : files) {
                if (filter == null || filter.accept(f)) {
                    FileInputStream in = FileUtils.openInputStream(f);
                    out.putNextEntry(new ZipEntry(Util.relativePath(srcDir, f).replace('\\', '/')));
                    IOUtils.copyLarge(in, out);
                    out.closeEntry();
                    IOUtils.closeQuietly(in);
                }
            }
            IOUtils.closeQuietly(out);
        } catch (Throwable t) {
            throw new IOException("Failed to create zip file", t);
        } finally {
            if (out != null) {
                out.flush();
                IOUtils.closeQuietly(out);
            }
        }
    }
