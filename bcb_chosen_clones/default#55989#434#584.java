    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        Object src = e.getSource();
        if (command.equals("New...")) {
            System.out.println(saveInterval);
        }
        if (command.equals("Print...")) {
            PrintUtilities.printComponent(textArea);
        }
        if (command.equals("Exit")) {
            youSure();
        }
        if (command.equals("WW")) {
        }
        if (command.equals("About Saturn Writer...")) {
            showAbout();
        }
        if (command.equals("Features")) {
            currentFeatures();
        }
        if (command.equals("Word Count")) {
            wordCountWindow();
        }
        if (command.equals("Font")) {
            FontWindow frame = new FontWindow(this);
        }
        if (command.equals("Find")) {
            find.setVisible(true);
        }
        if (command.equals("Exitf")) {
            find.setVisible(false);
        }
        if (command.equals("FindNext")) {
        }
        if (command.equals("SelectAll")) {
            textArea.selectAll();
        }
        if (command.equals("Time and Date")) {
            TimenDate frame = new TimenDate(this);
        }
        if (command.equals("Normal")) {
            textArea.setMargin(new Insets(3, 124, 3, 124));
        }
        if (command.equals("WebLayout")) {
            textArea.setMargin(new Insets(3, 124, 3, 124));
        }
        if (command.equals("PrintLay")) {
            textArea.setMargin(new Insets(104, 124, 104, 124));
        }
        if (command.equals("Delete")) {
            textArea.setText(textArea.getText().substring(0, textArea.getSelectionStart()) + textArea.getText().substring(textArea.getSelectionEnd()));
        }
        if (command.equals("Copy")) {
            textArea.copy();
            System.out.println(textArea.getSelectedText());
        }
        if (command.equals("Cut")) {
            textArea.cut();
        }
        if (command.equals("Paste")) {
            textArea.paste();
        }
        if (command.equals("Save")) {
            int returnVal = chooser.showSaveDialog(GUI.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                fileio.write(textArea.getText(), file.getPath());
            }
        }
        if (command.equals("Save As")) {
            int returnVal = chooser.showSaveDialog(GUI.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                fileio.write(textArea.getText(), file.getPath());
            }
        }
        if (command.equals("Open...")) {
            int returnVal = chooser.showOpenDialog(GUI.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                textArea.setText(fileio.read(file.getPath()));
            }
        }
        if (command.equals("StatusBar")) {
        }
        if (command.equals("LetCount")) {
            showLetCount();
        }
        if (command.equals("Spell Check")) {
            spellcheck();
        }
        if (command.equals("Auto List")) {
            AutoList frame = new AutoList(this);
        }
        if (command.equals("Color")) {
            dialog.setVisible(true);
        }
        if (command == ("Left")) {
            StyleConstants.setAlignment(set, StyleConstants.ALIGN_LEFT);
            textArea.setParagraphAttributes(set, true);
        }
        if (command == ("Center")) {
            StyleConstants.setAlignment(set, StyleConstants.ALIGN_CENTER);
            textArea.setParagraphAttributes(set, true);
        }
        if (command == ("Right")) {
            StyleConstants.setAlignment(set, StyleConstants.ALIGN_RIGHT);
            textArea.setParagraphAttributes(set, true);
        }
        if (command == ("Justified")) {
            StyleConstants.setAlignment(set, StyleConstants.ALIGN_JUSTIFIED);
            textArea.setParagraphAttributes(set, true);
        }
        if (command == ("Underline")) {
            StyleConstants.setUnderline(set, !StyleConstants.isUnderline(set));
            textArea.setParagraphAttributes(set, true);
        }
        if (command == ("Bold")) {
            StyleConstants.setBold(set, !StyleConstants.isBold(set));
            textArea.setParagraphAttributes(set, true);
        }
        if (command == ("Italic")) {
            StyleConstants.setItalic(set, !StyleConstants.isItalic(set));
            textArea.setParagraphAttributes(set, true);
        }
        if (command.equals("Image...")) {
            int returnVal = chooseImage.showOpenDialog(GUI.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File fileImage = chooseImage.getSelectedFile();
                try {
                    Style style = doc.addStyle("StyleName", null);
                    StyleConstants.setIcon(style, new ImageIcon(fileImage.getPath()));
                    doc.insertString(textArea.getCaretPosition(), "ignored text", style);
                } catch (BadLocationException event) {
                }
            }
        }
        if (command.equals("Options...")) {
            Preferences frame = new Preferences(this);
        }
        if (command.equals("Clip Art")) {
            ClipArt frame = new ClipArt(this);
        }
        String newFont = (String) font.getSelectedItem();
        String newSize = (String) size.getSelectedItem();
        int newSizeC = Integer.parseInt(newSize);
        StyleConstants.setFontFamily(set, newFont);
        textArea.setParagraphAttributes(set, true);
        StyleConstants.setFontSize(set, newSizeC);
        textArea.setParagraphAttributes(set, true);
    }
