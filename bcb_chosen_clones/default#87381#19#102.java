    public void showNewDlg() {
        dlgNew = new JDialog(this, "New graph", true);
        JPanel jpFPath = new JPanel(new GridLayout(10, 1, 5, 5));
        jpFPath.setBorder(new LineBorder(Color.GRAY));
        jpFPath.add(new JLabel("Graph name:"));
        final JTextField jtGraphName = new JTextField();
        jpFPath.add(jtGraphName);
        jpFPath.add(new JLabel("Location:"));
        final JTextField jtGraphPath = new JTextField();
        jtGraphPath.setEnabled(false);
        JButton bDir = new JButton("...");
        bDir.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                String IDir = jtGraphPath.getText();
                if (IDir.trim().equals("")) IDir = "./";
                JFileChooser jfc = new JFileChooser(IDir);
                jfc.setDialogType(JFileChooser.DIRECTORIES_ONLY);
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (jfc.showOpenDialog(dlgNew) == JFileChooser.APPROVE_OPTION) {
                    jtGraphPath.setText(jfc.getSelectedFile().getPath());
                }
            }
        });
        JPanel jpParent = new JPanel(new BorderLayout());
        jpParent.add(jtGraphPath, BorderLayout.CENTER);
        jpParent.add(bDir, BorderLayout.EAST);
        jpFPath.add(jpParent);
        jpFPath.add(new JLabel("Head:"));
        final JTextField jtHead = new JTextField();
        jpFPath.add(jtHead);
        jpFPath.add(new JLabel("Root:"));
        final JTextField jtRoot = new JTextField();
        jpFPath.add(jtRoot);
        jpFPath.add(new JLabel("Tail:"));
        final JTextField jtTail = new JTextField();
        jpFPath.add(jtTail);
        JPanel jpButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton bNOk = new JButton("Ok");
        JButton bNCancel = new JButton("Cancel");
        bNOk.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (jtGraphName.getText().equals("")) {
                    JOptionPane.showMessageDialog(dlgNew, "You should enter graph name.");
                } else {
                    HeaderFile = jtHead.getText();
                    RootFile = jtRoot.getText();
                    TailFile = jtTail.getText();
                    NameGraph = jtGraphName.getText();
                    if (NameGraph.endsWith(".grf")) NameGraph = NameGraph.replaceAll(".grf", "");
                    PathGraph = jtGraphPath.getText() + "/" + NameGraph + "/";
                    NameGraph += ".grf";
                    File d = new File(PathGraph);
                    File f = new File(PathGraph + NameGraph);
                    try {
                        d.mkdirs();
                        f.createNewFile();
                        ModalResult = true;
                        dlgNew.dispose();
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(dlgNew, "Can't create [" + PathGraph + NameGraph + "]");
                    }
                }
            }
        });
        bNCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                ModalResult = false;
                dlgNew.dispose();
            }
        });
        jpButtons.add(bNOk);
        jpButtons.add(bNCancel);
        dlgNew.getContentPane().setLayout(new BorderLayout());
        dlgNew.getContentPane().add(jpFPath, BorderLayout.CENTER);
        dlgNew.getContentPane().add(jpButtons, BorderLayout.SOUTH);
        dlgNew.setSize(550, 350);
        Dimension winSize = dlgNew.getSize();
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        dlgNew.setLocation((scrSize.width - winSize.width) >> 1, (scrSize.height - winSize.height) >> 1);
        dlgNew.show();
    }
