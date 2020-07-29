    public void mouseClicked(MouseEvent e) {
        try {
            java.awt.Desktop d = Desktop.getDesktop();
            if (Desktop.isDesktopSupported()) {
                d.browse(new URI(UIDefaults.PRODUCT_URL));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
