    private void registerMouseClicked(java.awt.event.MouseEvent evt) {
        String url = "http://register.jabber.org/";
        if (Desktop.isDesktopSupported()) {
            Desktop desk = Desktop.getDesktop();
            if (desk.isSupported(Desktop.Action.BROWSE)) {
                try {
                    URI account = new URI(url);
                    desk.browse(account);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(rootPane, "Cannot open http://register.jabber.org.  You don't have a registered application for browsing.");
                } catch (URISyntaxException e) {
                    JOptionPane.showMessageDialog(rootPane, "An unexpected error has occurred\n" + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "Cannot open http://register.jabber.org.  Please visit the site manually.");
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Cannot open http://register.jabber.org.  Please visit the site manually.");
        }
    }
