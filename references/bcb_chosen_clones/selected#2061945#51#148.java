    private void initComponents() {
        setIconImage((resourceMap.getImageIcon("window.icon")).getImage());
        closeButton = new JButton();
        JLabel appTitleLabel = new JLabel();
        JLabel versionLabel = new JLabel();
        JLabel appVersionLabel = new JLabel();
        JLabel vendorLabel = new JLabel();
        JLabel appVendorLabel = new JLabel();
        JLabel homepageLabel = new JLabel();
        JLabel appHomepageLabel = new JLabel();
        JLabel appDescLabel = new JLabel();
        JLabel imageLabel = new JLabel();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(resourceMap.getString("title"));
        setModal(true);
        setName("aboutBox");
        setResizable(false);
        closeButton.setAction(actionMap.get("closeAboutBox"));
        closeButton.setName("closeButton");
        appTitleLabel.setFont(appTitleLabel.getFont().deriveFont(appTitleLabel.getFont().getStyle() | Font.BOLD, appTitleLabel.getFont().getSize() + 4));
        appTitleLabel.setText(resourceMap.getString("Application.title"));
        appTitleLabel.setName("appTitleLabel");
        versionLabel.setFont(versionLabel.getFont().deriveFont(versionLabel.getFont().getStyle() | Font.BOLD));
        versionLabel.setText(resourceMap.getString("versionLabel.text"));
        versionLabel.setName("versionLabel");
        appVersionLabel.setText(resourceMap.getString("Application.version"));
        appVersionLabel.setName("appVersionLabel");
        vendorLabel.setFont(vendorLabel.getFont().deriveFont(vendorLabel.getFont().getStyle() | Font.BOLD));
        vendorLabel.setText(resourceMap.getString("vendorLabel.text"));
        vendorLabel.setName("vendorLabel");
        appVendorLabel.setText(resourceMap.getString("Application.vendor"));
        appVendorLabel.setName("appVendorLabel");
        homepageLabel.setFont(homepageLabel.getFont().deriveFont(homepageLabel.getFont().getStyle() | Font.BOLD));
        homepageLabel.setText(resourceMap.getString("homepageLabel.text"));
        homepageLabel.setName("homepageLabel");
        appHomepageLabel.setText(resourceMap.getString("appHomepageLabel.text"));
        appHomepageLabel.setName("appHomepageLabel");
        appHomepageLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent me) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI(resourceMap.getString("Application.homepage")));
                    } catch (URISyntaxException urise) {
                        logger.log(Level.WARNING, "Incorrect URI", urise);
                    } catch (IOException ioe) {
                        logger.log(Level.WARNING, "General IO Error", ioe);
                    }
                }
            }
        });
        appHomepageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        appDescLabel.setText(resourceMap.getString("appDescLabel.text"));
        appDescLabel.setName("appDescLabel");
        imageLabel.setIcon(resourceMap.getIcon("imageLabel.icon"));
        imageLabel.setName("imageLabel");
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        c.add(new JPanel(), BorderLayout.NORTH);
        c.add(imageLabel, BorderLayout.WEST);
        JPanel d = new JPanel(new BorderLayout());
        JPanel titleAndDesc = new JPanel(new GridLayout(2, 1));
        titleAndDesc.add(appTitleLabel);
        titleAndDesc.add(appDescLabel);
        d.add(titleAndDesc, BorderLayout.NORTH);
        GridBagLayout gb = new GridBagLayout();
        JPanel e = new JPanel(gb);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbAdd(e, gb, gbc, versionLabel);
        gbAdd(e, gb, gbc, appVersionLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbAdd(e, gb, gbc, new JPanel());
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbAdd(e, gb, gbc, vendorLabel);
        gbAdd(e, gb, gbc, appVendorLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbAdd(e, gb, gbc, new JPanel());
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbAdd(e, gb, gbc, homepageLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbAdd(e, gb, gbc, appHomepageLabel);
        d.add(e, BorderLayout.CENTER);
        JPanel f = new JPanel(new GridLayout(1, 4));
        f.add(new JPanel());
        f.add(new JPanel());
        f.add(new JPanel());
        f.add(closeButton);
        d.add(f, BorderLayout.SOUTH);
        c.add(d, BorderLayout.CENTER);
        c.add(new JPanel(), BorderLayout.EAST);
        c.add(new JPanel(), BorderLayout.SOUTH);
        setPreferredSize(new Dimension(450, 200));
        pack();
    }
