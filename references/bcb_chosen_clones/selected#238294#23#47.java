    public static void open(File file) {
        try {
            if (Desktop.isDesktopSupported() && (SecurityManager.isFileLaunchable(file.getName()) || file.isDirectory())) {
                logger.info("Launching file: " + file.getAbsolutePath());
                if (isJDIC) {
                    try {
                        org.jdesktop.jdic.desktop.Desktop.open(file);
                        isJDIC = true;
                        logger.info("File Opened Successfully using JDIC: " + file.getAbsolutePath());
                    } catch (Throwable e) {
                        logger.warn("JDIC not available, will try ti use JDK's Desktop: " + e);
                        isJDIC = false;
                    }
                }
                if (!isJDIC) {
                    Desktop.getDesktop().open(file);
                    logger.info("File Opened Successfully using JDK: " + file.getAbsolutePath());
                }
            } else {
                logger.warn("Launching File: " + file.getName() + "is disabled");
            }
        } catch (IOException e) {
            logger.fatal("Err in opening the file: " + e, e);
        }
    }
