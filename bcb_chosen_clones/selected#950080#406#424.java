    public ImageData getJPEGDiagram() {
        Shell shell = new Shell();
        GraphicalViewer viewer = new ScrollingGraphicalViewer();
        viewer.createControl(shell);
        viewer.setEditDomain(new DefaultEditDomain(null));
        viewer.setRootEditPart(new ScalableFreeformRootEditPart());
        viewer.setEditPartFactory(new CsdeEditPartFactory());
        viewer.setContents(getDiagram());
        viewer.flush();
        LayerManager lm = (LayerManager) viewer.getEditPartRegistry().get(LayerManager.ID);
        IFigure fig = lm.getLayer(LayerConstants.PRINTABLE_LAYERS);
        Dimension d = fig.getSize();
        Image image = new Image(null, d.width, d.height);
        GC tmpGC = new GC(image);
        SWTGraphics graphics = new SWTGraphics(tmpGC);
        fig.paint(graphics);
        shell.dispose();
        return image.getImageData();
    }
