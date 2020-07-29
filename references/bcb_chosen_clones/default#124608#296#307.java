                public void actionPerformed(java.awt.event.ActionEvent e) {
                    JFileChooser fc = new JFileChooser(new File(DirName));
                    fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    if (fc.showOpenDialog(getJContentPane().getParent()) == JFileChooser.APPROVE_OPTION) {
                        if (fc.getSelectedFile().isFile()) {
                            FileName = fc.getSelectedFile().getAbsolutePath();
                            getJTextAreaFilename().setText(FileName);
                        }
                        DirName = fc.getCurrentDirectory().getAbsolutePath();
                        logger.trace(" setting directory .................." + DirName);
                    }
                }
