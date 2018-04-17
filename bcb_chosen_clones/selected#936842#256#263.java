    private void openDateFormatHelpButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("http://java.sun.com/javase/6/docs/api/java/text/SimpleDateFormat.html"));
            } catch (Exception err) {
            }
        }
    }
