    @Override
    public void mouseClicked(MouseEvent e) {
        if (uri != null && Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException ex) {
                Logger.getLogger(PrettyIcon.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                ex.printStackTrace();
            }
        }
    }
