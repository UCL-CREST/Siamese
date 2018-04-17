    private void downloadlblMouseClicked(java.awt.event.MouseEvent evt) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new URI(Way2SMSCore.downloadlink));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }
