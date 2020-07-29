    private void browse(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Folgender Fehler ist beim �ffnen des Standart-" + "Browsers aufgetreten:\n" + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Ihre Plattform wird nicht unterst�tzt.", "Webseite besuchen", JOptionPane.ERROR_MESSAGE);
        }
    }
