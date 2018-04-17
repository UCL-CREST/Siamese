    public AboutDialog(JFrame parent) {
        super(parent, DialogMessages.about_dialog_title, true);
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        JPanel aboutPanel = new JPanel();
        aboutPanel.setLayout(new GridBagLayout());
        cp.add(aboutPanel, BorderLayout.CENTER);
        JLabel splash = new JLabel();
        splash.setIcon(ImageManager.getDefault().getSplashScreen());
        splash.setHorizontalAlignment(JTextField.CENTER);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 1;
        aboutPanel.add(splash, c);
        HyperlinkLabel websiteLink = new HyperlinkLabel(DialogMessages.website);
        websiteLink.setHorizontalAlignment(JTextField.CENTER);
        websiteLink.setHyperlinkListener(new IHyperlinkListener() {

            @Override
            public void mouseClicked(int id) {
                URI uri;
                try {
                    uri = new URI(DialogMessages.website);
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(uri);
                        } catch (IOException e) {
                            MessageUtil.addMessage("Unable to open the license file: " + e.getMessage());
                        }
                    }
                } catch (URISyntaxException e1) {
                    MessageUtil.addMessage("Unable to open the website: " + e1.getMessage());
                }
            }
        });
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 2;
        aboutPanel.add(websiteLink, c);
        JLabel aboutLabel2 = new JLabel();
        aboutLabel2.setHorizontalAlignment(JTextField.CENTER);
        aboutLabel2.setText(DialogMessages.build_number);
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 3;
        aboutPanel.add(aboutLabel2, c);
        HyperlinkLabel licenseLink = new HyperlinkLabel("Released under GNU GPL v3");
        licenseLink.setHorizontalAlignment(JTextField.CENTER);
        licenseLink.setHyperlinkListener(new IHyperlinkListener() {

            @Override
            public void mouseClicked(int id) {
                File licenseFile = new File("." + File.separator + "license.txt");
                if (!licenseFile.exists()) {
                    licenseFile = new File("." + File.separator + "deploy" + File.separator + "license.txt");
                }
                if (licenseFile.exists()) {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().open(licenseFile);
                        } catch (IOException e) {
                            MessageUtil.addMessage("Unable to open the license file: " + e.getMessage());
                        }
                    }
                } else {
                    MessageUtil.addMessage("Unable to locate the license file: " + licenseFile.getAbsolutePath());
                }
            }
        });
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 4;
        aboutPanel.add(licenseLink, c);
        HyperlinkLabel thirdPartyLink = new HyperlinkLabel("Third Party Software");
        thirdPartyLink.setHorizontalAlignment(JTextField.CENTER);
        thirdPartyLink.setHyperlinkListener(new IHyperlinkListener() {

            @Override
            public void mouseClicked(int id) {
                File readmeFile = new File("." + File.separator + "readme.txt");
                if (!readmeFile.exists()) {
                    readmeFile = new File("." + File.separator + "deploy" + File.separator + "readme.txt");
                }
                if (readmeFile.exists()) {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().open(readmeFile);
                        } catch (IOException e) {
                            MessageUtil.addMessage("Unable to open the readme file: " + e.getMessage());
                        }
                    }
                } else {
                    MessageUtil.addMessage("Unable to locate the readme file: " + readmeFile.getAbsolutePath());
                }
            }
        });
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 5;
        aboutPanel.add(thirdPartyLink, c);
        JPanel buttonPanel = new JPanel();
        cp.add(buttonPanel, BorderLayout.SOUTH);
        closeButton = new JButton();
        closeButton.setText(DialogMessages.close_button);
        closeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                AboutDialog.this.setVisible(false);
            }
        });
        buttonPanel.add(closeButton);
        this.pack();
        this.setLocation(ScreenUtil.centralise(this.getSize().width, this.getSize().height));
    }
