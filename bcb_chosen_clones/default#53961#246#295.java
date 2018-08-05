        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == itmNew || e.getSource() == buttonNew) {
                tAreaEditor.setText("");
                newFile = true;
            }
            if (e.getSource() == itmOpen || e.getSource() == buttonOpen) {
                FileInputStream readFile;
                int returnval = mySaveFile.showOpenDialog(c);
                File f = mySaveFile.getSelectedFile();
                String openFilePath = f.getPath();
                OpenOrSaveFilePath = openFilePath;
                nameOfTheFile = f.getName();
                setTitle(title + "-  [" + nameOfTheFile + "]");
                runPathforJava = f.getParent();
                System.out.println("The path of the file is:  " + runPathforJava);
                try {
                    readFile = new FileInputStream(openFilePath);
                    byte data[] = new byte[readFile.available()];
                    readFile.read(data);
                    String byteText = new String(data);
                    tAreaEditor.setText(byteText);
                } catch (Exception excp) {
                    System.out.println("get the hell out of here");
                }
            }
            if (e.getSource() == itmSave || e.getSource() == buttonSave) {
                FileOutputStream outputfile;
                if (newFile) {
                    int returnval = mySaveFile.showSaveDialog(c);
                    File f = mySaveFile.getSelectedFile();
                    savedFilePath = f.getPath();
                    OpenOrSaveFilePath = savedFilePath;
                    nameOfTheFile = f.getName();
                    runPathforJava = f.getParent();
                    newFile = false;
                }
                System.out.println("The path of the file is:" + savedFilePath);
                try {
                    outputfile = new FileOutputStream(savedFilePath);
                    byte data[] = tAreaEditor.getText().getBytes();
                    outputfile.write(data);
                } catch (Exception excp) {
                    System.out.println("get the hell out of here");
                }
            }
            if (e.getSource() == itmCut) tAreaEditor.cut();
            if (e.getSource() == itmCopy) tAreaEditor.copy();
            if (e.getSource() == itmPaste) tAreaEditor.paste();
            if (e.getSource() == itmAbout) JOptionPane.showMessageDialog(null, "name:Nandagopal" + "\n\nLicense:GNU GPL" + "\n\ne-mail: nand...@gmail.com");
        }
