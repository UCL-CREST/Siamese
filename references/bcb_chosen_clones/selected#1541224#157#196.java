    private void performDownload() {
        List<String> selected = filesPane.getSelectedValuesList();
        if (selected == null || selected.isEmpty() || selected.size() != 1) {
            JOptionPane.showMessageDialog(this, "Please select one path");
            return;
        }
        RFile file = new RFile(selected.get(0));
        if (!file.isFile()) {
            JOptionPane.showMessageDialog(this, "file does not exist anymore");
            return;
        }
        chooser.setSelectedFile(new File(chooser.getCurrentDirectory(), file.getName()));
        int ok = chooser.showSaveDialog(this);
        if (ok != JFileChooser.APPROVE_OPTION) {
            return;
        }
        FileOutputStream fout = null;
        RFileInputStream in = null;
        try {
            fout = new FileOutputStream(chooser.getSelectedFile());
            in = new RFileInputStream(file);
            IOUtils.copy(in, fout);
            JOptionPane.showMessageDialog(this, "File downloaded to " + chooser.getSelectedFile(), "Download finished", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException iOException) {
            JOptionPane.showMessageDialog(this, "Error: " + iOException, "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Throwable t) {
                }
            }
            if (fout != null) {
                try {
                    fout.close();
                } catch (Throwable t) {
                }
            }
        }
    }
