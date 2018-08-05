    private void authorMouseClicked(java.awt.event.MouseEvent evt) {
        click = true;
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("http://www.blogjava.net/hadeslee"));
            } catch (URISyntaxException ex) {
            } catch (IOException ex) {
            }
        }
    }
