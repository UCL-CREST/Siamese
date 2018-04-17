    public void actionPerformed(ActionEvent e) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URL(url).toURI());
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
        } else {
            JOptionPane.showConfirmDialog(null, "Can not determine the default web browser.\n" + url);
        }
    }
