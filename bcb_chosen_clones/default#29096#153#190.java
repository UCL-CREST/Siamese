    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        String fullLog = "";
        fullLog += Debugger.getLog() + "\r\n";
        fullLog += Debugger.getActions() + "\r\n";
        fullLog += Debugger.getExceptionList();
        JFileChooser pickFile = new JFileChooser();
        pickFile.setCurrentDirectory(new File(Settings.lastSavedFolder));
        pickFile.setFileFilter(new LogFilter());
        pickFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int retVal = pickFile.showSaveDialog(this);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File seq = pickFile.getSelectedFile();
            if (!(new SequenceFilter().accept(seq))) {
                seq = new File(seq.toString() + ".fal");
            }
            if (seq.exists()) {
                int overwrite = JOptionPane.showConfirmDialog(this, "File already exists, overwrite it?", "File exists", JOptionPane.YES_NO_OPTION);
                if (overwrite == JOptionPane.YES_OPTION) {
                    seq.delete();
                } else {
                    return;
                }
            }
            PrintWriter pw;
            try {
                pw = new PrintWriter(seq);
                pw.println(fullLog);
                pw.close();
                Settings.lastSavedFolder = pickFile.getCurrentDirectory().toString();
                Settings.saveSettings();
                Debugger.appendLog("Succesfull: saveSequence");
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Error saving to file: " + seq, "Error saving log", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "I/O Exception while saving: " + ex.getMessage(), "Error saving log", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
