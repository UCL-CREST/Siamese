    @Override
    public void actionPerformed(ActionEvent e) {
        if (Desktop.isDesktopSupported()) {
            Desktop d = Desktop.getDesktop();
            try {
                if (d.isSupported(Desktop.Action.BROWSE)) {
                    d.browse(new URI("http://zdfmediathk.sourceforge.net/"));
                }
            } catch (Exception ex) {
            }
        }
    }
