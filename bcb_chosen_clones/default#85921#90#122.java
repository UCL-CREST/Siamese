    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Next Character") && unistrok != null) {
            point++;
            lblCodepoint.setText("Unicode Codepoint: U+" + Integer.toHexString(unistrok.characters.get(point).codepoint));
        } else if (e.getActionCommand().equals("Previous Character") && unistrok != null) {
            point--;
            lblCodepoint.setText("Unicode Codepoint: U+" + Integer.toHexString(unistrok.characters.get(point).codepoint));
        } else if (e.getActionCommand().equals("Open")) {
            JFileChooser fc = new JFileChooser();
            int retval = fc.showOpenDialog(this);
            if (retval == JFileChooser.APPROVE_OPTION) {
                try {
                    unistrok = new Unistrok(fc.getSelectedFile().getPath());
                    point = 0;
                } catch (IOException ex) {
                    System.out.println("Problem loading " + fc.getSelectedFile().getName() + ": " + ex.getMessage());
                }
            }
        } else if (e.getActionCommand().equals("Save")) {
            JFileChooser fc = new JFileChooser();
            int retval = fc.showOpenDialog(this);
            if (retval == JFileChooser.APPROVE_OPTION) {
                try {
                    unistrok.saveFile(fc.getSelectedFile().getPath());
                } catch (IOException ex) {
                    System.out.println("Problem loading " + fc.getSelectedFile().getName() + ": " + ex.getMessage());
                }
            }
        } else if (e.getActionCommand().equals("Exit")) {
        }
        lblUnicodeCharacter.setText(String.valueOf((char) unistrok.characters.get(point).codepoint));
        updateStrokes();
    }
