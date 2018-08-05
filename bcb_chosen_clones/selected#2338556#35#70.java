    public MeldungsLeiste() {
        setLayout(new BorderLayout(5, 5));
        hilfeButton = new JButton("Hilfe");
        hilfeButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    try {
                        java.awt.Desktop.getDesktop().open(new File("resources/hilfe/index.html"));
                    } catch (final IOException ex) {
                        System.err.println("Die Hilfeseite von BAUS! konnte nicht gefunden werden");
                    }
                }
            }
        });
        westPanel.add(hilfeButton);
        auftragButton = new JButton("Auftrag");
        auftragButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                    try {
                        java.awt.Desktop.getDesktop().open(new File("auftraege/test.pdf"));
                    } catch (final IOException ex) {
                        System.err.println("Der Auftrag konnte nicht angezeigt werden.");
                    }
                }
            }
        });
        westPanel.add(auftragButton);
        westPanel.add(new Uhr());
        add(westPanel, BorderLayout.WEST);
        meldungen = new JTextArea(4, 40);
        meldungen.setEditable(false);
        add(new JScrollPane(meldungen), BorderLayout.CENTER);
    }
