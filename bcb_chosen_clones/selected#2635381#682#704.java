    private void deleteImageDBDialogShowImageButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (deleteImageDBTable.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(getFrame(), "Debe seleccionar imagen a mostrar", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                int row = deleteImageDBTable.getSelectedRow();
                String name = (String) deleteImageDBTable.getValueAt(row, 4);
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        String whereIsImage = new File(G.deleteImagesFromDBDialogInputPictogramsPath + File.separator + name.substring(0, 1).toUpperCase() + File.separator + name).getCanonicalPath();
                        desktop.open(new File(whereIsImage));
                    } catch (Exception exc) {
                        System.out.println(exc);
                    }
                } else {
                    JOptionPane.showMessageDialog(getFrame(), "Imposible mostrar imagen, error interno", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception exc) {
            System.out.println(exc);
        }
    }
