    public static void extract(final File destDir, final ZipInfo zipInfo, final IProgressMonitor monitor) throws IOException {
        if (!destDir.exists()) destDir.mkdirs();
        for (String key : zipInfo.getEntryKeys()) {
            ZipEntry entry = zipInfo.getEntry(key);
            InputStream in = zipInfo.getInputStream(entry);
            File entryDest = new File(destDir, entry.getName());
            entryDest.getParentFile().mkdirs();
            if (!entry.isDirectory()) {
                OutputStream out = new FileOutputStream(new File(destDir, entry.getName()));
                try {
                    IOUtils.copy(in, out);
                    out.flush();
                    if (monitor != null) monitor.worked(1);
                } finally {
                    IOUtils.closeQuietly(in);
                    IOUtils.closeQuietly(out);
                }
            }
        }
        if (monitor != null) monitor.done();
    }
