    private void projlblMouseClicked(java.awt.event.MouseEvent evt) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new URI("http://sourceforge.net/projects/w2sc/"));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }
