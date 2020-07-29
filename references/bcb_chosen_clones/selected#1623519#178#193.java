    private void goBtnActionPerformed(java.awt.event.ActionEvent evt) {
        Desktop desktop;
        if ((desktop = Desktop.getDesktop()).isDesktopSupported()) {
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                URI uri = null;
                try {
                    uri = new URI(internetEdt.getText());
                    desktop.browse(uri);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } catch (URISyntaxException use) {
                    use.printStackTrace();
                }
            }
        }
    }
