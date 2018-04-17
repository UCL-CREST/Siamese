    public static void copy(File from, File to, int bufferSize) throws IOException {
        if (to.exists()) {
            logger.info("File " + to + " exists, will replace it.");
            to.delete();
        }
        to.getParentFile().mkdirs();
        to.createNewFile();
        FileInputStream ois = null;
        FileOutputStream cos = null;
        try {
            ois = new FileInputStream(from);
            cos = new FileOutputStream(to);
            byte[] buf = new byte[bufferSize];
            int read;
            while ((read = ois.read(buf, 0, bufferSize)) > 0) {
                cos.write(buf, 0, read);
            }
            cos.flush();
        } finally {
            try {
                if (ois != null) ois.close();
            } catch (IOException ignored) {
                logger.warn("Could not close file input stream " + from, ignored);
            }
            try {
                if (cos != null) {
                    cos.close();
                }
            } catch (IOException ignored) {
                logger.warn("Could not close file output stream " + to, ignored);
            }
        }
    }
