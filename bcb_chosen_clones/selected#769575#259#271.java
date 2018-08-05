    @SuppressWarnings("unused")
    private GraphicalViewer createGraphicalViewer(Composite parent) {
        GraphicalViewer viewer = new ScrollingGraphicalViewer();
        viewer.createControl(parent);
        viewer.getControl().setBackground(parent.getBackground());
        viewer.setRootEditPart(new ScalableFreeformRootEditPart());
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
        getEditDomain().addViewer(viewer);
        getSite().setSelectionProvider(viewer);
        viewer.setEditPartFactory(getEditPartFactory());
        viewer.setContents(getContent());
        return viewer;
    }
