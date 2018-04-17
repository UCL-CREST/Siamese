    public DialogueSymbole(final JFrame jframe, final Element el, final String srcAttr) {
        super(jframe, JaxeResourceBundle.getRB().getString("symbole.Insertion"), true);
        this.jframe = jframe;
        this.el = el;
        final String nomf = el.getAttribute(srcAttr);
        boolean applet = false;
        try {
            final File dossierSymboles = new File("symboles");
            if (!dossierSymboles.exists()) {
                JOptionPane.showMessageDialog(jframe, JaxeResourceBundle.getRB().getString("erreur.SymbolesNonTrouve"), JaxeResourceBundle.getRB().getString("erreur.Erreur"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            liste = chercherImages(dossierSymboles);
        } catch (AccessControlException ex) {
            applet = true;
            try {
                final URL urlListe = DialogueSymbole.class.getClassLoader().getResource("symboles/liste.txt");
                BufferedReader in = new BufferedReader(new InputStreamReader(urlListe.openStream()));
                final ArrayList<File> listeImages = new ArrayList<File>();
                String ligne = null;
                while ((ligne = in.readLine()) != null) {
                    if (!"".equals(ligne.trim())) listeImages.add(new File("symboles/" + ligne.trim()));
                }
                liste = listeImages.toArray(new File[listeImages.size()]);
            } catch (IOException ex2) {
                LOG.error(ex2);
            }
        }
        final JPanel cpane = new JPanel(new BorderLayout());
        setContentPane(cpane);
        final GridLayout grille = new GridLayout((int) Math.ceil(liste.length / 13.0), 13, 10, 10);
        final JPanel spane = new JPanel(grille);
        cpane.add(spane, BorderLayout.CENTER);
        ichoix = 0;
        final MyMouseListener ecouteur = new MyMouseListener();
        labels = new JLabel[liste.length];
        for (int i = 0; i < liste.length; i++) {
            if (nomf != null && !"".equals(nomf) && nomf.equals(liste[i].getPath())) ichoix = i;
            URL urlIcone;
            try {
                if (applet) {
                    final URL urlListe = DialogueSymbole.class.getClassLoader().getResource("symboles/liste.txt");
                    final String baseURL = urlListe.toString().substring(0, urlListe.toString().indexOf("symboles/liste.txt"));
                    urlIcone = new URL(baseURL + liste[i].getPath());
                } else urlIcone = liste[i].toURL();
            } catch (MalformedURLException ex) {
                LOG.error(ex);
                break;
            }
            final Icon ic = new ImageIcon(urlIcone);
            final JLabel label = new JLabel(ic);
            label.addMouseListener(ecouteur);
            labels[i] = label;
            spane.add(label);
        }
        final JPanel bpane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        final JButton boutonAnnuler = new JButton(JaxeResourceBundle.getRB().getString("bouton.Annuler"));
        boutonAnnuler.addActionListener(this);
        boutonAnnuler.setActionCommand("Annuler");
        bpane.add(boutonAnnuler);
        final JButton boutonOK = new JButton(JaxeResourceBundle.getRB().getString("bouton.OK"));
        boutonOK.addActionListener(this);
        boutonOK.setActionCommand("OK");
        bpane.add(boutonOK);
        cpane.add(bpane, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(boutonOK);
        choix(ichoix);
        pack();
        if (jframe != null) {
            final Rectangle r = jframe.getBounds();
            setLocation(r.x + r.width / 4, r.y + r.height / 4);
        } else {
            final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation((screen.width - getSize().width) / 3, (screen.height - getSize().height) / 3);
        }
    }
