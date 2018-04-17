    public void main_cd_onPress() {
        final JFileChooser chooser = new DirectoryChooser(new File(s_dir));
        if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(get("main.cd").getComponent())) {
            s_dir = chooser.getSelectedFile().getAbsolutePath();
        }
        restart("");
    }
