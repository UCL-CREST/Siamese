    @SuppressWarnings("unchecked")
    protected void initializeGraphicalViewer() {
        GraphicalViewer viewer = getGraphicalViewer();
        ScalableRootEditPart rootEditPart = new ScalableRootEditPart();
        viewer.setEditPartFactory(new DBEditPartFactory());
        viewer.setRootEditPart(rootEditPart);
        ZoomManager manager = rootEditPart.getZoomManager();
        double[] zoomLevels = new double[] { 0.25, 0.5, 0.75, 1.0, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 10.0, 20.0 };
        manager.setZoomLevels(zoomLevels);
        List<String> zoomContributions = new ArrayList<String>();
        zoomContributions.add(ZoomManager.FIT_ALL);
        zoomContributions.add(ZoomManager.FIT_HEIGHT);
        zoomContributions.add(ZoomManager.FIT_WIDTH);
        manager.setZoomLevelContributions(zoomContributions);
        getActionRegistry().registerAction(new ZoomInAction(manager));
        getActionRegistry().registerAction(new ZoomOutAction(manager));
        PrintAction printAction = new PrintAction(this);
        printAction.setText(DBPlugin.getResourceString("action.print"));
        printAction.setImageDescriptor(DBPlugin.getImageDescriptor("icons/print.gif"));
        getActionRegistry().registerAction(printAction);
        IFile file = ((IFileEditorInput) getEditorInput()).getFile();
        RootModel root = null;
        try {
            root = VisualDBSerializer.deserialize(file.getContents());
        } catch (Exception ex) {
            DBPlugin.logException(ex);
            root = new RootModel();
            root.setDialectName(DialectProvider.getDialectNames()[0]);
        }
        viewer.setContents(root);
        final DeleteAction deleteAction = new DeleteAction((IWorkbenchPart) this);
        deleteAction.setSelectionProvider(getGraphicalViewer());
        getActionRegistry().registerAction(deleteAction);
        getGraphicalViewer().addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                deleteAction.update();
            }
        });
        MenuManager menuMgr = new MenuManager();
        menuMgr.add(new QuickOutlineAction());
        menuMgr.add(new Separator());
        menuMgr.add(getActionRegistry().getAction(ActionFactory.UNDO.getId()));
        menuMgr.add(getActionRegistry().getAction(ActionFactory.REDO.getId()));
        menuMgr.add(new Separator());
        PasteAction pasteAction = new PasteAction(this);
        getActionRegistry().registerAction(pasteAction);
        getSelectionActions().add(pasteAction.getId());
        menuMgr.add(pasteAction);
        CopyAction copyAction = new CopyAction(this, pasteAction);
        getActionRegistry().registerAction(copyAction);
        getSelectionActions().add(copyAction.getId());
        menuMgr.add(copyAction);
        menuMgr.add(getActionRegistry().getAction(ActionFactory.DELETE.getId()));
        menuMgr.add(new Separator());
        menuMgr.add(new AutoLayoutAction(viewer));
        menuMgr.add(new DommainEditAction(viewer));
        MenuManager convertMenu = new MenuManager(DBPlugin.getResourceString("action.convert"));
        menuMgr.add(convertMenu);
        UppercaseAction uppercaseAction = new UppercaseAction(this);
        convertMenu.add(uppercaseAction);
        getActionRegistry().registerAction(uppercaseAction);
        getSelectionActions().add(uppercaseAction.getId());
        LowercaseAction lowercaseAction = new LowercaseAction(this);
        convertMenu.add(lowercaseAction);
        getActionRegistry().registerAction(lowercaseAction);
        getSelectionActions().add(lowercaseAction.getId());
        Physical2LogicalAction physical2logicalAction = new Physical2LogicalAction(this);
        convertMenu.add(physical2logicalAction);
        getActionRegistry().registerAction(physical2logicalAction);
        getSelectionActions().add(physical2logicalAction.getId());
        Logical2PhysicalAction logical2physicalAction = new Logical2PhysicalAction(this);
        convertMenu.add(logical2physicalAction);
        getActionRegistry().registerAction(logical2physicalAction);
        getSelectionActions().add(logical2physicalAction.getId());
        menuMgr.add(new ToggleModelAction(viewer));
        menuMgr.add(new ChangeDBTypeAction(viewer));
        menuMgr.add(new Separator());
        menuMgr.add(getActionRegistry().getAction(GEFActionConstants.ZOOM_IN));
        menuMgr.add(getActionRegistry().getAction(GEFActionConstants.ZOOM_OUT));
        menuMgr.add(new Separator());
        menuMgr.add(new CopyAsImageAction(viewer));
        menuMgr.add(getActionRegistry().getAction(ActionFactory.PRINT.getId()));
        menuMgr.add(new Separator());
        MenuManager validation = new MenuManager(DBPlugin.getResourceString("action.validation"));
        validation.add(new ValidateAction(viewer));
        validation.add(new DeleteMarkerAction(viewer));
        menuMgr.add(validation);
        MenuManager importMenu = new MenuManager(DBPlugin.getResourceString("action.import"));
        importMenu.add(new ImportFromJDBCAction(viewer));
        importMenu.add(new ImportFromDiagramAction(viewer));
        menuMgr.add(importMenu);
        MenuManager generate = new MenuManager(DBPlugin.getResourceString("action.export"));
        IGenerator[] generaters = GeneratorProvider.getGeneraters();
        for (int i = 0; i < generaters.length; i++) {
            generate.add(new GenerateAction(generaters[i], viewer, this));
        }
        menuMgr.add(generate);
        menuMgr.add(new SelectedTablesDDLAction(viewer));
        viewer.setContextMenu(menuMgr);
        viewer.getControl().addMouseListener(new MouseAdapter() {

            public void mouseDoubleClick(MouseEvent e) {
                IStructuredSelection selection = (IStructuredSelection) getGraphicalViewer().getSelection();
                Object obj = selection.getFirstElement();
                if (obj != null && obj instanceof IDoubleClickSupport) {
                    ((IDoubleClickSupport) obj).doubleClicked();
                }
            }
        });
        outlinePage = new VisualDBOutlinePage(viewer, getEditDomain(), root, getSelectionSynchronizer());
        applyPreferences();
        viewer.getControl().addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.stateMask == SWT.CTRL && e.keyCode == 'o') {
                    new QuickOutlineAction().run();
                }
            }
        });
    }
