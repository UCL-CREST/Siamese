    public static void copyFiles(File src, File dest, String... patterns) {
        if (src == null || !src.exists()) {
            LOGGER.warn("Source file/directory does not exist : " + src);
            return;
        } else if (!src.canRead()) {
            LOGGER.warn("Source file/directory not readable : " + src);
            return;
        }
        Pattern pattern = getPattern(patterns);
        if (pattern.matcher(src.getAbsolutePath()).matches()) {
            LOGGER.info("Not copying file : " + src);
            return;
        }
        if (src.isDirectory()) {
            if (!dest.exists()) {
                if (!dest.mkdirs()) {
                    LOGGER.warn("Could not create the new destination directory : " + dest);
                } else {
                    LOGGER.info("Created directory : " + dest);
                }
            }
            String children[] = src.list();
            for (int i = 0; i < children.length; i++) {
                File childSrc = new File(src, children[i]);
                File childDest = new File(dest, children[i]);
                copyFiles(childSrc, childDest, patterns);
            }
        } else {
            copyFile(src, dest);
        }
    }
