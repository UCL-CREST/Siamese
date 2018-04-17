    public static boolean decodeFileToFile(final String infile, final String outfile) {
        boolean success = false;
        java.io.InputStream in = null;
        java.io.OutputStream out = null;
        try {
            in = new Base64.InputStream(new java.io.BufferedInputStream(new java.io.FileInputStream(infile)), Base64.DECODE);
            out = new java.io.BufferedOutputStream(new java.io.FileOutputStream(outfile));
            final byte[] buffer = new byte[65536];
            int read = -1;
            while ((read = in.read(buffer)) >= 0) {
                out.write(buffer, 0, read);
            }
            success = true;
        } catch (final java.io.IOException exc) {
            exc.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (final Exception exc) {
            }
            try {
                out.close();
            } catch (final Exception exc) {
            }
        }
        return success;
    }
