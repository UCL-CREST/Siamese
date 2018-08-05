    public void createPartControl(Composite parent) {
        splitter = new FlyoutPaletteComposite(parent, SWT.NONE, getSite().getPage(), getPaletteViewerProvider(), new PaletteFlyoutPreferences());
        viewer = new ScrollingGraphicalViewer();
        viewer.createControl(splitter);
        editDomain.addViewer(viewer);
        viewer.getControl().setBackground(ColorConstants.listBackground);
        viewer.setProperty(SnapToGrid.PROPERTY_GRID_SPACING, new Dimension(45, 45));
        ScalableFreeformRootEditPart root = new CustomRootEditPart();
        viewer.setRootEditPart(root);
        viewer.setEditPartFactory(new CustomEditPartFactory());
        getSelectionSynchronizer().addViewer(viewer);
        getSite().setSelectionProvider(viewer);
        splitter.hookDropTargetListener(viewer);
        splitter.setGraphicalControl(viewer.getControl());
        configureViewerActions(root);
        configureKeyHandler();
        ContextMenuProvider comtextMenuProvider = new DiagramContextMenuProvider(viewer, actionRegistry, undoRedoGroup);
        viewer.setContextMenu(comtextMenuProvider);
        viewer.addDropTargetListener(DiagramDropTargetListener.forImport(viewer));
        viewer.addDropTargetListener(DiagramDropTargetListener.forModel(viewer));
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                if (selection.size() == 1) {
                    EditPart selectedPart = (EditPart) selection.getFirstElement();
                    if (selectedPart instanceof TableEditPart) {
                        Table table = ((TableEditPart) selectedPart).getTable();
                        RMBenchPlugin.getEventManager().fireTableSelected(DiagramEditor.this, table);
                    } else if (selectedPart instanceof ColumnEditPart) {
                        Column column = ((ColumnEditPart) selectedPart).getColumn();
                        RMBenchPlugin.getEventManager().fireColumnSelected(DiagramEditor.this, column);
                    } else if (selectedPart instanceof ForeignKeyEditPart) {
                        ForeignKey key = ((ForeignKeyEditPart) selectedPart).getForeignKey();
                        RMBenchPlugin.getEventManager().fireForeignKeySelected(DiagramEditor.this, key);
                    } else {
                        RMBenchPlugin.getEventManager().fireTableSelected(DiagramEditor.this, null);
                    }
                }
                updateActions(selectionActionIDs);
            }
        });
        viewer.setContents(getDiagram());
        if (RMBenchPlugin.getModelManager().isDirty()) firePropertyChange(IWorkbenchPartConstants.PROP_DIRTY);
        pasteTablesAction.hookEvents();
    }
