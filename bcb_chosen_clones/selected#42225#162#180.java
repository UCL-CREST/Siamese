    protected GraphicalViewer createGraphicalViewer(Composite parent) {
        GraphicalViewer viewer = new ScrollingGraphicalViewer();
        viewer.createControl(parent);
        viewer.getControl().setBackground(parent.getBackground());
        viewer.setRootEditPart(new ScalableFreeformRootEditPart());
        GraphicalViewerKeyHandler graphicalViewerKeyHandler = new GraphicalViewerKeyHandler(viewer);
        KeyHandler parentKeyHandler = graphicalViewerKeyHandler.setParent(getCommonKeyHandler());
        viewer.setKeyHandler(parentKeyHandler);
        getEditDomain().addViewer(viewer);
        getSite().setSelectionProvider(viewer);
        ContextMenuProvider provider = new TestContextMenuProvider(viewer, getActionRegistry());
        viewer.setContextMenu(provider);
        getSite().registerContextMenu("cubicTestPlugin.editor.contextmenu", provider, viewer);
        viewer.addDropTargetListener(new DataEditDropTargetListner(((IFileEditorInput) getEditorInput()).getFile().getProject(), viewer));
        viewer.addDropTargetListener(new FileTransferDropTargetListener(viewer));
        viewer.setEditPartFactory(getEditPartFactory());
        viewer.setContents(getContent());
        return viewer;
    }
