                public void actionPerformed(ActionEvent e) {
                    Options.saveFileChooser.setSelectedFile(null);
                    int returnVal = Options.saveFileChooser.showSaveDialog(parent);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = Options.saveFileChooser.getSelectedFile();
                        try {
                            OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                            mbp.writeTo(os);
                            os.close();
                        } catch (Exception ex) {
                            LogFrame.log(ex);
                        }
                    }
                }
