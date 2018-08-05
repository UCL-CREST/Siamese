    public About(JFrame parent) {
        super(parent);
        setTitle("About JSystem");
        setIconImage(ImageCenter.getInstance().getAwtImage(ImageCenter.ICON_JSYSTEM));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setLocation(screenWidth / 3, screenHeight / 3);
        setModal(true);
        setResizable(false);
        JPanel panel = (JPanel) getContentPane();
        ImageIcon leftImage = ImageCenter.getInstance().getImage(ImageCenter.ABOUT_DIALOG_LEFT_IMAGE);
        JPanel bgPanel = jsystem.utils.SwingUtils.getJPannelWithLeftBgImage(leftImage);
        bgPanel.setLayout(new BorderLayout());
        bgPanel.setBackground(new Color(0xf6, 0xf6, 0xf6));
        panel.add(bgPanel);
        ImageIcon logoImage = ImageCenter.getInstance().getImage(ImageCenter.ABOUT_DIALOG_LOGO);
        JLabel logoImageLable = new JLabel(logoImage);
        logoImageLable.setOpaque(false);
        logoImageLable.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        bgPanel.add(logoImageLable, BorderLayout.PAGE_START);
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.setOpaque(false);
        labelPanel.setBorder(BorderFactory.createEmptyBorder(4, 32, 4, 8));
        try {
            version = ClassSearchUtil.getPropertyFromClassPath("META-INF/jsystemApp.build.properties", "jversion");
        } catch (Exception e) {
            log.log(Level.WARNING, "Failed getting client version: " + e.getMessage());
        }
        JLabel versionLabel = new JLabel("Version: " + version);
        versionLabel.setOpaque(false);
        versionLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 2, 4));
        versionLabel.setFont(new Font("sansserif", Font.BOLD, 16));
        labelPanel.add(versionLabel);
        final JLabel copyrightLabel = new JLabel("<html>� Copyright 2005-2010 <a href=\"www.ignissoft.com\">Ignis Software Tools Ltd</a>. All rights reserved.</html>");
        copyrightLabel.setOpaque(false);
        copyrightLabel.setBorder(BorderFactory.createEmptyBorder(2, 4, 8, 4));
        copyrightLabel.setFont(new Font("sansserif", Font.PLAIN, 12));
        labelPanel.add(copyrightLabel);
        copyrightLabel.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent me) {
                copyrightLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent me) {
                copyrightLabel.setCursor(Cursor.getDefaultCursor());
            }

            public void mouseClicked(MouseEvent me) {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(new URI("http://www.ignissoft.com"));
                    } catch (Exception e) {
                        log.log(Level.WARNING, "Failed openning browser to Ignis website: " + e.getMessage());
                    }
                }
            }
        });
        JLabel contributionLable1 = new JLabel("JSystem is developed using other open source projects.");
        contributionLable1.setOpaque(false);
        contributionLable1.setBorder(BorderFactory.createEmptyBorder(8, 4, 2, 4));
        contributionLable1.setFont(new Font("sansserif", Font.PLAIN, 12));
        labelPanel.add(contributionLable1);
        JLabel contributionLable2 = new JLabel("For a complete list go to");
        contributionLable2.setOpaque(false);
        contributionLable2.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        contributionLable2.setFont(new Font("sansserif", Font.PLAIN, 12));
        labelPanel.add(contributionLable2);
        final JLabel contributionLable3 = new JLabel("<html><a href=\"http://trac.jsystemtest.org/wiki/DetailedOSProjectsList\">http://trac.jsystemtest.org</a></html>");
        contributionLable3.setOpaque(false);
        contributionLable3.setBorder(BorderFactory.createEmptyBorder(2, 4, 16, 4));
        contributionLable3.setFont(new Font("sansserif", Font.PLAIN, 12));
        labelPanel.add(contributionLable3);
        contributionLable3.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent me) {
                contributionLable3.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent me) {
                contributionLable3.setCursor(Cursor.getDefaultCursor());
            }

            public void mouseClicked(MouseEvent me) {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(new URI("http://trac.jsystemtest.org/wiki/DetailedOSProjectsList"));
                    } catch (Exception e) {
                        log.log(Level.WARNING, "Failed openning browser to JSystem website: " + e.getMessage());
                    }
                }
            }
        });
        String customerProduct = JSystemProperties.getInstance().getPreference(FrameworkOptions.CUSTOMER_PRODUCT);
        if (customerProduct != null) {
            String customerProductList[] = customerProduct.split(CommonResources.DELIMITER);
            labelPanel.add(new JSeparator());
            JLabel customerLabel = new JLabel("Customer information:");
            customerLabel.setOpaque(false);
            customerLabel.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 4));
            customerLabel.setFont(new Font("sansserif", Font.PLAIN, 12));
            labelPanel.add(customerLabel);
            JLabel customerLabels[] = new JLabel[customerProductList.length];
            for (int i = 0; i < customerProductList.length; i++) {
                customerLabels[i] = new JLabel(customerProductList[i]);
                customerLabels[i].setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
                customerLabels[i].setFont(new Font("sansserif", Font.PLAIN, 12));
                labelPanel.add(customerLabels[i]);
            }
        }
        bgPanel.add(labelPanel, BorderLayout.CENTER);
        JButton closeButton = new JButton("Close");
        closeButton.setOpaque(false);
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JPanel closeButtonPanel = new JPanel();
        closeButtonPanel.setOpaque(false);
        closeButtonPanel.add(closeButton);
        closeButtonPanel.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 4));
        bgPanel.add(closeButtonPanel, BorderLayout.PAGE_END);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        pack();
    }
