    public static void openWithSpecificApp(java.io.File temp) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                if (!temp.exists()) logger.error("There was an error downloading as temporary file"); else {
                    desktop.open(temp);
                }
            } catch (IOException e1) {
                if (e1.getMessage().contains("The parameter is incorrect")) {
                    try {
                        Runtime.getRuntime().exec("rundll32 shell32,OpenAs_RunDLL " + temp);
                    } catch (IOException e) {
                        logger.error(e.getMessage());
                    }
                    logger.error(e1.getMessage());
                }
            }
        }
    }
