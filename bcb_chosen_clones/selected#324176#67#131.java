    public HogsCustomizer() {
        m_filename = PathFinder.getCustsFile();
        m_currenttaunts = new String[10];
        m_textfields = new JTextField[10];
        m_color = new Color(255, 255, 255);
        boolean exists = (new File(m_filename)).exists();
        m_inverted = false;
        m_chooser = new JColorChooser();
        AbstractColorChooserPanel[] panels = m_chooser.getChooserPanels();
        m_chooser.removeChooserPanel(panels[0]);
        m_chooser.removeChooserPanel(panels[2]);
        m_chooser.setPreviewPanel(new JPanel());
        Reader reader = null;
        if (exists) {
            try {
                reader = new FileReader(m_filename);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Object[] options = { "Yes", "No, Thanks" };
            int n = JOptionPane.showOptionDialog(this, "You do not have a customization file in your home directory.\n                 " + "Would you like to create one?", "Hogs Customization", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            if (n == 0) {
                try {
                    FileChannel srcChannel = new FileInputStream(HogsConstants.CUSTS_TEMPLATE).getChannel();
                    FileChannel dstChannel = new FileOutputStream(m_filename).getChannel();
                    dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
                    srcChannel.close();
                    dstChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            } else {
                System.exit(0);
            }
            try {
                reader = new FileReader(m_filename);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                System.exit(0);
            }
        }
        try {
            readFromFile(reader);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        Box mainpanel = Box.createVerticalBox();
        mainpanel.add(buildTauntsPanel());
        mainpanel.add(buildMouseStylePanel());
        mainpanel.add(Box.createVerticalStrut(10));
        mainpanel.add(buildColorPanel());
        mainpanel.add(Box.createVerticalStrut(10));
        mainpanel.add(buildButtonsPanel());
        mainpanel.add(Box.createVerticalStrut(10));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainpanel);
        this.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - (this.getWidth() / 2), dim.height / 2 - (this.getHeight() / 2));
        this.setTitle("Hogs Customizer");
        this.setVisible(true);
    }
