            public void actionPerformed(ActionEvent ae) {
                if (ae.getActionCommand().equals("Browse...")) {
                    JFileChooser jfc = new JFileChooser();
                    jfc.setCurrentDirectory(new File(JConfig.getHomeDirectory("jbidwatcher")));
                    jfc.setApproveButtonText("Choose");
                    int rval = jfc.showOpenDialog(null);
                    if (rval == JFileChooser.APPROVE_OPTION) {
                        try {
                            filePath.setText(jfc.getSelectedFile().getCanonicalPath());
                        } catch (IOException ioe) {
                            filePath.setText(jfc.getSelectedFile().getAbsolutePath());
                        }
                    }
                }
            }
