    private RevisionVisualization(BufferedReader r, JApplet appletContext) throws IOException {
        super(Compilation.getName());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        TreeSet<Category> allCategories = new TreeSet<Category>();
        Pattern TAB = Pattern.compile("[\t]");
        String line = r.readLine();
        if (line == null) throw new IOException("Header line missing");
        String tokens[] = TAB.split(line);
        this.header = new String[tokens.length - 4];
        System.arraycopy(tokens, 4, this.header, 0, this.header.length);
        if (tokens.length < 6) throw new IOException("bad number of columns");
        while ((line = r.readLine()) != null) {
            tokens = TAB.split(line);
            if (tokens.length != this.header.length + 4) throw new IOException("illegale number of columns in " + line);
            Figure f = new Figure();
            f.page = new Page(tokens[0]);
            String cats[] = tokens[1].split("[|]");
            for (String cat : cats) {
                f.categories.add(new Category(cat));
            }
            allCategories.addAll(f.categories);
            f.userCount = Integer.parseInt(tokens[2]);
            f.revisionCount = Integer.parseInt(tokens[3]);
            f.sizes = new int[header.length];
            f.revisions = new int[header.length];
            for (int i = 4; i < tokens.length; ++i) {
                int j = tokens[i].indexOf(";");
                f.sizes[i - 4] = Integer.parseInt(tokens[i].substring(0, j));
                f.revisions[i - 4] = Integer.parseInt(tokens[i].substring(j + 1));
            }
            this.figures.add(f);
        }
        JPanel mainPane = new JPanel(new BorderLayout());
        setContentPane(mainPane);
        JPanel left = new JPanel(new GridLayout(0, 1, 2, 2));
        mainPane.add(left, BorderLayout.WEST);
        JPanel pane1 = new JPanel(new BorderLayout());
        pane1.setPreferredSize(new Dimension(200, 200));
        left.add(pane1);
        pane1.setBorder(new TitledBorder("Pages (" + this.figures.size() + ")"));
        this.pageList = new JList(new Vector<Figure>(this.figures));
        this.pageList.setCellRenderer(new DefaultListCellRenderer() {

            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                this.setText(Figure.class.cast(value).page.getLocalName());
                return c;
            }
        });
        this.pageList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scroll = new JScrollPane(this.pageList);
        scroll.setPreferredSize(new Dimension(200, 200));
        pane1.add(scroll, BorderLayout.CENTER);
        JPanel pane2 = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        pane1.add(pane2, BorderLayout.SOUTH);
        pane2.add(new JButton(new AbstractAction("Clear") {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                pageList.getSelectionModel().clearSelection();
            }
        }));
        pane1 = new JPanel(new BorderLayout());
        left.add(pane1);
        pane1.setBorder(new TitledBorder("Categories (" + allCategories.size() + ")"));
        this.catList = new JList(new Vector<Category>(allCategories));
        this.catList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        scroll = new JScrollPane(this.catList);
        scroll.setPreferredSize(new Dimension(200, 200));
        pane1.add(scroll, BorderLayout.CENTER);
        pane2 = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        pane1.add(pane2, BorderLayout.SOUTH);
        pane2.add(new JButton(new AbstractAction("Clear") {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                catList.getSelectionModel().clearSelection();
            }
        }));
        pane1 = new JPanel(new BorderLayout());
        left.add(pane1);
        this.treeGroup = new JTree(buildTree());
        pane1.setBorder(new TitledBorder("Groups"));
        scroll = new JScrollPane(this.treeGroup);
        scroll.setPreferredSize(new Dimension(200, 200));
        pane1.add(scroll, BorderLayout.CENTER);
        pane2 = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        pane1.add(pane2, BorderLayout.SOUTH);
        pane2.add(new JButton(new AbstractAction("Clear") {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                treeGroup.getSelectionModel().clearSelection();
            }
        }));
        this.drawingArea = new JPanel(null, false) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintDrawingArea(Graphics2D.class.cast(g));
            }

            @Override
            public String getToolTipText(MouseEvent event) {
                Figure f = getFigureAt(event.getX(), event.getY());
                if (f == null) return null;
                int i = (int) (((header.length - 1) / (double) drawingArea.getWidth()) * event.getX());
                if (i >= header.length) return f.page.getLocalName();
                StringBuilder b = new StringBuilder("<html><body>");
                b.append("<b>").append(XMLUtilities.escape(f.page.getLocalName())).append("</b>");
                b.append("<ul>");
                b.append("<li>").append(XMLUtilities.escape(header[i])).append("</li>");
                b.append("<li>Revisions: ").append(f.revisions[i]).append("</li>");
                b.append("<li>Sizes: ").append(f.sizes[i]).append("</li>");
                b.append("</ul>");
                b.append("</body></html>");
                return b.toString();
            }
        };
        MouseAdapter mouse = new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                highlitedFigure = null;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                drawHigLightedFigure();
                highlitedFigure = null;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Figure f = getFigureAt(e.getX(), e.getY());
                if (f == highlitedFigure) return;
                if (highlitedFigure != null) drawHigLightedFigure();
                highlitedFigure = f;
                drawHigLightedFigure();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Figure f = getFigureAt(e.getX(), e.getY());
                if (f == null) return;
                if (!(e.isPopupTrigger() || e.isControlDown())) return;
                JPopupMenu popup = new JPopupMenu();
                JMenuItem menu = new JMenuItem(new ObjectAction<Page>(f.page, "Open " + f.page) {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String uri = Wikipedia.BASE + "/wiki/" + getObject().getQNameEncoded();
                        try {
                            if (RevisionVisualization.this.appletContext == null) {
                                Desktop d = Desktop.getDesktop();
                                d.browse(new URI(uri));
                            } else {
                                RevisionVisualization.this.appletContext.getAppletContext().showDocument(new URL(uri), "_" + System.currentTimeMillis());
                            }
                        } catch (Exception err) {
                            ThrowablePane.show(RevisionVisualization.this, err);
                        }
                    }
                });
                menu.setEnabled(RevisionVisualization.this.appletContext == null && Desktop.isDesktopSupported());
                popup.add(menu);
                popup.show(drawingArea, e.getX(), e.getY());
            }
        };
        this.drawingArea.addMouseListener(mouse);
        this.drawingArea.addMouseMotionListener(mouse);
        this.drawingArea.setToolTipText("");
        this.drawingArea.setOpaque(true);
        this.drawingArea.setBackground(Color.WHITE);
        mainPane.add(this.drawingArea, BorderLayout.CENTER);
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEADING));
        mainPane.add(bottom, BorderLayout.SOUTH);
        useRevisionInsteadOfSize = new JCheckBox("Revisions");
        bottom.add(useRevisionInsteadOfSize);
        useRevisionInsteadOfSize.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dirty = true;
                drawingArea.repaint();
            }
        });
        this.pageList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                dirty = true;
                drawingArea.repaint();
            }
        });
        this.catList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                dirty = true;
                drawingArea.repaint();
            }
        });
        this.drawingArea.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                dirty = true;
                drawingArea.repaint();
            }
        });
        this.treeGroup.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                dirty = true;
                drawingArea.repaint();
            }
        });
        SwingUtils.setFontSize(left, 10);
        JMenuBar bar = new JMenuBar();
        setJMenuBar(bar);
        JMenu menu = new JMenu("File");
        bar.add(menu);
        menu.add(new AbstractAction("About") {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(RevisionVisualization.this, Compilation.getLabel());
            }
        });
        menu.add(new AbstractAction("About me") {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(RevisionVisualization.this, "Pierre Lindenbaum PhD. " + Me.MAIL + " " + Me.WWW);
            }
        });
        menu.add(new JSeparator());
        AbstractAction action = new AbstractAction("Save as SVG") {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(PreferredDirectory.getPreferredDirectory());
                if (chooser.showSaveDialog(RevisionVisualization.this) != JFileChooser.APPROVE_OPTION) return;
                File f = chooser.getSelectedFile();
                if (f == null || (f.exists() && JOptionPane.showConfirmDialog(RevisionVisualization.this, f.toString() + "exists. Overwrite ?", "Overwrite ?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null) != JOptionPane.OK_OPTION)) {
                    return;
                }
                PreferredDirectory.setPreferredDirectory(f);
                try {
                    PrintWriter out = new PrintWriter(f);
                    saveAsSVG(out);
                    out.flush();
                    out.close();
                } catch (Exception e2) {
                    ThrowablePane.show(RevisionVisualization.this, e2);
                }
            }
        };
        menu.add(action);
        action.setEnabled(RevisionVisualization.this.appletContext == null);
        menu.add(new AbstractAction("Quit") {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                RevisionVisualization.this.setVisible(false);
                RevisionVisualization.this.dispose();
            }
        });
        Collections.sort(this.figures, compareOnRevisions);
    }
