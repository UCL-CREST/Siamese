    protected final void showMsg(final String msg, final boolean quit) {
        final JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        final JImage im = new JImage(new ImageIcon(this.getClass().getResource("error.png")));
        final JLabel l = new JLabel("Une erreur est survenue");
        l.setFont(l.getFont().deriveFont(Font.BOLD));
        final JLabel lError = new JLabel(msg);
        final JTextArea textArea = new JTextArea();
        textArea.setFont(textArea.getFont().deriveFont(11f));
        c.gridheight = 3;
        p.add(im, c);
        c.insets = new Insets(2, 4, 2, 4);
        c.gridheight = 1;
        c.gridx++;
        c.weightx = 1;
        c.gridwidth = 2;
        p.add(l, c);
        c.gridy++;
        p.add(lError, c);
        c.gridy++;
        p.add(new JLabel("Il s'agit probablement d'une mauvaise configuration ou installation du logiciel."), c);
        c.gridx = 0;
        c.gridwidth = 3;
        c.gridy++;
        c.weighty = 0;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy++;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        final Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        final boolean browseSupported = desktop != null && desktop.isSupported(Action.BROWSE);
        if (ForumURL != null) {
            final javax.swing.Action communityAction;
            if (browseSupported) {
                communityAction = new AbstractAction("Consulter le forum") {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            desktop.browse(new URI(ForumURL));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                };
            } else {
                communityAction = new AbstractAction("Copier l'adresse du forum") {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        copyToClipboard(ForumURL);
                    }
                };
            }
            p.add(new JButton(communityAction), c);
        }
        c.weightx = 0;
        c.gridx++;
        final javax.swing.Action supportAction;
        if (browseSupported) supportAction = new AbstractAction("Contacter l'assistance") {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    desktop.browse(URI.create(ILM_CONTACT));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }; else supportAction = new AbstractAction("Copier l'adresse de l'assistance") {

            @Override
            public void actionPerformed(ActionEvent e) {
                copyToClipboard(ILM_CONTACT);
            }
        };
        p.add(new JButton(supportAction), c);
        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 0, 0, 0);
        p.add(new JSeparator(), c);
        c.gridx = 0;
        c.gridwidth = 3;
        c.gridy++;
        c.insets = new Insets(2, 4, 2, 4);
        p.add(new JLabel("Détails de l'erreur:"), c);
        c.insets = new Insets(0, 0, 0, 0);
        c.gridy++;
        String message = this.getCause() == null ? null : this.getCause().getMessage();
        if (message == null) {
            message = msg;
        } else {
            message = msg + "\n\n" + message;
        }
        message += "\n";
        message += getTrace();
        textArea.setText(message);
        textArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.getViewport().setMinimumSize(new Dimension(200, 300));
        c.weighty = 1;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy++;
        p.add(scroll, c);
        c.gridy++;
        c.fill = GridBagConstraints.NONE;
        c.weighty = 0;
        c.insets = new Insets(2, 4, 2, 4);
        final JButton buttonClose = new JButton("Fermer");
        p.add(buttonClose, c);
        final Window window = this.comp == null ? null : SwingUtilities.getWindowAncestor(this.comp);
        final JDialog f;
        if (window instanceof Frame) {
            f = new JDialog((Frame) window, "Erreur", true);
        } else {
            f = new JDialog((Dialog) window, "Erreur", true);
        }
        f.setContentPane(p);
        f.pack();
        f.setSize(580, 680);
        f.setMinimumSize(new Dimension(380, 380));
        f.setLocationRelativeTo(this.comp);
        final ActionListener al = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (quit) {
                    System.exit(1);
                } else {
                    f.dispose();
                }
            }
        };
        buttonClose.addActionListener(al);
        f.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                al.actionPerformed(null);
            }
        });
        f.setVisible(true);
    }
