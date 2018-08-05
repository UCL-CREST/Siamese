    void load() {
        try {
            JFileChooser fc = new JFileChooser();
            fc.showOpenDialog(this);
            File file = fc.getSelectedFile();
            if (file.isFile()) {
                System.out.println(file.toString());
                loadin(file);
            }
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        }
    }
