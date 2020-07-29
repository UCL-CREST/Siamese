    private void createGraphicalViewer(Composite parent) {
        viewer = new ScrollingGraphicalViewer();
        viewer.createControl(parent);
        viewer.getControl().setBackground(parent.getBackground());
        viewer.setRootEditPart(new ScalableFreeformRootEditPart());
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
        registerEditPartViewer(viewer);
        configureEditPartViewer(viewer);
        viewer.setEditPartFactory(new GraphicalEditPartsFactory(getSite().getShell()));
        viewer.setContents(getContractEditor().getContract());
        ContextMenuProvider provider = new ContractContextMenuProvider(getGraphicalViewer(), getContractEditor().getActionRegistry());
        getGraphicalViewer().setContextMenu(provider);
        getSite().registerContextMenu(provider, getGraphicalViewer());
    }
