    private void homeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI("http://db.apache.org/derby/"));
            } else {
                JOptionPane.showMessageDialog(this, "Sorry, your desktop is noe supported.");
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
