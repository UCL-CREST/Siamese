    private void updateViewerContent(ScrollingGraphicalViewer viewer) {
        BioPAXGraph graph = (BioPAXGraph) viewer.getContents().getModel();
        if (!graph.isMechanistic()) return;
        Map<String, Color> highlightMap = new HashMap<String, Color>();
        for (Object o : graph.getNodes()) {
            IBioPAXNode node = (IBioPAXNode) o;
            if (node.isHighlighted()) {
                highlightMap.put(node.getIDHash(), node.getHighlightColor());
            }
        }
        for (Object o : graph.getEdges()) {
            IBioPAXEdge edge = (IBioPAXEdge) o;
            if (edge.isHighlighted()) {
                highlightMap.put(edge.getIDHash(), edge.getHighlightColor());
            }
        }
        HighlightLayer hLayer = (HighlightLayer) ((ChsScalableRootEditPart) viewer.getRootEditPart()).getLayer(HighlightLayer.HIGHLIGHT_LAYER);
        hLayer.removeAll();
        hLayer.highlighted.clear();
        viewer.deselectAll();
        graph.recordLayout();
        PathwayHolder p = graph.getPathway();
        if (withContent != null) {
            p.updateContentWith(withContent);
        }
        BioPAXGraph newGraph = main.getRootGraph().excise(p);
        newGraph.setAsRoot();
        viewer.setContents(newGraph);
        boolean layedout = newGraph.fetchLayout();
        if (!layedout) {
            new CoSELayoutAction(main).run();
        }
        viewer.deselectAll();
        GraphAnimation.run(viewer);
        for (Object o : newGraph.getNodes()) {
            IBioPAXNode node = (IBioPAXNode) o;
            if (highlightMap.containsKey(node.getIDHash())) {
                node.setHighlightColor(highlightMap.get(node.getIDHash()));
                node.setHighlight(true);
            }
        }
        for (Object o : newGraph.getEdges()) {
            IBioPAXEdge edge = (IBioPAXEdge) o;
            if (highlightMap.containsKey(edge.getIDHash())) {
                edge.setHighlightColor(highlightMap.get(edge.getIDHash()));
                edge.setHighlight(true);
            }
        }
    }
