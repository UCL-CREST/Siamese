    public static void goToUpdate() {
        if (NEW_VERSION_AVAILABLE && Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(UPDATE_URL));
            } catch (Exception exception) {
                WindowUtils.showError(null, exception.getMessage());
            }
        }
    }
