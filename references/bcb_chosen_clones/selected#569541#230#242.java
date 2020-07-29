    private void urlLabelMouseClicked(java.awt.event.MouseEvent evt) {
        if (urlLabel.getText().equals("") == false) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(urlLabel.getText()));
                } catch (Throwable ex) {
                    Global.dialogs.showErrorDialog(Bundles.subgetBundle.getString("Error"), Bundles.subgetBundle.getString("Could_not_open_a_default_internet_browser."));
                }
            } else {
                Global.dialogs.showErrorDialog(Bundles.subgetBundle.getString("Error"), Bundles.subgetBundle.getString("Could_not_open_a_default_internet_browser."));
            }
        }
    }
