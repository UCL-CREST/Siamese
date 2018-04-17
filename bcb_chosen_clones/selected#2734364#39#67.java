    public TreeOptions(IDecisionMethod tree, IAtomContainer atomcontainer) {
        super(JSplitPane.HORIZONTAL_SPLIT);
        JOutlookBar outlook = new JOutlookBar();
        outlook.setBackground(Color.white);
        outlook.setTabPlacement(JTabbedPane.LEFT);
        addGenericTab(outlook);
        if (atomcontainer != null) addMoleculeTab(outlook, atomcontainer);
        addTreeTab(outlook, tree);
        setLeftComponent(outlook);
        JTextPane details = new JTextPane();
        details.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    try {
                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().browse(e.getURL().toURI());
                        } else Tools.openURL(e.getURL().toString());
                    } catch (Exception x) {
                        x.printStackTrace();
                    }
                }
            }
        });
        details.setEditorKit(new HTMLEditorKit());
        details.setText(tree.getExplanation());
        setRightComponent(new JScrollPane(details));
        setPreferredSize(new Dimension(450, 400));
    }
