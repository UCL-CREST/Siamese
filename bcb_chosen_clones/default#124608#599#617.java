    private JButton getJButtonBrowseEXE() {
        if (jButtonBrowseEXE == null) {
            jButtonBrowseEXE = new JButton();
            jButtonBrowseEXE.setBounds(new Rectangle(645, 193, 21, 20));
            jButtonBrowseEXE.setText("...");
            jButtonBrowseEXE.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    JFileChooser fc = new JFileChooser(new File("."));
                    fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    if (fc.showOpenDialog(getJContentPane().getParent()) == JFileChooser.APPROVE_OPTION) {
                        getJTextFieldEXefile().setText(fc.getSelectedFile().getAbsolutePath());
                        App.ProgramFile = fc.getSelectedFile().getAbsolutePath();
                    }
                }
            });
        }
        return jButtonBrowseEXE;
    }
