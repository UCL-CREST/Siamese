    public static void decompressGZIP(File gzip, File to, long skip) throws IOException {
        GZIPInputStream gis = null;
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(to));
            FileInputStream fis = new FileInputStream(gzip);
            fis.skip(skip);
            gis = new GZIPInputStream(fis);
            final byte[] buffer = new byte[IO_BUFFER];
            int read = -1;
            while ((read = gis.read(buffer)) != -1) {
                bos.write(buffer, 0, read);
            }
        } finally {
            try {
                gis.close();
            } catch (Exception nope) {
            }
            try {
                bos.flush();
            } catch (Exception nope) {
            }
            try {
                bos.close();
            } catch (Exception nope) {
            }
        }
    }
