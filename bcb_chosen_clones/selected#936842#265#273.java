    private void openBaseURLButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (Desktop.isDesktopSupported()) {
            try {
                URI baseURI = new URI(this.rssFeedURLField.getText());
                Desktop.getDesktop().browse(baseURI);
            } catch (Exception err) {
            }
        }
    }
