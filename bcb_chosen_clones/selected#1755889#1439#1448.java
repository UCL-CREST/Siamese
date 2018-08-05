    private void menuHelpShowHelpActionPerformed(java.awt.event.ActionEvent evt) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(new File("resources/ayuda.txt"));
            } catch (Exception exc) {
                System.out.println(exc);
            }
        }
    }
