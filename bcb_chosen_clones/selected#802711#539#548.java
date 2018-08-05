    private void aboutMyTwitterMouseClicked(java.awt.event.MouseEvent evt) {
        if (!Desktop.isDesktopSupported()) return;
        try {
            Desktop.getDesktop().browse(new URI("http://twitter.com/vigneshwaranr"));
        } catch (URISyntaxException ex) {
            Logger.getLogger(JJSplit.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JJSplit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
