    public static int save(File inputFile, File outputFile) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(inputFile);
            outputFile.getParentFile().mkdirs();
            out = new FileOutputStream(outputFile);
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            return IOUtils.copy(in, out);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ioe) {
                ioe.getMessage();
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ioe) {
                ioe.getMessage();
            }
        }
    }
