    public void actionPerformed(ActionEvent action) {
        String name = action.getActionCommand();
        if (action.getSource() == browseFirstB) {
            JFileChooser chooser = new JFileChooser(userDir);
            chooser.addChoosableFileFilter(new modelFileTypeFilter("bmp", "Windows Bitmap image"));
            chooser.addChoosableFileFilter(new modelFileTypeFilter("sl2", "Slice file (colour)"));
            chooser.addChoosableFileFilter(new modelFileTypeFilter("sl", "Slice file"));
            int option = chooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                String extension = modelFileTypeFilter.getExtension(chooser.getSelectedFile());
                if (extension.equals("bmp") || extension.equals("sl") || extension.equals("sl2")) {
                    if (Character.isDigit(chooser.getSelectedFile().getAbsolutePath().charAt(0))) {
                        JOptionPane.showMessageDialog(null, "The filename must start with a letter.", "Invalid filename", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (extension.equals("bmp") || extension.equals("sl2")) {
                        if (!Character.isDigit(chooser.getSelectedFile().getAbsolutePath().charAt(chooser.getSelectedFile().getAbsolutePath().length() - 5))) {
                            JOptionPane.showMessageDialog(null, "The filename must end with a digit indicating which layer it is.", "Invalid filename", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } else {
                        if (!Character.isDigit(chooser.getSelectedFile().getAbsolutePath().charAt(chooser.getSelectedFile().getAbsolutePath().length() - 4))) {
                            JOptionPane.showMessageDialog(null, "The filename must end with a digit indicating which layer it is.", "Invalid filename", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    int lastSlash = chooser.getSelectedFile().getAbsolutePath().lastIndexOf("\\");
                    if (lastSlash == -1) {
                        lastSlash = chooser.getSelectedFile().getAbsolutePath().lastIndexOf("/");
                    }
                    userDir = chooser.getSelectedFile().getAbsolutePath().substring(0, lastSlash + 1);
                    directoryFirst.setText(chooser.getSelectedFile().getAbsolutePath());
                } else {
                    JOptionPane.showMessageDialog(null, "The file does not have a valid .bmp or .sl extension.", "Invalid file extension", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (action.getSource() == browseLastB) {
            JFileChooser chooser = new JFileChooser(userDir);
            chooser.addChoosableFileFilter(new modelFileTypeFilter("bmp", "Windows Bitmap image"));
            chooser.addChoosableFileFilter(new modelFileTypeFilter("sl2", "Slice file (colour)"));
            chooser.addChoosableFileFilter(new modelFileTypeFilter("sl", "Slice file"));
            int option = chooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                String extension = modelFileTypeFilter.getExtension(chooser.getSelectedFile());
                if (extension.equals("bmp") || extension.equals("sl") || extension.equals("sl2")) {
                    if (Character.isDigit(chooser.getSelectedFile().getAbsolutePath().charAt(0))) {
                        JOptionPane.showMessageDialog(null, "The filename must start with a letter.", "Invalid Bitmap filename", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (extension.equals("bmp") || (extension.equals("sl2"))) {
                        if (!Character.isDigit(chooser.getSelectedFile().getAbsolutePath().charAt(chooser.getSelectedFile().getAbsolutePath().length() - 5))) {
                            JOptionPane.showMessageDialog(null, "The filename must end with a digit indicating which layer it is.", "Invalid filename", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } else {
                        if (!Character.isDigit(chooser.getSelectedFile().getAbsolutePath().charAt(chooser.getSelectedFile().getAbsolutePath().length() - 4))) {
                            JOptionPane.showMessageDialog(null, "The filename must end with a digit indicating which layer it is.", "Invalid filename", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    int lastSlash = chooser.getSelectedFile().getAbsolutePath().lastIndexOf("\\");
                    if (lastSlash == -1) {
                        lastSlash = chooser.getSelectedFile().getAbsolutePath().lastIndexOf("/");
                    }
                    userDir = chooser.getSelectedFile().getAbsolutePath().substring(0, lastSlash + 1);
                    directoryLast.setText(chooser.getSelectedFile().getAbsolutePath());
                } else {
                    JOptionPane.showMessageDialog(null, "The file does not have a valid bmp extension.", "Invalid Bitmap extension", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (action.getSource() == continueB) {
            if ((directoryFirst.getText().equals("")) || (directoryLast.getText().equals(""))) {
                JOptionPane.showMessageDialog(null, "You must give both the first and last layer locations", "Layer location expected", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int lastSlash;
            int index;
            String extension;
            lastSlash = directoryFirst.getText().lastIndexOf("\\");
            if (lastSlash == -1) {
                lastSlash = directoryFirst.getText().lastIndexOf("/");
            }
            firstDirectory = directoryFirst.getText().substring(0, lastSlash + 1);
            firstBaseName = directoryFirst.getText().substring(lastSlash + 1);
            if (firstBaseName.contains(".sl2")) {
                extension = ".sl2";
                firstBaseName = firstBaseName.replaceAll(".sl2", "");
            } else if (firstBaseName.contains(".sl")) {
                extension = ".sl";
                firstBaseName = firstBaseName.replaceAll(".sl", "");
            } else {
                extension = ".bmp";
                firstBaseName = firstBaseName.replaceAll(".bmp", "");
            }
            index = firstBaseName.length() - 1;
            while ((index >= 0) && (Character.isDigit(firstBaseName.charAt(index)))) {
                index--;
            }
            firstNum = Integer.valueOf(firstBaseName.substring(index + 1));
            firstBaseName = firstBaseName.substring(0, index + 1);
            lastSlash = directoryLast.getText().lastIndexOf("\\");
            if (lastSlash == -1) {
                lastSlash = directoryLast.getText().lastIndexOf("/");
            }
            lastDirectory = directoryLast.getText().substring(0, lastSlash + 1);
            lastBaseName = directoryLast.getText().substring(lastSlash + 1);
            if (lastBaseName.contains(".sl2")) {
                if (!extension.equals(".sl2")) {
                    JOptionPane.showMessageDialog(null, "Both files must be of the same type. Either .bmp, .sl or .sl2.", "File types differ", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                lastBaseName = lastBaseName.replaceAll(".sl2", "");
            } else if (lastBaseName.contains(".sl")) {
                if (!extension.equals(".sl")) {
                    JOptionPane.showMessageDialog(null, "Both files must be of the same type. Either .bmp, .sl or .sl2.", "File types differ", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                lastBaseName = lastBaseName.replaceAll(".sl", "");
            } else {
                if (!extension.equals(".bmp")) {
                    JOptionPane.showMessageDialog(null, "Both files must be of the same type. Either .bmp, .sl or .sl2.", "File types differ", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                lastBaseName = lastBaseName.replaceAll(".bmp", "");
            }
            index = lastBaseName.length() - 1;
            while ((index >= 0) && (Character.isDigit(lastBaseName.charAt(index)))) {
                index--;
            }
            lastNum = Integer.valueOf(lastBaseName.substring(index + 1));
            lastBaseName = lastBaseName.substring(0, index + 1);
            if (!firstDirectory.equals(lastDirectory)) {
                JOptionPane.showMessageDialog(null, "The locations of the images must be in the same directory.", "Directories differ", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!firstBaseName.equals(lastBaseName)) {
                JOptionPane.showMessageDialog(null, "The base filenames of the images must be the same.", "Base filenames of images differ", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (lastNum < firstNum) {
                JOptionPane.showMessageDialog(null, "The number of the last image is smaller that the first image.", "Images must be in increasing order.", JOptionPane.ERROR_MESSAGE);
                return;
            }
            mainProg.directory = firstDirectory;
            mainProg.filenameBase = firstBaseName;
            mainProg.fileExtension = extension;
            mainProg.startIndex = firstNum;
            mainProg.stopIndex = lastNum;
            mainProg.hollowOutModel = hollowOutCB.isSelected();
            mainProg.shellThickness = shellThickness.getValue();
            mainProg.numLayers = (lastNum + 1 - firstNum);
            mainProg.alterCenterOfMass = changeCOM.isSelected();
            finished = true;
        } else if (action.getSource() == hollowOutCB) {
            if (hollowOutCB.isSelected()) {
                shellThickness.setEnabled(true);
            } else {
                shellThickness.setEnabled(false);
            }
        } else if (name.equals("About")) {
            JOptionPane.showMessageDialog(null, "LSculpturer: LConstruct Cellular Automata\n Automated Brick Sculpture Construction Application\nVersion 1.0 \nAuthor : Eugene Smal \nContact: eugene.smal@gmail.com \nStellenbosch University Master's Student" + " \n2008 \n", "About LSculpturer: LConstruct", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images\\about.gif"));
        } else if (name.equals("Help contents")) {
            helpGUI helpWindow = new helpGUI();
        }
    }
