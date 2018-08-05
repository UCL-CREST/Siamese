    public void zipVisualizationFiles(final String outputfile) {
        final byte[] buf = new byte[100000];
        try {
            final String outFilename = outputfile;
            final ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
            for (int i = 0; i < filenames.size(); i++) {
                final FileInputStream in = new FileInputStream(filenames.get(i));
                out.putNextEntry(new ZipEntry(filenames.get(i)));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
