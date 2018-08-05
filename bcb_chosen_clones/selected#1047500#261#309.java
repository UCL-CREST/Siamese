    private void createAboutDialog() {
        aboutDialog = new JDialog(parent, "About " + Main.APP_NAME, true);
        aboutDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        JLabel versionLabel = new JLabel(Main.APP_NAME + " " + Main.APP_VERSION);
        versionLabel.setFont(versionLabel.getFont().deriveFont(versionLabel.getFont().getSize2D() * 1.5f));
        JLabel copyrightLabel = new JLabel(Main.COPYRIGHT);
        JTextArea licenseArea = new JTextArea(loadLicenseText());
        licenseArea.setEditable(false);
        licenseArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
        JScrollPane licenseScroll = new JScrollPane(licenseArea);
        licenseScroll.setPreferredSize(new Dimension(licenseScroll.getPreferredSize().width, 200));
        JLabel homepageLabel = new JLabel(Main.HOMEPAGE);
        JButton homepageButton = new JButton(Main.HOMEPAGE);
        homepageButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(Main.HOMEPAGE));
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });
        JComponent homepageComponent;
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.BROWSE)) {
            homepageComponent = homepageButton;
        } else {
            homepageComponent = homepageLabel;
        }
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                aboutDialog.setVisible(false);
            }
        });
        GroupLayout layout = new GroupLayout(aboutDialog.getContentPane());
        aboutDialog.setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.CENTER).addComponent(versionLabel).addComponent(copyrightLabel).addComponent(licenseScroll).addGroup(layout.createSequentialGroup().addComponent(homepageComponent).addGap(0, 0, Integer.MAX_VALUE).addComponent(closeButton)));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(versionLabel).addComponent(copyrightLabel).addComponent(licenseScroll, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addGroup(layout.createBaselineGroup(false, false).addComponent(homepageComponent).addComponent(closeButton)));
        aboutDialog.pack();
        aboutDialog.setResizable(false);
    }
