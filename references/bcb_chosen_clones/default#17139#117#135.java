            public void actionPerformed(ActionEvent e) {
                Options.openFileChooser.setSelectedFile(null);
                int returnVal = Options.openFileChooser.showOpenDialog(ReaderX.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = Options.openFileChooser.getSelectedFile();
                    try {
                        InputStream is = new BufferedInputStream(new FileInputStream(file));
                        MimeMessage msg = new MimeMessage(Options.session, is);
                        is.close();
                        MessageViewer msgView = new MessageViewer(msg, mWindow);
                        msgView.setSize(640, 480);
                        msgView.show();
                        desktop.add(msgView);
                        msgView.setSelected(true);
                    } catch (Exception ex) {
                        LogFrame.log(ex);
                    }
                }
            }
