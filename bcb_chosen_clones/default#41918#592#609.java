    public void saveProject() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save");
        chooser.setApproveButtonText("Save");
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = chooser.getCurrentDirectory() + "/" + chooser.getSelectedFile().getName();
                File f = new File(filePath);
                PrintWriter out = new PrintWriter(new FileWriter(f));
                out.print(display_code_textarea.getText());
                out.close();
            } catch (IOException e) {
                System.err.println("IO Exception reading file: ");
                System.exit(1);
            }
        }
    }
