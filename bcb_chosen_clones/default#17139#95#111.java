            public void actionPerformed(ActionEvent e) {
                Options.openFileChooser.setSelectedFile(null);
                int returnVal = Options.openFileChooser.showOpenDialog(ReaderX.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = Options.openFileChooser.getSelectedFile();
                    try {
                        InputStream is = new BufferedInputStream(new FileInputStream(file));
                        MimeViewer jif = new MimeViewer(mWindow, is, file.getName());
                        is.close();
                        jif.setSize(320, 240);
                        jif.setVisible(true);
                        desktop.add(jif);
                    } catch (Exception ex) {
                        LogFrame.log(ex);
                    }
                }
            }
