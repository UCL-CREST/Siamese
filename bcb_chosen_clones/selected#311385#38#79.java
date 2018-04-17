    private AboutDialog(Window owner) {
        super(owner, Messages.getString("dialog.about.title"), ModalityType.APPLICATION_MODAL);
        this.setIconImage(Icons.ABOUT.getImage());
        this.setPreferredSize(new Dimension(450, 500));
        Container container = this.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        JLabel icon = new JLabel();
        icon.setIcon(Icons.SANDBOX_128);
        icon.setText(MessageFormat.format(Messages.getString("dialog.about.text"), Sandbox.VERSION, Version.getVersion()));
        JTextPane text = new JTextPane();
        text.setEditable(false);
        try {
            text.setPage(this.getClass().getResource(Messages.getString("dialog.about.html")));
        } catch (IOException e) {
            text.setText(Messages.getString("dialog.about.html.error"));
        }
        text.addHyperlinkListener(new HyperlinkListener() {

            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        if (desktop.isSupported(Desktop.Action.BROWSE)) {
                            try {
                                URI uri = e.getURL().toURI();
                                desktop.browse(uri);
                            } catch (URISyntaxException ex) {
                                System.err.println(MessageFormat.format(Messages.getString("dialog.about.uri.error"), e.getURL()));
                            } catch (IOException ex) {
                                System.err.println(Messages.getString("dialog.about.navigate.error"));
                            }
                        }
                    }
                }
            }
        });
        JScrollPane scroller = new JScrollPane(text);
        container.add(icon);
        container.add(scroller);
        this.pack();
    }
