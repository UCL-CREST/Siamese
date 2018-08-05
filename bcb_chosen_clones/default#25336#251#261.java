            public void actionPerformed(ActionEvent e) {
                int returnVal = fileManager.showOpenDialog(CAEMURA_GUI.this);
                if (e.getSource() == openFileManager) {
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fileManager.getSelectedFile();
                        CAEMURA.mBorrowedResource.mainConsole.append("Opening: " + file.getName() + ".\n");
                    } else {
                        CAEMURA.mBorrowedResource.mainConsole.append("Open command cancelled by user.\n");
                    }
                }
            }
