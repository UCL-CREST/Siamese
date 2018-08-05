    protected void initializeGraphicalViewer() {
        getGraphicalViewer().setContents(loadModel());
        getGraphicalViewer().addDropTargetListener(createTransferDropTargetListener());
        getGraphicalViewer().addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                if (event.getSelection().isEmpty()) {
                    return;
                }
                loadProperties(((StructuredSelection) event.getSelection()).getFirstElement());
            }
        });
    }
