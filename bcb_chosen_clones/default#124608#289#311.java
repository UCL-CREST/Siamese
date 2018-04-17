    private JButton getJButtonBrowse() {
        if (jButtonBrowse == null) {
            jButtonBrowse = new JButton();
            jButtonBrowse.setBounds(new Rectangle(571, 29, 18, 38));
            jButtonBrowse.setText(".....");
            jButtonBrowse.addActionListener(new java.awt.event.ActionListener() {

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
            });
        }
        return jButtonBrowse;
    }
