    public AboutDialog(final Frame owner) {
        super(owner);
        setAlwaysOnTop(true);
        final FormLayout layout = new FormLayout("20dlu, 150dlu, 150dlu, 20dlu", "10dlu, 120dlu, 80dlu, 60dlu, 20dlu, 20dlu, 10dlu");
        setLayout(layout);
        final CellConstraints cc = new CellConstraints();
        JLabel img = new JLabel();
        img.setIcon(ResourceLoader.createIconFromLocalResource(CorePlugin.class, Images.EWORLD_LOGO_IMAGE));
        add(img, cc.rchw(2, 2, 1, 2, CellConstraints.TOP, CellConstraints.CENTER));
        img = new JLabel();
        img.setIcon(ResourceLoader.createIconFromLocalResource(CorePlugin.class, Images.HPI_BIG_IMAGE));
        add(img, cc.rchw(4, 3, 2, 1, CellConstraints.TOP, CellConstraints.RIGHT));
        final JLabel label = new JLabel();
        label.setText("<html><body><b>eWorld is based on the work of:</b>" + "<ul>" + "<li>Martin Beck, Sebastian Enderlein, Christian Holz, Bernd Schaeufele, Martin Wolf (winter 2007/08)</li>" + "<li>Frank Huxol, Marco Helmich, Nico Naumann, David Rieck, Jonas Truemper (summer 2008)</li>" + "<li>Lutz Gericke, Matthias Kleine, Philipp Maschke, Gerald Toepper (winter 2008/09)</li>" + "<li>Stefan Reichel (summer 2009)</li>" + "<li>Markus Behrens, Thomas Beyhl, Martin Czuchra, Philipp Eichhorn, Eyk Kny, Keven Richly, Thomas Schulz, Florian Thomas (winter 2009/10)</li>" + "<li>Martin Boissier, Dustin Glaeser, Franz Goerke, David Jaeger, Robert Kornmesser, Henry Kraeplin, Mike Nagora, Ole Rienow, Patrick Schilf, Gary Yao (summer 2010)</li>" + "<li>Egidijus Gircys, Anton Gulenko, Uwe Hartmann, Ingo Jaeckel, Christian Kieschnick, Marvin Killing, Sebastian Klose, Frederik Leidloff, Martin Linkhorst, Paul Roemer, Stefan Schaefer, Christian Wiggert (winter 2010/11)</li>" + "</ul>" + "<p><b>Adviser:</b><br>" + "Bjoern Schuenemann</p><br>" + "<p><b>Note:</b><br>This programm uses source code from JOSM.</p><br>" + "<p><b>WWW:</b><br></p></body></html>");
        add(label, cc.rchw(3, 2, 2, 2, CellConstraints.BOTTOM, CellConstraints.DEFAULT));
        final String linkText = "<html><body><a href=\"http://eworld.sourceforge.net/\">http://eworld.sourceforge.net/</a></body></html>";
        final JLabel link = new HyperlinkLabel(linkText, new HyperlinkLabel.OnClick() {

            public void onClickDo(final MouseEvent me) {
                if (Desktop.isDesktopSupported()) {
                    final Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(new URI("http://eworld.sourceforge.net"));
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        add(link, cc.rchw(5, 2, 1, 1, CellConstraints.TOP, CellConstraints.DEFAULT));
        final JButton button = new JButton("Close");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                AboutDialog.this.setVisible(false);
            }
        });
        add(button, cc.rchw(6, 2, 1, 2, CellConstraints.CENTER, CellConstraints.CENTER));
        setTitle("About eWorld");
        setIconImage(ResourceLoader.createIconFromLocalResource(CorePlugin.class, Images.ABOUT_IMAGE).getImage());
        pack();
        setModal(true);
        setResizable(false);
        final Dimension dialogSize = getSize();
        final Dimension ownerSize = owner.getSize();
        setLocation((ownerSize.width - dialogSize.width) / 2, (ownerSize.height - dialogSize.height) / 2);
    }
