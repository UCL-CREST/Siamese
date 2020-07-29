    public HelpDialog() {
        this.setTitle("A propos ...");
        this.setModal(true);
        this.add(new TitledPanel("ViDeso 3D " + Videso3D.VERSION), BorderLayout.NORTH);
        JEditorPane text = new JEditorPane("text/html", "<p align=center><b>Auteurs</b><br />" + "Bruno Spyckerelle<br />" + "Adrien Vidal<br />" + "Mickael Papail<br />" + "<br />" + "<b>Liens</b><br />" + "<a href=\"http://code.google.com/p/videso3d/wiki/Home?tm=6\">Aide en ligne</a><br />" + "<a href=\"http://code.google.com/p/videso3d/issues/list\">Signaler un bug</a><br /></p>");
        text.setEditable(false);
        text.setOpaque(false);
        text.addHyperlinkListener(new HyperlinkListener() {

            @Override
            public void hyperlinkUpdate(HyperlinkEvent evt) {
                if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED && Desktop.isDesktopSupported()) {
                    final Desktop dt = Desktop.getDesktop();
                    if (dt.isSupported(Desktop.Action.BROWSE)) {
                        try {
                            dt.browse(evt.getURL().toURI());
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        this.add(text);
        this.setPreferredSize(new Dimension(400, 240));
        this.pack();
        Toolkit tk = this.getToolkit();
        int x = (tk.getScreenSize().width - this.getWidth()) / 2;
        int y = (tk.getScreenSize().height - this.getHeight()) / 2;
        this.setLocation(x, y);
    }
