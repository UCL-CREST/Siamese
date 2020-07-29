    private void initComponents() {
        FormListener formListener = new FormListener();
        setBackground(UMCConstants.guiColor);
        panelMain = new JPanel();
        panelWidget = new WidgetPanel();
        JPanel xxx = new JPanel();
        xxx.setLayout(new GridBagLayout());
        xxx.setBackground(UMCConstants.guiColor);
        JLabel jl = new JLabel();
        URL url = MainPanel2.class.getResource("/com/umc/gui/resources/header.jpg");
        jl.setIcon(new ImageIcon(url));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.gridwidth = 5;
        xxx.add(jl, gbc);
        JEditorPane jep = new JEditorPane("text/html", "");
        jep.setBorder(null);
        jep.setEditable(false);
        jep.setForeground(Color.GRAY);
        jep.setText("<html><body bgcolor='#000000'>" + "<table>" + "<tr>" + "<td><font face='Arial' size='4'>" + "Welcome to the brand new <b><i>UltimateMediaCollector</i></b> Version 2.0 - Beta<br>" + "Starting with Version 2.0, UMC will now also be able to scan music and photos<br>" + "</td>" + "</tr>" + "<tr>" + "<td>" + "<li>supports movies (avi,mpg,mpeg,mkv,iso,vob,m2ts,ts,divx,wmv,dat,img,...)</li>" + "<li>supports music (mp3)</li>" + "<li>supports photos (jpg,jpeg,gif,png,bmp)</li>" + "<li>rescan</li>" + "<li>widget interface (LastMoviesScanned, Statistic, News, VersionCheck)</li>" + "<li>brandnew UMC RobG frontend(only C-200)</li>" + "<li>supports YAMJ nfo's and library.xml's</li>" + "<li>frontend known prior to V2.0, now available as native user interface (A , B and C Series)</li>" + "<li>and much more...(check out the changelog)</li>" + "</td>" + "</tr>" + "</body></html>");
        jep.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    String url = e.getURL().toString();
                    try {
                        if (StringUtils.isNotEmpty(url)) {
                            if (url.equals("xy_ungelöst")) {
                            } else {
                                if (Desktop.isDesktopSupported()) Desktop.getDesktop().browse(new URI(e.getURL().toString()));
                            }
                        }
                    } catch (IOException e1) {
                    } catch (URISyntaxException e1) {
                    }
                }
            }
        });
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridwidth = 5;
        xxx.add(jep, gbc);
        JButton jbtn1 = new JButton("New Project");
        jbtn1.setPreferredSize(new Dimension(100, 100));
        jbtn1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                GuiController.getInstance().showWizard();
            }
        });
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0;
        gbc.gridy = 1;
        xxx.add(jbtn1, gbc);
        JButton jbtn2 = new JButton();
        ImageIcon icon = new ImageIcon(getClass().getResource("/com/umc/gui/resources/search.png"));
        jbtn2.setIcon(icon);
        jbtn2.setToolTipText("Start to scan the libraries from your currently loaded configuration");
        jbtn2.setPreferredSize(new Dimension(100, 100));
        jbtn2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                GuiController.getInstance().startScan();
            }
        });
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 1;
        gbc.gridy = 1;
        xxx.add(jbtn2, gbc);
        JButton jbtn3 = new JButton();
        icon = new ImageIcon(getClass().getResource("/com/umc/gui/resources/bug.png"));
        jbtn3.setIcon(icon);
        jbtn3.setToolTipText("Report a bug");
        jbtn3.setPreferredSize(new Dimension(100, 100));
        jbtn3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Not yet available", "TODO", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 2;
        gbc.gridy = 1;
        xxx.add(jbtn3, gbc);
        JButton jbtn4 = new JButton();
        icon = new ImageIcon(getClass().getResource("/com/umc/gui/resources/nmt.png"));
        jbtn4.setIcon(icon);
        jbtn4.setToolTipText("UMC forum");
        jbtn4.setPreferredSize(new Dimension(100, 100));
        jbtn4.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI("http://popcornforum.de/forumdisplay.php?fid=67"));
                    } catch (Exception exc) {
                        JOptionPane.showMessageDialog(null, "Could not acces http://popcornforum.de/forumdisplay.php?fid=67", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 3;
        gbc.gridy = 1;
        xxx.add(jbtn4, gbc);
        JButton jbtn5 = new JButton();
        icon = new ImageIcon(getClass().getResource("/com/umc/gui/resources/paypal.png"));
        jbtn5.setIcon(icon);
        jbtn5.setToolTipText("The UMC project is a free available software. It is a free time project in which we are spending a lot of hours. If you would like to honor this work and give pleasure to the UMC team , please donate a free amount via PayPal.");
        jbtn5.setPreferredSize(new Dimension(100, 100));
        jbtn5.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Not yet available", "TODO", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 4;
        gbc.gridy = 1;
        xxx.add(jbtn5, gbc);
        panelMain.setBackground(UMCConstants.guiColor);
        GroupLayout panelMainLayout = new GroupLayout(panelMain);
        panelMain.setLayout(panelMainLayout);
        panelMainLayout.setHorizontalGroup(panelMainLayout.createParallelGroup(GroupLayout.LEADING).add(panelMainLayout.createSequentialGroup().addContainerGap().add(xxx, GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE).addContainerGap()));
        panelMainLayout.setVerticalGroup(panelMainLayout.createParallelGroup(GroupLayout.LEADING).add(panelMainLayout.createSequentialGroup().addContainerGap().add(xxx, GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE).addContainerGap()));
        panelWidget.setPreferredSize(new Dimension(360, 528));
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(GroupLayout.LEADING).add(panelMain, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(LayoutStyle.RELATED).add(panelWidget, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.LEADING).add(GroupLayout.TRAILING, layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(GroupLayout.TRAILING).add(GroupLayout.LEADING, panelWidget, GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE).add(layout.createSequentialGroup().add(panelMain, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(LayoutStyle.RELATED))).addContainerGap()));
    }
