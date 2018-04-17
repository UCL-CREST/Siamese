    @Inject
    public InfoDialog(final I18n i18n, final Settings settings, final LogInfoDialog logInfoDialog, final SystemInfoDialog systemInfoDialog) {
        super("icons/information.png", settings);
        setTitle(i18n.get("menu.about"));
        setLayout(new MigLayout("wrap 2, fill"));
        JLabel iconLabel = new JLabel(icon);
        add(iconLabel, "aligny top");
        String version = Icetasks.class.getPackage().getImplementationVersion();
        JEditorPane versionLabel = new JEditorPane("text/html", "<html><p><span style=\"font-weight:bold;font-size:150%;\">" + i18n.get("app.title") + "</span><br/>" + i18n.get("app.version") + " " + (null != version ? version : "[version only shown when started from jar file]") + "<br/>Copyright 2008 Heribert Hirth<br/><br/><br/>" + i18n.get("menu.info.libraries") + "<br/><a href =\"http://commons.apache.org/\">Apache Commons</a>" + "<br/><a href =\"http://logging.apache.org/log4j/\">Apache log4j</a>" + "<br/><a href =\"http://everaldo.com/crystal/\">Crystal Project icons</a>" + "<br/><a href =\"http://code.google.com/p/google-guice/\">Google Guice</a>" + "<br/><a href =\"http://joda-time.sourceforge.net/\">Joda Time</a>" + "<br/><a href =\"http://www.miglayout.com/\">MiGLayout</a>" + "<br/><a href =\"http://www.famfamfam.com/lab/icons/silk/\">Silk Icons</a>" + "<br/><a href =\"https://substance.dev.java.net/\">Substance Java look & feel</a>" + "<br/><a href =\"http://swinglabs.org/projects.jsp\">SwingX</a>" + "</p></html>");
        StyleSheet css = ((HTMLEditorKit) versionLabel.getEditorKit()).getStyleSheet();
        css.addRule("p { margin:0; font-family:" + iconLabel.getFont().getFontName() + "; font-size:" + iconLabel.getFont().getSize() + "pt; }");
        versionLabel.setEditable(false);
        versionLabel.setOpaque(false);
        versionLabel.setBorder(null);
        versionLabel.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent event) {
                if (HyperlinkEvent.EventType.ACTIVATED.equals(event.getEventType())) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        if (desktop.isSupported(Desktop.Action.BROWSE)) {
                            try {
                                desktop.browse(event.getURL().toURI());
                            } catch (IOException e) {
                                log.error("Cannot open URL", e);
                            } catch (URISyntaxException e) {
                                log.error("Cannot open URL", e);
                            }
                        } else log.info("Clicked on info URL, but no browser support detected");
                    } else log.info("Clicked on info URL, but no browser support detected");
                }
            }
        });
        add(versionLabel, "growx 100000, growy, alignx left, aligny top");
        add(new JSeparator(), "span 2, growx");
        JButton logInfo = new JButton(i18n.get("menu.info.log"));
        add(logInfo, "split 3, span 2, wmin pref+10px");
        JButton systemInfo = new JButton(i18n.get("menu.info.system.title"));
        add(systemInfo, "wmin pref+10px");
        JButton ok = new JButton(i18n.get("dialog.ok"));
        add(ok, "tag ok");
        getRootPane().setDefaultButton(ok);
        logInfo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                logInfoDialog.setVisible(true);
            }
        });
        systemInfo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                systemInfoDialog.setVisible(true);
            }
        });
        ok.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        pack();
    }
