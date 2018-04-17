    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fileChooser = new JFileChooser();
        ExampleFileFilter filter = new ExampleFileFilter(new String[] { "qc" }, "quantum circuits");
        fileChooser.addChoosableFileFilter(filter);
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            ObjectOutputStream output = null;
            try {
                output = new ObjectOutputStream(new FileOutputStream(file));
                try {
                    int[] size = { xRegister.size, yRegister.size };
                    output.writeObject(size);
                    output.writeObject(circuitPanel.gates);
                    output.flush();
                } catch (EOFException eof) {
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                try {
                    if (output != null) output.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }
