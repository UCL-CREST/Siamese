    public void saveToFile(nodeBase baseNode) {
        try {
            JFileChooser jfc = new JFileChooser();
            int retValue = jfc.showSaveDialog(parent);
            File baseDirectoryFile = new File("");
            if (EncoderLoad.directory == null) EncoderLoad.directory = new File(System.getProperty("user.dir"));
            jfc.setCurrentDirectory(EncoderLoad.directory);
            if (retValue == JFileChooser.APPROVE_OPTION) {
                ((EncoderLoad) (parent.ELoad)).setBaseDir(jfc.getCurrentDirectory());
                baseDirectoryFile = jfc.getSelectedFile();
                BufferedWriter out = new BufferedWriter(new FileWriter(baseDirectoryFile));
                out.write(text.getText());
                out.close();
            }
        } catch (Exception e) {
            parent.EerrorMsg.text.setText("EncoderProperties.saveToFile " + e);
            parent.jtp.setSelectedIndex(3);
        }
    }
