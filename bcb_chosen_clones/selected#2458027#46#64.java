    public static void openDefaultPDFViewer(String path) {
        if (Desktop.isDesktopSupported()) {
            File file = new File(path);
            if (file.exists()) {
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    logger.log(Level.SEVERE, LanguageResource.getLanguage().getString("error.no_pdf_viewer_installed"), e);
                    LoggingDesktopController.printError(LanguageResource.getLanguage().getString("error.no_pdf_viewer_installed"));
                }
            } else {
                logger.severe(LanguageResource.getLanguage().getString("error.no_file.exists"));
                LoggingDesktopController.printError(LanguageResource.getLanguage().getString("error.no_file.exists"));
            }
        } else {
            logger.severe(LanguageResource.getLanguage().getString("error.no_desktop_support"));
            LoggingDesktopController.printError(LanguageResource.getLanguage().getString("error.no_desktop_support"));
        }
    }
