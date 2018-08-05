    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setEditPartFactory(createEditPartFactory());
        ScalableRootEditPart rootEditPart = new ScalableRootEditPart();
        viewer.setRootEditPart(rootEditPart);
        ZoomManager manager = rootEditPart.getZoomManager();
        double[] zoomLevels = new double[] { 0.25, 0.5, 0.75, 1.0, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 10.0, 20.0 };
        manager.setZoomLevels(zoomLevels);
        ArrayList zoomContributions = new ArrayList();
        zoomContributions.add(ZoomManager.FIT_ALL);
        zoomContributions.add(ZoomManager.FIT_HEIGHT);
        zoomContributions.add(ZoomManager.FIT_WIDTH);
        manager.setZoomLevelContributions(zoomContributions);
        getActionRegistry().registerAction(new ZoomInAction(manager));
        getActionRegistry().registerAction(new ZoomOutAction(manager));
        getGraphicalViewer().setKeyHandler(new GraphicalViewerKeyHandler(getGraphicalViewer()));
        String menuId = this.getClass().getName() + ".EditorContext";
        MenuManager menuMgr = new MenuManager(menuId, menuId);
        openPropertyAction = new OpenPropertyViewAction(viewer);
        openOutlineAction = new OpenOutlineViewAction(viewer);
        saveAsImageAction = new SaveAsImageAction(viewer);
        createDiagramAction(viewer);
        getSite().registerContextMenu(menuId, menuMgr, viewer);
        PrintAction printAction = new PrintAction(this);
        printAction.setImageDescriptor(UMLPlugin.getImageDescriptor("icons/print.gif"));
        getActionRegistry().registerAction(printAction);
        final DeleteAction deleteAction = new DeleteAction((IWorkbenchPart) this);
        deleteAction.setSelectionProvider(getGraphicalViewer());
        getActionRegistry().registerAction(deleteAction);
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                deleteAction.update();
            }
        });
        menuMgr.add(new Separator("edit"));
        menuMgr.add(getActionRegistry().getAction(ActionFactory.DELETE.getId()));
        menuMgr.add(getActionRegistry().getAction(ActionFactory.UNDO.getId()));
        menuMgr.add(getActionRegistry().getAction(ActionFactory.REDO.getId()));
        menuMgr.add(new Separator("zoom"));
        menuMgr.add(getActionRegistry().getAction(GEFActionConstants.ZOOM_IN));
        menuMgr.add(getActionRegistry().getAction(GEFActionConstants.ZOOM_OUT));
        fillDiagramPopupMenu(menuMgr);
        menuMgr.add(new Separator("print"));
        menuMgr.add(saveAsImageAction);
        menuMgr.add(printAction);
        menuMgr.add(new Separator("views"));
        menuMgr.add(openPropertyAction);
        menuMgr.add(openOutlineAction);
        menuMgr.add(new Separator("generate"));
        menuMgr.add(new Separator("additions"));
        viewer.setContextMenu(menuMgr);
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer).setParent(getCommonKeyHandler()));
    }
