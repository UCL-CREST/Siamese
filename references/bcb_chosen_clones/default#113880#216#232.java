    private void obtenerCSVActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showSaveDialog(this);
            File file;
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                CSVWriter writer = new CSVWriter(new FileWriter(file.getAbsolutePath()));
                writer.writeAll(getCategoria().getResultSet(), true);
                writer.close();
            }
        } catch (SQLException excepcionSQL) {
            JOptionPane.showMessageDialog(null, "Hubo un problema con la base de dato MySQL.", "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (IOException excepcionIO) {
            JOptionPane.showMessageDialog(null, "Hubo un problema de lectura escritura.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
