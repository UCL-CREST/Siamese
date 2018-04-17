    private void createCanvas() {
        GraphicalViewer viewer = new ScrollingGraphicalViewer();
        viewer.setRootEditPart(new ScalableRootEditPart());
        viewer.setEditPartFactory(new BlockEditPartFactory());
        viewer.createControl(this);
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
        ActionRegistry actionRegistry = new ActionRegistry();
        createActions(actionRegistry);
        ContextMenuProvider cmProvider = new BlockContextMenuProvider(viewer, actionRegistry);
        viewer.setContextMenu(cmProvider);
        Block b = new Block();
        b.addChild(new ChartItem());
        viewer.setContents(b);
        PaletteViewer paletteViewer = new PaletteViewer();
        paletteViewer.createControl(this);
    }
