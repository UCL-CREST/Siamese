    public static int convertImage(InputStream is, OutputStream os, String command) throws IOException, InterruptedException {
        if (logger.isInfoEnabled()) {
            logger.info(command);
        }
        Process p = Runtime.getRuntime().exec(command);
        ByteArrayOutputStream errOut = new ByteArrayOutputStream();
        StreamGobbler errGobbler = new StreamGobbler(p.getErrorStream(), errOut, "Convert Thread (err gobbler): " + command);
        errGobbler.start();
        StreamGobbler outGobbler = new StreamGobbler(new BufferedInputStream(is), p.getOutputStream(), "Convert Thread (out gobbler): " + command);
        outGobbler.start();
        try {
            IOUtils.copy(p.getInputStream(), os);
            os.flush();
            if (p.waitFor() != 0) {
                logger.error("Unable to convert, stderr: " + new String(errOut.toByteArray(), "UTF-8"));
            }
            return p.exitValue();
        } finally {
            IOUtils.closeQuietly(os);
        }
    }
