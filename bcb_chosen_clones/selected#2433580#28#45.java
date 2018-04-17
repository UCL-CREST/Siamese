    public static IFigure render(IDiagram diagram) {
        Diagram realDiagram;
        try {
            realDiagram = ((Diagram.IDiagramImpl) diagram).getDiagram();
        } catch (ClassCastException x) {
            throw new IllegalArgumentException("invalid diagram parameter");
        }
        ScrollingGraphicalViewer viewer = new ScrollingGraphicalViewer();
        viewer.createControl(RMBenchPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell());
        viewer.setRootEditPart(new ScalableFreeformRootEditPart());
        viewer.setEditPartFactory(new CustomEditPartFactory());
        viewer.setContents(realDiagram);
        AbstractGraphicalEditPart aep = (AbstractGraphicalEditPart) viewer.getRootEditPart();
        refresh(aep);
        IFigure root = ((AbstractGraphicalEditPart) viewer.getRootEditPart()).getFigure();
        setPreferedSize(root);
        return root;
    }
