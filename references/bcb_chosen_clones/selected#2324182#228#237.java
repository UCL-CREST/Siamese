    private void limitationsLabelMouseClicked(java.awt.event.MouseEvent evt) {
        if (!Desktop.isDesktopSupported()) {
            return;
        }
        try {
            NULogger.getLogger().log(Level.INFO, "{0}Limitations label clicked.. Opening webpage..", HostsPanel.class.getName());
            Desktop.getDesktop().browse(new URI("http://neembuuuploader.sourceforge.net/support.html#limitations"));
        } catch (Exception ex) {
        }
    }
