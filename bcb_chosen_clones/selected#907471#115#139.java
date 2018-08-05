    protected void addFileEntry(final ZipOutputStream out, final String entryName, final String filename) throws IOException {
        BufferedInputStream origin = null;
        try {
            final byte data[] = new byte[BUFFER];
            System.out.println("Adding: " + entryName);
            final FileInputStream fi = new FileInputStream(filename);
            origin = new BufferedInputStream(fi, BUFFER);
            final ZipEntry entry = new ZipEntry(entryName);
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
        } catch (final IOException ioe) {
            ioe.printStackTrace();
            throw ioe;
        } finally {
            if (origin != null) {
                try {
                    origin.close();
                } catch (final IOException e) {
                }
            }
        }
    }
