    public AbstractHowToView(final IViewContext context) {
        super(ID);
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    this.sourceUri = new URI(URL_PREFIX + getClass().getSimpleName() + ".java");
                    this.migLayoutUri = new URI("http://www.miglayout.com/");
                } catch (final Exception e1) {
                    throw new RuntimeException();
                }
            }
        }
        final IActionItemModel sourceAction = context.getToolBar().addActionItem(SilkIcons.PAGE_WHITE_TEXT, "View Source");
        sourceAction.addActionListener(new IActionListener() {

            @Override
            public void actionPerformed() {
                try {
                    if (desktop != null && sourceUri != null) {
                        desktop.browse(sourceUri);
                    } else {
                        Toolkit.getMessagePane().showError("Could not open browser. \n Maybe java desktop is not supported.");
                    }
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        final IActionItemModel migLayout = context.getToolBar().addActionItem(SilkIcons.WORLD, "MiGLayout Layout Manager");
        migLayout.addActionListener(new IActionListener() {

            @Override
            public void actionPerformed() {
                try {
                    if (desktop != null && sourceUri != null) {
                        desktop.browse(migLayoutUri);
                    } else {
                        Toolkit.getMessagePane().showError("Could not open browser. \n Maybe java desktop is not supported.");
                    }
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        createViewContent(context.getContainer(), Toolkit.getBluePrintFactory());
    }
