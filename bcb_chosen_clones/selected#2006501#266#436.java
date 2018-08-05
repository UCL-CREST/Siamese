    private Graph4NCBI(Vector<Graph> graphs) throws ClassNotFoundException {
        super("Graph4NCBI");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.graphs.addAll(graphs);
        for (Graph g : this.graphs) {
            for (Node n : g.getNodes()) {
                Icon icn = new Icon(n);
                this.icons.addElement(icn);
                this.node2icon.put(n, icn);
            }
        }
        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                SwingUtils.center(e.getWindow(), 100);
                focusGraph(Graph4NCBI.this.graphs.firstElement());
            }

            @Override
            public void windowClosing(WindowEvent e) {
                doMenuClose();
            }
        });
        JPanel topPane = new JPanel(new FlowLayout(FlowLayout.LEADING));
        contentPane.add(topPane, BorderLayout.NORTH);
        topPane.add(new JButton(this.backwardHistoryAction = new AbstractAction("Back") {

            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                setHistoryIndex(Graph4NCBI.this.position_in_history - 1);
            }
        }));
        topPane.add(new JButton(this.forwardHistoryAction = new AbstractAction("Next") {

            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                setHistoryIndex(Graph4NCBI.this.position_in_history + 1);
            }
        }));
        this.historyStack = new Vector<Icon>();
        this.forwardHistoryAction.setEnabled(false);
        this.backwardHistoryAction.setEnabled(false);
        this.mainLabel = new JLabel();
        this.mainLabel.setFont(new Font("Helvetica", Font.BOLD, 18));
        this.mainLabel.setForeground(Color.BLUE);
        topPane.add(this.mainLabel);
        JMenuBar bar = new JMenuBar();
        setJMenuBar(bar);
        JMenu menu = new JMenu("File");
        bar.add(menu);
        menu.add(new AbstractAction("About") {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Graph4NCBI.this, Compilation.getLabel() + " Pierre Lindenbaum PhD 2008. plindenbaum@yahoo.fr");
            }
        });
        menu.add(new JSeparator());
        menu.add(new AbstractAction("Quit") {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                doMenuClose();
            }
        });
        menu = new JMenu("Graph");
        bar.add(menu);
        menu.add(new AbstractAction("Show All") {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                focusGraph(null);
            }
        });
        menu.add(new JSeparator());
        for (Graph graph : this.graphs) {
            menu.add(new ObjectAction<Graph>(graph, String.valueOf(graph.getId())) {

                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent e) {
                    focusGraph(getObject());
                }
            });
        }
        JPanel pane1 = new JPanel(new GridLayout(1, 0, 5, 5));
        contentPane.add(pane1, BorderLayout.CENTER);
        this.drawingArea = new JPanel(true) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                paintDrawingArea(Graphics2D.class.cast(g));
            }

            public String getToolTipText(MouseEvent e) {
                Icon icon = findIconAt(e.getX(), e.getY());
                if (icon != null) return icon.getTitle();
                return null;
            }
        };
        this.drawingArea.setToolTipText("");
        this.drawingArea.setOpaque(true);
        this.drawingArea.setBackground(Color.BLACK);
        this.drawingArea.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentShown(ComponentEvent e) {
                focusGraph(null);
                e.getComponent().removeComponentListener(this);
            }
        });
        MouseInputAdapter mouseListener = new MouseInputAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (!e.isPopupTrigger()) return;
                Icon icon = findIconAt(e.getX(), e.getY());
                if (icon == null) return;
                JPopupMenu pop = new JPopupMenu();
                AbstractAction action = new ObjectAction<Icon>(icon, "Open URL ") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            Desktop.getDesktop().browse(getObject().getNode().getURL().toURI());
                        } catch (Exception e1) {
                            ThrowablePane.show(drawingArea, e1);
                        }
                    }
                };
                if (icon == null || !Desktop.isDesktopSupported()) {
                    action.setEnabled(false);
                }
                pop.add(action);
                pop.show(e.getComponent(), e.getX(), e.getY());
            }

            public void mouseClicked(MouseEvent e) {
                Icon icon = findIconAt(e.getX(), e.getY());
                if (icon == null) return;
                if (e.getClickCount() < 2 && e.isShiftDown()) {
                    try {
                        Desktop.getDesktop().browse(icon.getNode().getURL().toURI());
                    } catch (Exception e1) {
                        ThrowablePane.show(drawingArea, e1);
                    }
                    return;
                }
                setHistory(icon);
            }
        };
        this.drawingArea.addMouseListener(mouseListener);
        this.drawingArea.addMouseMotionListener(mouseListener);
        pane1.add(new JScrollPane(this.drawingArea));
    }
