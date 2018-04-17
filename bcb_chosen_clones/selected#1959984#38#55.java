    public static void compress(final File zip, final Map<InputStream, String> entries, final IProgressMonitor monitor) throws IOException {
        if (zip == null || entries == null || CollectionUtils.isEmpty(entries.keySet())) throw new IllegalArgumentException("One ore more parameters are empty!");
        if (zip.exists()) zip.delete(); else if (!zip.getParentFile().exists()) zip.getParentFile().mkdirs();
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zip)));
        out.setLevel(Deflater.BEST_COMPRESSION);
        try {
            for (InputStream inputStream : entries.keySet()) {
                ZipEntry zipEntry = new ZipEntry(skipBeginningSlash(entries.get(inputStream)));
                out.putNextEntry(zipEntry);
                IOUtils.copy(inputStream, out);
                out.closeEntry();
                inputStream.close();
                if (monitor != null) monitor.worked(1);
            }
        } finally {
            IOUtils.closeQuietly(out);
        }
    }
