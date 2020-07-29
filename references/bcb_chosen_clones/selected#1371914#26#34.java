    public static boolean copy(InputStream is, File file) {
        try {
            IOUtils.copy(is, new FileOutputStream(file));
            return true;
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return false;
        }
    }
