    public AboutDialog() {
        setTitle("About " + TrackPlanMain.APP_NAME);
        setResizable(false);
        setBounds(100, 100, 536, 318);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 530, 0 };
        gridBagLayout.rowHeights = new int[] { 243, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
        getContentPane().setLayout(gridBagLayout);
        JPanel panel = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.weightx = 1.0;
        gbc_panel.insets = new Insets(0, 0, 5, 0);
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 0;
        gbc_panel.gridy = 0;
        getContentPane().add(panel, gbc_panel);
        panel.setLayout(new GridLayout(0, 1, 0, 0));
        {
            JPanel panel_1 = new JPanel() {

                private static final long serialVersionUID = 1L;

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, this.getWidth(), this.getHeight());
                    g.drawImage(ABOUT_LOGO.getImage(), (this.getWidth() / 2) - (ABOUT_LOGO.getIconWidth() / 2), (this.getHeight() / 2) - (ABOUT_LOGO.getIconHeight() / 2), null);
                }
            };
            panel.add(panel_1);
        }
        {
            JPanel panel_1 = new JPanel();
            panel.add(panel_1);
            GridBagLayout gbl_panel_1 = new GridBagLayout();
            gbl_panel_1.columnWidths = new int[] { 0, 0 };
            gbl_panel_1.rowHeights = new int[] { 0, 0, 0, 0 };
            gbl_panel_1.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
            gbl_panel_1.rowWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
            panel_1.setLayout(gbl_panel_1);
            {
                JLabel lblVersion = new JLabel("Version " + TrackPlanMain.MAJOR_VERSION + "." + TrackPlanMain.MINOR_VERSION);
                lblVersion.setFont(new Font("Segoe UI", Font.BOLD, 12));
                GridBagConstraints gbc_lblVersion = new GridBagConstraints();
                gbc_lblVersion.ipady = 5;
                gbc_lblVersion.insets = new Insets(0, 0, 5, 0);
                gbc_lblVersion.gridx = 0;
                gbc_lblVersion.gridy = 0;
                panel_1.add(lblVersion, gbc_lblVersion);
            }
            {
                JLabel lblWwwtrackplanorg_1 = new JLabel("www.trackplan.org");
                lblWwwtrackplanorg_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                lblWwwtrackplanorg_1.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent arg0) {
                        try {
                            if (Desktop.isDesktopSupported()) {
                                Desktop desktop = Desktop.getDesktop();
                                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                                    URI website = new URI(TrackPlanMain.APP_WEBSITE);
                                    Desktop.getDesktop().browse(website);
                                }
                            }
                        } catch (Exception e) {
                            return;
                        }
                    }
                });
                lblWwwtrackplanorg_1.setForeground(Color.BLUE);
                GridBagConstraints gbc_lblWwwtrackplanorg_1 = new GridBagConstraints();
                gbc_lblWwwtrackplanorg_1.insets = new Insets(0, 0, 5, 0);
                gbc_lblWwwtrackplanorg_1.gridx = 0;
                gbc_lblWwwtrackplanorg_1.gridy = 1;
                panel_1.add(lblWwwtrackplanorg_1, gbc_lblWwwtrackplanorg_1);
            }
            {
                JScrollPane scrollPane = new JScrollPane();
                GridBagConstraints gbc_scrollPane = new GridBagConstraints();
                gbc_scrollPane.ipadx = 3;
                gbc_scrollPane.fill = GridBagConstraints.BOTH;
                gbc_scrollPane.gridx = 0;
                gbc_scrollPane.gridy = 2;
                panel_1.add(scrollPane, gbc_scrollPane);
                {
                    JTextArea textArea = new JTextArea("");
                    textArea.setWrapStyleWord(true);
                    textArea.setLineWrap(true);
                    textArea.setText(aboutText);
                    textArea.setEditable(false);
                    textArea.setCaretPosition(0);
                    scrollPane.setViewportView(textArea);
                }
            }
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            GridBagConstraints gbc_buttonPane = new GridBagConstraints();
            gbc_buttonPane.anchor = GridBagConstraints.SOUTH;
            gbc_buttonPane.fill = GridBagConstraints.HORIZONTAL;
            gbc_buttonPane.gridx = 0;
            gbc_buttonPane.gridy = 1;
            getContentPane().add(buttonPane, gbc_buttonPane);
            {
                JButton cancelButton = new JButton("Close");
                cancelButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        dialogRef.setVisible(false);
                        dispose();
                    }
                });
                {
                    JPanel panel_1 = new JPanel();
                    buttonPane.add(panel_1);
                    panel_1.setLayout(null);
                    {
                        JLabel lblWwwtrackplanorg = new JLabel("www.trackplan.org");
                        lblWwwtrackplanorg.setBounds(0, 0, 100, 16);
                        panel_1.add(lblWwwtrackplanorg);
                        lblWwwtrackplanorg.setForeground(Color.BLUE);
                        lblWwwtrackplanorg.setHorizontalAlignment(SwingConstants.LEFT);
                    }
                }
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }
