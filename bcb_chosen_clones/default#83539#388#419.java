    public void saveDocumentAs() {
        try {
            File current = new File(".");
            JFileChooser chooser = new JFileChooser(current);
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setFileFilter(new HTMLFileFilter());
            int approval = chooser.showSaveDialog(this);
            if (approval == JFileChooser.APPROVE_OPTION) {
                File newFile = chooser.getSelectedFile();
                if (newFile.exists()) {
                    String message = newFile.getAbsolutePath() + " already exists. \n" + "Do you want to replace it?";
                    if (JOptionPane.showConfirmDialog(this, message) == JOptionPane.YES_OPTION) {
                        currentFile = newFile;
                        FileWriter fw = new FileWriter(currentFile);
                        fw.write(textPane.getText());
                        fw.close();
                        if (debug) System.out.println("Saved " + currentFile.getAbsolutePath());
                    }
                } else {
                    currentFile = new File(newFile.getAbsolutePath());
                    FileWriter fw = new FileWriter(currentFile);
                    fw.write(textPane.getText());
                    fw.close();
                    if (debug) System.out.println("Saved " + currentFile.getAbsolutePath());
                }
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println("FileNotFoundException: " + fnfe.getMessage());
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }
