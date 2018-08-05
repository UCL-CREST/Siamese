    @Override
    public void actionPerformed(ActionEvent e) {
        if (java.awt.Desktop.isDesktopSupported()) {
            try {
                java.awt.Desktop.getDesktop().browse((java.net.URI) this.getTarget());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
