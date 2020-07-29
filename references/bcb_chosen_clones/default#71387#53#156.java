    public Keywarden() {
        setTitle(ManifestInfo.info.product + "  V" + ManifestInfo.info.version);
        setSize(500, 250);
        try {
            setIconImage(Utils.getImageRes(this, "/images/logo.jpg"));
        } catch (Exception e2) {
        }
        final int border5 = 5;
        final double layoutPanelModules[][] = { { border5, 100, border5, TableLayout.FILL, border5, 100, border5 }, { border5, 20, 20, 20, 20, 20, 20, 50, border5 } };
        setLayout(new TableLayout(layoutPanelModules));
        JLabel labelKeywarden = new JLabel("Keywarden XML");
        add(labelKeywarden, "1,1,r,c");
        tfKeywarden = new JTextField();
        tfKeywarden.addCaretListener(new CaretListener() {

            public void caretUpdate(CaretEvent arg0) {
                if (new File(tfKeywarden.getText()).exists()) {
                    bConvert.setEnabled(true);
                }
            }
        });
        add(tfKeywarden, "3,1,f,c");
        JButton bKeywarden = new JButton("...");
        bKeywarden.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser d = new JFileChooser("Keywarden XML file");
                d.setCurrentDirectory(new File("."));
                d.setFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(File f) {
                        return f.isDirectory() || f.getName().toLowerCase().endsWith(".xml");
                    }

                    @Override
                    public String getDescription() {
                        return "*.xml";
                    }
                });
                if (d.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    tfKeywarden.setText(d.getSelectedFile().getPath());
                }
            }
        });
        add(bKeywarden, "5,1,l,c");
        JLabel labelConverted = new JLabel("Converted XML");
        add(labelConverted, "1,3,r,c");
        tfConverted = new JTextField();
        add(tfConverted, "3,3,f,c");
        JButton bConverted = new JButton("...");
        bConverted.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                JFileChooser d = new JFileChooser("Converted XML file");
                d.setCurrentDirectory(new File("."));
                d.setFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(File f) {
                        return f.isDirectory() || f.getName().toLowerCase().endsWith(".xml");
                    }

                    @Override
                    public String getDescription() {
                        return "*.xml";
                    }
                });
                if (d.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    tfConverted.setText(d.getSelectedFile().getPath());
                }
            }
        });
        add(bConverted, "5,3,l,c");
        bConvert = new JButton("Convert");
        bConvert.setEnabled(false);
        bConvert.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (tfConverted.getText().isEmpty()) {
                    String s = new File(tfKeywarden.getText()).getParent();
                    s = s + "/New_" + new File(tfKeywarden.getText()).getName();
                    tfConverted.setText(s);
                }
                convert(new File(tfKeywarden.getText()), new File(tfConverted.getText()));
            }
        });
        add(bConvert, "3,7,c,f");
        JButton bAbout = new JButton("About");
        bAbout.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new AboutBox(null, ManifestInfo.info, Global.LINKURL);
            }
        });
        add(bAbout, "5,7");
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
