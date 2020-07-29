    public void open() {
        JFileChooser fc = new JFileChooser(Main.RUTA);
        fc.showOpenDialog(this);
        File file = fc.getSelectedFile();
        if (file != null) {
        }
    }
