        public void run() {
            Shell shell = new Shell(display);
            shell.setLayout(new GridLayout(1, false));
            ERDiagramEditPartFactory editPartFactory = new ERDiagramEditPartFactory();
            viewer = new ScrollingGraphicalViewer();
            viewer.setControl(new FigureCanvas(shell));
            ScalableFreeformRootEditPart rootEditPart = new PagableFreeformRootEditPart(diagram);
            viewer.setRootEditPart(rootEditPart);
            viewer.setEditPartFactory(editPartFactory);
            viewer.setContents(diagram);
        }
