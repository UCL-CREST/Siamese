    private void saveFastaItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getSource() == saveFastaItem) {
            ArrayList data = new ArrayList();
            ListModel model = selectedGenes.getModel();
            for (int i = 0; i < model.getSize(); i++) {
                data.add(model.getElementAt(i));
            }
            if (data.size() == 0) {
                JOptionPane.showMessageDialog(null, "Please add sequences first");
                return;
            }
            int returnVal = fc.showSaveDialog(Demarcations.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File output = fc.getSelectedFile();
                SelectSeqBins fileMaker = new SelectSeqBins(fastaRGCopy, data, output);
                log.append("Saved to file: " + output.getPath() + "\n");
            } else log.append("Dialog cancelled by user.");
        }
    }
