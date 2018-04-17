    public static final boolean copy(File source, File target, boolean overwrite) {
        if (!overwrite && target.exists()) {
            LOGGER.error("Target file exist and it not permitted to overwrite it !");
            return false;
        }
        FileChannel in = null;
        FileChannel out = null;
        try {
            in = new FileInputStream(source).getChannel();
            out = new FileOutputStream(target).getChannel();
            in.transferTo(0, in.size(), out);
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getLocalizedMessage());
            if (LOGGER.isDebugEnabled()) e.printStackTrace();
            return false;
        } catch (IOException e) {
            LOGGER.error(e.getLocalizedMessage());
            if (LOGGER.isDebugEnabled()) e.printStackTrace();
            return false;
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
            try {
                out.close();
            } catch (Exception e) {
            }
        }
        return true;
    }
