    public void actionPerformed(ActionEvent event) {
        String cmd = event.getActionCommand();
        if (cmd.equals("CMD_EXIT")) {
            windowClosed();
        } else if (cmd.equals("CMD_FILE_NEW")) {
            path = "";
            file = JOptionPane.showInputDialog(this, "Please enter the name of the new bookman list:", "Create a New Bookman List", JOptionPane.QUESTION_MESSAGE);
            if (!file.endsWith(".bml")) file += ".bml";
            booktable.newfile();
            title();
        } else if (cmd.equals("CMD_FILE_OPEN")) {
            JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
            fc.addChoosableFileFilter(new BookFilter());
            try {
                if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    file = fc.getSelectedFile().getName();
                    path = fc.getSelectedFile().getParent() + File.separator;
                    booktable.open(path + file);
                    title();
                    loadSetting();
                }
            } catch (Exception e) {
            }
        } else if (cmd.equals("CMD_HELP_ABOUT")) {
            JOptionPane.showMessageDialog(this, "          Bookman " + version + "\n               " + date, "About Bookman", JOptionPane.INFORMATION_MESSAGE);
        } else if (cmd.equals("CMD_PRINT")) {
            try {
                table.print();
            } catch (Exception e) {
            }
        } else if (cmd.equals("CMD_FILE_SAVE")) {
            try {
                booktable.save(path + file);
                title();
                saveSetting();
            } catch (Exception e) {
            }
        } else if (cmd.equals("CMD_FILE_SAVE_AS")) {
            JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
            fc.addChoosableFileFilter(new BookFilter());
            try {
                if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    file = fc.getSelectedFile().getName();
                    path = fc.getSelectedFile().getParent() + File.separator;
                    if (!file.endsWith(".bml")) file += ".bml";
                    booktable.save(path + file);
                    title();
                    saveSetting();
                }
            } catch (Exception e) {
            }
        } else if (cmd.equals("CMD_FILE_EXPORT_CSV")) {
            try {
                booktable.exportcsv(path + file.split("[.]")[0] + ".csv");
            } catch (Exception e) {
            }
        } else if (cmd.equals("CMD_NEW")) {
            buildNewBook();
        } else if (cmd.equals("CMD_NEW_SAVE")) {
            if (booktable.contains(textName.getText())) {
                JOptionPane.showMessageDialog(newbook, "This Book is already in your list, you cannot add it again.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (textName.getText().length() > 0) {
                    booktable.add(textName.getText(), textAuthor.getSelectedItem().toString(), textVolumes.getText(), buttonRead.isSelected(), buttonEnd.isSelected(), buttonBurn.isSelected(), textNotes.getText());
                    newbook.setVisible(false);
                }
            }
        } else if (cmd.equals("CMD_NEW_CANCEL")) {
            newbook.setVisible(false);
        } else if (cmd.equals("CMD_COPY")) {
            if (table.getSelectedRowCount() > 0) Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString()), new StringSelection(table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString()));
        } else if (cmd.equals("CMD_COPYROW")) {
            if (table.getSelectedRowCount() > 0) {
                String s = "";
                for (int r : table.getSelectedRows()) {
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        s += table.getValueAt(r, i).toString();
                        if (i + 1 != table.getColumnCount()) s += "\t";
                    }
                    s += "\n";
                }
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(s), new StringSelection(s));
            }
        } else if (cmd.equals("CMD_CUT")) {
            if (table.getSelectedRowCount() > 0) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString()), new StringSelection(table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString()));
                table.setValueAt("", table.getSelectedRow(), table.getSelectedColumn());
            }
        } else if (cmd.equals("CMD_PASTE")) {
            if (table.getSelectedRowCount() > 0) {
                try {
                    table.setValueAt((String) (Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this).getTransferData(DataFlavor.stringFlavor)), table.getSelectedRow(), table.getSelectedColumn());
                } catch (Exception e) {
                }
            }
        } else if (cmd.equals("CMD_CLEAR")) {
            if (table.getSelectedRowCount() > 0) table.setValueAt("", table.getSelectedRow(), table.getSelectedColumn());
        } else if (cmd.equals("CMD_DELETE")) {
            if (table.getSelectedRowCount() > 0) {
                int n = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book, \"" + table.getValueAt(table.getSelectedRow(), 0) + "\"?", "Bookman", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) booktable.remove((String) table.getValueAt(table.getSelectedRow(), 0));
            }
        } else {
            System.out.println("Command invoked - " + cmd);
        }
    }
