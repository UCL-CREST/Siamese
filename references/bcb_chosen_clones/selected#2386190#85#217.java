    private void constructDialogContent(Composite parent) {
        SashForm splitter = new SashForm(parent, SWT.HORIZONTAL);
        splitter.setLayoutData(new GridData(GridData.FILL_BOTH));
        Group fragmentsGroup = new Group(splitter, SWT.NONE);
        fragmentsGroup.setLayout(new GridLayout(1, false));
        fragmentsGroup.setText("Result Fragments");
        fragmentsTable = CheckboxTableViewer.newCheckList(fragmentsGroup, SWT.NONE);
        fragmentsTable.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
        fragmentsTable.setContentProvider(new ArrayContentProvider());
        fragmentsTable.setLabelProvider(new LabelProvider() {

            public Image getImage(Object element) {
                return JFaceResources.getImage(WsmoImageRegistry.INSTANCE_ICON);
            }

            public String getText(Object element) {
                if (element == null) {
                    return "";
                }
                if (element instanceof ProcessFragment) {
                    ProcessFragment frag = (ProcessFragment) element;
                    String label = (frag.getName() == null) ? " <no-fragment-name>" : frag.getName();
                    if (frag.getDescription() != null) {
                        label += "  [" + Utils.normalizeSpaces(frag.getDescription()) + ']';
                    }
                    return label;
                }
                return element.toString();
            }
        });
        fragmentsTable.setInput(results.toArray());
        final MenuManager menuMgr = new MenuManager();
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager mgr) {
                if (false == GUIHelper.containsCursor(fragmentsTable.getTable())) {
                    return;
                }
                if (false == fragmentsTable.getSelection().isEmpty()) {
                    menuMgr.add(new Action("Edit Name") {

                        public void run() {
                            doEditName();
                        }
                    });
                    menuMgr.add(new Action("Edit Description") {

                        public void run() {
                            doEditDescription();
                        }
                    });
                    menuMgr.add(new Separator());
                }
                menuMgr.add(new Action("Select All") {

                    public void run() {
                        fragmentsTable.setAllChecked(true);
                        updateSelectionMonitor();
                    }
                });
                menuMgr.add(new Separator());
                menuMgr.add(new Action("Unselect All") {

                    public void run() {
                        fragmentsTable.setAllChecked(false);
                        updateSelectionMonitor();
                    }
                });
            }
        });
        fragmentsTable.getTable().setMenu(menuMgr.createContextMenu(fragmentsTable.getTable()));
        fragmentsTable.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                updatePreviewPanel((IStructuredSelection) event.getSelection());
            }
        });
        new FragmentsToolTipProvider(this.fragmentsTable.getTable());
        Group previewGroup = new Group(splitter, SWT.NONE);
        previewGroup.setLayout(new GridLayout(1, false));
        previewGroup.setText("Fragment Preview");
        createZoomToolbar(previewGroup);
        previewArea = new Composite(previewGroup, SWT.BORDER);
        previewArea.setLayoutData(new GridData(GridData.FILL_BOTH));
        previewArea.setLayout(new GridLayout(1, false));
        viewer = new ScrollingGraphicalViewer();
        viewer.createControl(previewArea);
        ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart();
        viewer.setRootEditPart(rootEditPart);
        viewer.setEditPartFactory(new GraphicalPartFactory());
        viewer.getControl().setBackground(ColorConstants.listBackground);
        viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        ZoomManager zoomManager = rootEditPart.getZoomManager();
        ArrayList<String> zoomContributions = new ArrayList<String>();
        zoomContributions.add(ZoomManager.FIT_ALL);
        zoomContributions.add(ZoomManager.FIT_HEIGHT);
        zoomContributions.add(ZoomManager.FIT_WIDTH);
        zoomManager.setZoomLevelContributions(zoomContributions);
        zoomManager.setZoomLevels(new double[] { 0.25, 0.33, 0.5, 0.75, 1.0 });
        zoomManager.setZoom(1.0);
        Composite businessGoalPanel = new Composite(previewGroup, SWT.NONE);
        businessGoalPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        businessGoalPanel.setLayout(new GridLayout(4, false));
        Label lab = new Label(businessGoalPanel, SWT.NONE);
        lab.setText("Process goal:");
        bpgIRI = new Text(businessGoalPanel, SWT.BORDER | SWT.READ_ONLY);
        bpgIRI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        selectBpgButton = new Button(businessGoalPanel, SWT.NONE);
        selectBpgButton.setText("Select");
        selectBpgButton.setEnabled(false);
        selectBpgButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent s) {
                doSelectProcessGoal();
            }
        });
        clearBpgButton = new Button(businessGoalPanel, SWT.NONE);
        clearBpgButton.setText("Clear");
        clearBpgButton.setEnabled(false);
        clearBpgButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent s) {
                IStructuredSelection sel = (IStructuredSelection) fragmentsTable.getSelection();
                if (sel.isEmpty() || false == sel.getFirstElement() instanceof ProcessFragment) {
                    return;
                }
                ((ProcessFragment) sel.getFirstElement()).setBusinessProcessGoal(null);
                updatePreviewPanel(sel);
            }
        });
        splitter.setWeights(new int[] { 1, 2 });
    }
