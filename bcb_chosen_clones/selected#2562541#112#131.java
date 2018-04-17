    public static void extract(final File destDir, final Collection<ZipEntryInfo> entryInfos) throws IOException {
        if (destDir == null || CollectionUtils.isEmpty(entryInfos)) throw new IllegalArgumentException("One or parameter is null or empty!");
        if (!destDir.exists()) destDir.mkdirs();
        for (ZipEntryInfo entryInfo : entryInfos) {
            ZipEntry entry = entryInfo.getZipEntry();
            InputStream in = entryInfo.getInputStream();
            File entryDest = new File(destDir, entry.getName());
            entryDest.getParentFile().mkdirs();
            if (!entry.isDirectory()) {
                OutputStream out = new FileOutputStream(new File(destDir, entry.getName()));
                try {
                    IOUtils.copy(in, out);
                    out.flush();
                } finally {
                    IOUtils.closeQuietly(in);
                    IOUtils.closeQuietly(out);
                }
            }
        }
    }
