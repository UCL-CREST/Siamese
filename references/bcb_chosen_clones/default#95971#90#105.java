            public void actionPerformed(ActionEvent e) {
                String directory = (lastDirectory != null) ? lastDirectory : "data";
                JFileChooser fileChooser = new JFileChooser(directory);
                int returnVal = fileChooser.showOpenDialog(statusWindow.getTopLevelAncestor());
                switch(returnVal) {
                    case JFileChooser.APPROVE_OPTION:
                        openFile(fileChooser.getSelectedFile());
                        break;
                    case JFileChooser.CANCEL_OPTION:
                        message("Open cancelled", 10);
                        break;
                    default:
                        System.err.println("Bogosity from JFileChooser");
                        break;
                }
            }
