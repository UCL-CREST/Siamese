    private void createTab2(TabLayoutPanel tab) {
        ScrollingGraphicalViewer viewer;
        try {
            viewer = new ScrollingGraphicalViewer();
            viewer.createControl();
            ScalableFreeformRootEditPart root = new ScalableFreeformRootEditPart();
            viewer.setRootEditPart(root);
            viewer.setEditDomain(new EditDomain());
            viewer.setEditPartFactory(new org.drawx.gef.sample.client.tool.example.editparts.MyEditPartFactory());
            CanvasModel model = new CanvasModel();
            for (int i = 0; i < 1; i++) {
                MyConnectionModel conn = new MyConnectionModel();
                OrangeModel m1 = new OrangeModel(new Point(30, 230));
                OrangeModel m2 = new OrangeModel(new Point(0, 0));
                model.addChild(m1);
                model.addChild(m2);
                m1.addSourceConnection(conn);
                m2.addTargetConnection(conn);
                viewer.setContents(model);
            }
            DiagramEditor p = new DiagramEditor(viewer);
            viewer.setContextMenu(new MyContextMenuProvider(viewer, p.getActionRegistry()));
            VerticalPanel panel = new VerticalPanel();
            addToolbox(viewer.getEditDomain(), viewer, panel);
            panel.add(viewer.getControl().getWidget());
            tab.add(panel, "Fixed Size Canvas(+Overview)");
            addOverview(viewer, panel);
            viewer.getControl().setSize(400, 300);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
