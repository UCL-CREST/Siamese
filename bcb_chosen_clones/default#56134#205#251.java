    public void saveSequence() {
        Debugger.appendAction("Starting saveSequence");
        if (flames.size() > 0) {
            JFileChooser pickFile = new JFileChooser();
            pickFile.setCurrentDirectory(new File(Settings.lastSavedFolder));
            pickFile.setFileFilter(new SequenceFilter());
            pickFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int retVal = pickFile.showSaveDialog(main);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File seq = pickFile.getSelectedFile();
                if (!(new SequenceFilter().accept(seq))) {
                    seq = new File(seq.toString() + ".seq");
                }
                if (seq.exists()) {
                    int overwrite = JOptionPane.showConfirmDialog(main, "File already exists, overwrite it?", "File exists", JOptionPane.YES_NO_OPTION);
                    if (overwrite == JOptionPane.YES_OPTION) {
                        seq.delete();
                        Debugger.appendLog("Status: saveSequence (File overwrite allowed)");
                    } else {
                        Debugger.appendLog("Canceled: saveSequence (No overwrite allowed)");
                        return;
                    }
                }
                PrintWriter pw;
                try {
                    pw = new PrintWriter(seq);
                    pw.println(getSequence());
                    pw.close();
                    Settings.lastSavedFolder = pickFile.getCurrentDirectory().toString();
                    Settings.saveSettings();
                    Debugger.appendLog("Succesfull: saveSequence");
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(main, "Error saving to file: " + seq, "Error saving sequence", JOptionPane.ERROR_MESSAGE);
                    Debugger.appendLog("Failed: saveSequence");
                    Debugger.storeException(ex);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(main, "I/O Exception while saving: " + ex.getMessage(), "Error saving sequence", JOptionPane.ERROR_MESSAGE);
                    Debugger.appendLog("Failed: saveSequence");
                    Debugger.storeException(ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(main, "Sequence is empty", "Error saving sequence", JOptionPane.ERROR_MESSAGE);
            Debugger.appendLog("Failed: openSequence");
        }
        Debugger.appendAction("Ending saveSequence");
    }
