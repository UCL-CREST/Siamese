    public void createControl(Composite parent) {
        top = new Composite(parent, SWT.NONE);
        top.setLayout(new GridLayout());
        top.setLayoutData(new GridData(GridData.FILL_BOTH));
        ComposedAdapterFactory factories = new ComposedAdapterFactory();
        factories.addAdapterFactory(new EcoreItemProviderAdapterFactory());
        factories.addAdapterFactory(new NotationAdapterFactory());
        factories.addAdapterFactory(new ResourceItemProviderAdapterFactory());
        modelViewer = new TreeViewer(top, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        modelViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
        modelViewer.setContentProvider(new AdapterFactoryContentProvider(factories) {

            public boolean hasChildren(Object object) {
                boolean result = super.hasChildren(object);
                if (object instanceof Diagram) {
                    result = false;
                }
                if (object instanceof EPackage && result == false) {
                    result = !DiagramUtil.getDiagrams((EPackage) object, editor.getDiagram().eResource()).isEmpty();
                }
                return result;
            }

            public Object[] getChildren(Object object) {
                Object[] result = super.getChildren(object);
                if (object instanceof EPackage) {
                    List<Diagram> list = DiagramUtil.getDiagrams((EPackage) object, editor.getDiagram().eResource());
                    if (list.size() != 0) {
                        Object[] newResult = new Object[result.length + list.size()];
                        for (int i = 0; i < result.length; i++) {
                            newResult[i] = result[i];
                        }
                        for (int i = 0; i < list.size(); i++) {
                            newResult[result.length + i] = list.get(i);
                        }
                        return newResult;
                    }
                }
                return result;
            }
        });
        modelViewer.setLabelProvider(new AdapterFactoryLabelProvider(factories) {

            public String getText(Object element) {
                String result = super.getText(element);
                if (element instanceof Diagram) {
                    if (editor.getDiagram() == element) {
                        result += " *";
                    }
                }
                return result;
            }

            public String getColumnText(Object object, int columnIndex) {
                String result = super.getText(object);
                if (object instanceof Diagram) {
                    if (editor.getDiagram() == object) {
                        result += " (active)";
                    }
                }
                return result;
            }
        });
        modelViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                setDiagramSelection(modelViewer.getSelection());
            }
        });
        modelViewer.addDragSupport(DND.DROP_COPY, new Transfer[] { LocalTransfer.getInstance() }, new TreeDragListener());
        modelViewer.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                Object selectedObject = selection.getFirstElement();
                if (selectedObject instanceof Diagram) {
                    openDiagram((Diagram) selectedObject);
                }
            }
        });
        createContextMenuFor(modelViewer);
        editor.getDiagramGraphicalViewer().addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                selectionInDiagramChange(event);
            }
        });
        this.getSite().setSelectionProvider(modelViewer);
        setInput();
    }
