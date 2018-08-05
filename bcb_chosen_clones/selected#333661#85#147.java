    private static synchronized JOptionPane getOptionPane() {
        if (S_optionPane == null) {
            final JTabbedPane onglets = new JTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
            final JPanel auteur = new JPanel(new BorderLayout());
            final JLabel app = new JLabel(APPLICATION_NAME, getImageIcon("icon64.png"), SwingConstants.CENTER);
            final Font ft = app.getFont();
            app.setFont(ft.deriveFont(Font.BOLD, ft.getSize() * 2.5F));
            auteur.add(app, BorderLayout.NORTH);
            final JPanel centre = new JPanel(new GridLayout(4, 1));
            final JLabel ver = new JLabel("v " + APPLICATION_VERSION, SwingConstants.CENTER);
            ver.setFont(ft.deriveFont(Font.BOLD, ft.getSize() * 1.5F));
            centre.add(ver);
            centre.add(new JLabel("© 2006-2009 - " + APPLICATION_AUTHOR, SwingConstants.CENTER));
            final JLabel traducteur = new JLabel(getI18NString("dialog.about.translater"), SwingConstants.CENTER);
            traducteur.setFont(ft.deriveFont(Font.ITALIC));
            centre.add(traducteur);
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                final JPanel panelWeb = new JPanel();
                final JButton boutonWeb = new JButton(APPLICATION_WEB);
                boutonWeb.setToolTipText(getI18NString("dialog.about.web.tooltip"));
                boutonWeb.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent pEvt) {
                        assert pEvt != null;
                        try {
                            Desktop.getDesktop().browse(new URI(APPLICATION_WEB));
                        } catch (final IOException e) {
                        } catch (final URISyntaxException e) {
                        }
                    }
                });
                panelWeb.add(boutonWeb);
                centre.add(panelWeb);
            } else {
                centre.add(new JLabel(APPLICATION_WEB, SwingConstants.CENTER));
            }
            auteur.add(centre, BorderLayout.CENTER);
            final JTextArea remerciements = new JTextArea(getI18NString("dialog.about.thanks"));
            remerciements.setFont(ft.deriveFont(Font.ITALIC, ft.getSize() * 0.9F));
            remerciements.setLineWrap(true);
            remerciements.setWrapStyleWord(true);
            remerciements.setOpaque(false);
            remerciements.setForeground(Color.DARK_GRAY);
            remerciements.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
            auteur.add(remerciements, BorderLayout.SOUTH);
            onglets.add(getI18NString("dialog.about.author"), auteur);
            onglets.add(getI18NString("dialog.about.license"), getLicensePanel());
            final TableModel modele = new AboutModel();
            final JTable proprietes = new JTable(modele);
            proprietes.getTableHeader().setReorderingAllowed(false);
            proprietes.setEnabled(false);
            proprietes.setRowSelectionAllowed(false);
            proprietes.setRowSorter(new TableRowSorter<TableModel>(modele));
            proprietes.setShowHorizontalLines(false);
            proprietes.setDefaultRenderer(Object.class, new EvenOddRowsRenderer());
            onglets.add(getI18NString("dialog.about.system"), new JScrollPane(proprietes));
            onglets.setPreferredSize(new Dimension(450, 300));
            S_optionPane = new JOptionPane(onglets, JOptionPane.PLAIN_MESSAGE);
        }
        assert S_optionPane != null;
        return S_optionPane;
    }
