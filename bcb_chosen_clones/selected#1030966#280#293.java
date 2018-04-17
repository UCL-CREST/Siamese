    private void helpActionPerformed() {
        boolean showMessage = !Desktop.isDesktopSupported();
        if (!showMessage) {
            try {
                Desktop.getDesktop().browse(new URI("http://code.google.com/apis/language/translate/v2/using_rest.html#auth"));
            } catch (Exception e) {
                LOGGER.error("Exception browsing to Google docs", e);
                showMessage = true;
            }
        }
        if (showMessage) {
            JOptionPane.showMessageDialog(this, SwingUtils.getMessage("settingsDialog.googleApiKeyHelp"));
        }
    }
