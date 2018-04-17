    void save() {
        try {
            JFileChooser fc = new JFileChooser();
            fc.showSaveDialog(this);
            File file = fc.getSelectedFile();
            System.out.println(file.toString());
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            String text = input_doc.getText(0, input_doc.getLength());
            writer.write(text, 0, text.length());
            writer.flush();
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        }
    }
