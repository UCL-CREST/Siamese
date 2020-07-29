    public void actionPerformed(ActionEvent arg0) {
        try {
            address = new URI("http://www.keep-totem.co.uk");
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
        System.out.println(Desktop.isDesktopSupported());
        try {
            Desktop.getDesktop().browse(address);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainWindowFrame.getRootPane(), "An I/O error occurred: the TOTEM metadata database could not be accessed");
            logger.warn("A I/O error occurred: the TOTEM metadata database could not be accessed.");
        }
    }
