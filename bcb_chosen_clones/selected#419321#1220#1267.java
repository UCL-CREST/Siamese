    private JPanel createAboutPanel() {
        JPanel panel = new JPanel();
        Border border = new EmptyBorder(5, 5, 5, 5);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(border);
        JPanel pnlAbout = new JPanel();
        BoxLayout bl = new BoxLayout(pnlAbout, BoxLayout.Y_AXIS);
        pnlAbout.setLayout(bl);
        JLabel icon = new JLabel();
        icon.setIcon(new ImageIcon(this.getClass().getResource("/icon.png")));
        icon.setAlignmentX(CENTER_ALIGNMENT);
        pnlAbout.add(icon);
        JLabel version = new JLabel("Version: " + Version.getVersion() + " TestBed");
        version.setAlignmentX(CENTER_ALIGNMENT);
        pnlAbout.add(version);
        JTextPane text = new JTextPane();
        text.setEditable(false);
        try {
            text.setPage(this.getClass().getResource("/description.html"));
        } catch (IOException e) {
            text.setText("");
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
                                System.err.println("A link in the description.html is not correct: " + e.getURL());
                            } catch (IOException ex) {
                                System.err.println("Cannot navigate to link since a default program is not set or does not exist.");
                            }
                        }
                    }
                }
            }
        });
        JScrollPane scroller = new JScrollPane(text);
        pnlAbout.add(scroller);
        panel.add(pnlAbout);
        return panel;
    }
