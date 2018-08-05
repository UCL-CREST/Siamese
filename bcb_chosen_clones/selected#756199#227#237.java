    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(new File(UsefulMethods.getLastSavedLocation()));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Following error occurred while opening:\n" + e.getMessage(), "Cannot Open", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Your system does not support this operation.", "Cannot Open", JOptionPane.ERROR_MESSAGE);
        }
    }
