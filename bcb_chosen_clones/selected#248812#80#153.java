    public void createPartControl(Composite parent) {
        FormToolkit toolkit;
        toolkit = new FormToolkit(parent.getDisplay());
        form = toolkit.createForm(parent);
        form.setText("Apple Inc.");
        toolkit.decorateFormHeading(form);
        form.getBody().setLayout(new GridLayout());
        chart = createChart();
        final DateAxis dateAxis = new DateAxis();
        viewer = new GraphicalViewerImpl();
        viewer.setRootEditPart(new ScalableRootEditPart());
        viewer.setEditPartFactory(new ChartEditPartFactory(dateAxis));
        viewer.createControl(form.getBody());
        viewer.setContents(chart);
        viewer.setEditDomain(new EditDomain());
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                System.err.println("selectionChanged " + event.getSelection());
            }
        });
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                deleteAction.update();
            }
        });
        ActionRegistry actionRegistry = new ActionRegistry();
        createActions(actionRegistry);
        ContextMenuProvider cmProvider = new BlockContextMenuProvider(viewer, actionRegistry);
        viewer.setContextMenu(cmProvider);
        getSite().setSelectionProvider(viewer);
        deleteAction.setSelectionProvider(viewer);
        viewer.getEditDomain().getCommandStack().addCommandStackEventListener(new CommandStackEventListener() {

            public void stackChanged(CommandStackEvent event) {
                undoAction.setEnabled(viewer.getEditDomain().getCommandStack().canUndo());
                redoAction.setEnabled(viewer.getEditDomain().getCommandStack().canRedo());
            }
        });
        Data data = Data.getData();
        chart.setInput(data);
        DateRange dateRange = new DateRange(0, 50);
        dateAxis.setDates(data.date);
        dateAxis.setSelectedRange(dateRange);
        slider = new Slider(form.getBody(), SWT.NONE);
        slider.setMinimum(0);
        slider.setMaximum(data.close.length - 1);
        slider.setSelection(dateRange.start);
        slider.setThumb(dateRange.length);
        slider.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                DateRange r = new DateRange(slider.getSelection(), slider.getThumb());
                dateAxis.setSelectedRange(r);
            }
        });
        final Scale spinner = new Scale(form.getBody(), SWT.NONE);
        spinner.setMinimum(5);
        spinner.setMaximum(data.close.length - 1);
        spinner.setSelection(dateRange.length);
        spinner.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                slider.setThumb(spinner.getSelection());
                DateRange r = new DateRange(slider.getSelection(), slider.getThumb());
                dateAxis.setSelectedRange(r);
            }
        });
        GridDataFactory.defaultsFor(viewer.getControl()).grab(true, true).align(GridData.FILL, GridData.FILL).applyTo(viewer.getControl());
        GridDataFactory.defaultsFor(slider).grab(true, false).align(GridData.FILL, GridData.FILL).grab(true, false).applyTo(slider);
        GridDataFactory.defaultsFor(spinner).grab(true, false).align(GridData.FILL, GridData.FILL).grab(true, false).applyTo(spinner);
        getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
    }
