            public void actionPerformed(ActionEvent e) {
                Options.saveFileChooser.setSelectedFile(null);
                int returnVal = Options.saveFileChooser.showSaveDialog(MessageViewer.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = Options.saveFileChooser.getSelectedFile();
                        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                        msg.writeTo(os);
                        os.close();
                    } catch (Exception ex) {
                        LogFrame.log(ex);
                    }
                }
            }
