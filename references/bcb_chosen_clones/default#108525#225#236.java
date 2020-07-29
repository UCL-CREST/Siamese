    public void doDotdotdot() {
        final JFileChooser chooser = new DirectoryChooser(new File(x_main_dir.getText()));
        writeStatus("Choosing directory...");
        if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(x_main)) {
            final File dir = chooser.getSelectedFile();
            x_main_dir.setText(dir.getAbsolutePath());
            m_listModel.removeAllElements();
            writeStatus("Chose " + dir.getName());
        } else {
            writeStatus("Choosing directory cancelled");
        }
    }
