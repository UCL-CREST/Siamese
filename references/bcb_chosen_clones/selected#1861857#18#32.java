    public MovementsPage(EditDomain editDomain, Object contents, final GraphicalViewer viewer) {
        super(tree, editDomain, contents);
        tree.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                if (event.getSelection() instanceof IStructuredSelection) {
                    IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                    if (selection.getFirstElement() instanceof Movement) {
                        Movement movement = (Movement) selection.getFirstElement();
                        viewer.setContents(movement);
                    }
                }
            }
        });
    }
