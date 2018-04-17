    public void createPartControl(Composite parent) {
        viewer = new ScrollingGraphicalViewer();
        viewer.setRootEditPart(new ScalableRootEditPart());
        viewer.setEditPartFactory(new BlockEditPartFactory());
        viewer.setEditDomain(new EditDomain());
        viewer.createControl(parent);
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
        ActionRegistry actionRegistry = new ActionRegistry();
        createActions(actionRegistry);
        ContextMenuProvider cmProvider = new BlockContextMenuProvider(viewer, actionRegistry);
        viewer.setContextMenu(cmProvider);
        Block b = new Block();
        b.addChild(new ChartItem());
        viewer.setContents(b);
        System.err.println("!!! " + viewer.getContents().getChildren().get(0));
        viewer.select((EditPart) viewer.getContents().getChildren().get(0));
        PaletteRoot root = new PaletteRoot();
        PaletteGroup toolGroup = new PaletteGroup("Chart Tools");
        List toolList = new ArrayList();
        ToolEntry tool = new SelectionToolEntry();
        toolList.add(tool);
        root.setDefaultEntry(tool);
        tool = new MarqueeToolEntry();
        toolList.add(tool);
        toolGroup.addAll(toolList);
        PaletteGroup templateGroup = new PaletteGroup("Templates");
        List templateList = new ArrayList();
        CombinedTemplateCreationEntry entry = new CombinedTemplateCreationEntry("Rect", "Rect", new ChartItem(), new SimpleFactory(ChartItem.class), ImageDescriptor.getMissingImageDescriptor(), ImageDescriptor.getMissingImageDescriptor());
        CombinedTemplateCreationEntry entry1 = new CombinedTemplateCreationEntry("Line", "Line", new ChartLine(), new SimpleFactory(ChartLine.class), ImageDescriptor.getMissingImageDescriptor(), ImageDescriptor.getMissingImageDescriptor());
        templateList.add(entry);
        templateList.add(entry1);
        templateGroup.addAll(templateList);
        List rootList = new ArrayList();
        rootList.add(toolGroup);
        rootList.add(templateGroup);
        root.addAll(rootList);
        final PaletteViewer v = new PaletteViewer();
        v.setPaletteRoot(root);
        v.createControl(parent);
        v.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                System.err.println("selectionChanged " + v.getActiveTool());
            }
        });
        deleteAction.setSelectionProvider(viewer);
        viewer.getEditDomain().setPaletteViewer(v);
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                System.err.println(event.getSelection());
                deleteAction.update();
            }
        });
        viewer.getEditDomain().getCommandStack().addCommandStackEventListener(new CommandStackEventListener() {

            public void stackChanged(CommandStackEvent event) {
                undoAction.setEnabled(viewer.getEditDomain().getCommandStack().canUndo());
                redoAction.setEnabled(viewer.getEditDomain().getCommandStack().canRedo());
            }
        });
    }
