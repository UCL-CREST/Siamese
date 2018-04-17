    public static void compress(final File zip, final Map<InputStream, String> entries) throws IOException {
        if (zip == null || entries == null || CollectionUtils.isEmpty(entries.keySet())) throw new IllegalArgumentException("One ore more parameters are empty!");
        if (zip.exists()) zip.delete(); else if (!zip.getParentFile().exists()) zip.getParentFile().mkdirs();
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zip)));
        out.setLevel(Deflater.BEST_COMPRESSION);
        InputStream in = null;
        try {
            for (InputStream inputStream : entries.keySet()) {
                in = inputStream;
                ZipEntry zipEntry = new ZipEntry(skipBeginningSlash(entries.get(in)));
                out.putNextEntry(zipEntry);
                IOUtils.copy(in, out);
                out.closeEntry();
                in.close();
            }
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }
