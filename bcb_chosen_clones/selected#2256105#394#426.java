    @Override
    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        getGraphicalViewer().setEditPartFactory(fact);
        ITypeRoot tr = sourceEditor.getInputElement();
        try {
            getGraphicalViewer().setContents(tr);
            ((DesignTimeComponent) fact.getTopPart().getModelChildren().get(0)).addListener(new DesignTimeListener() {

                public void handleUpdate(DesignTimeComponent comp) {
                    updateSourceWithComponent(comp.getTopLevel());
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
        getGraphicalViewer().addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent arg0) {
                if (getSelectedComponent() == null) {
                    return;
                }
                PropertySheetPage propertySheet = ((PropertySheetPage) getAdapter(IPropertySheetPage.class));
                propertySheet.selectionChanged(getSite().getPart(), new StructuredSelection(getSelectedComponent()));
                try {
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        getGraphicalViewer().setContextMenu(contextMenu);
        contextMenu.addMenuListener(this);
    }
