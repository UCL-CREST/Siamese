    private ScrollingGraphicalViewer createGraphicalViewer(final Composite parent) {
        final ScrollingGraphicalViewer viewer = new ScrollingGraphicalViewer();
        viewer.createControl(parent);
        _root = new ScalableRootEditPart();
        viewer.setRootEditPart(_root);
        getEditDomain().addViewer(viewer);
        getSite().setSelectionProvider(viewer);
        viewer.setEditPartFactory(getEditPartFactory());
        viewer.setContents(getEditorInput().getAdapter(ScannedMap.class));
        return viewer;
    }
