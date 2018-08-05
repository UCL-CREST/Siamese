    protected Control createContents(Composite parent) {
        this.getShell().setText("Chisio");
        this.getShell().setSize(800, 600);
        this.getShell().setImage(ImageDescriptor.createFromFile(ChisioMain.class, "icon/chisio-icon.png").createImage());
        Composite composite = new Composite(parent, SWT.BORDER);
        composite.setLayout(new FillLayout());
        this.viewer = new ScrollingGraphicalViewer();
        this.viewer.setEditDomain(this.editDomain);
        this.viewer.createControl(composite);
        this.viewer.getControl().setBackground(ColorConstants.white);
        this.rootEditPart = new ChsScalableRootEditPart();
        this.viewer.setRootEditPart(this.rootEditPart);
        this.viewer.setEditPartFactory(new ChsEditPartFactory());
        ((FigureCanvas) this.viewer.getControl()).setScrollBarVisibility(FigureCanvas.ALWAYS);
        this.viewer.addDropTargetListener(new ChsFileDropTargetListener(this.viewer, this));
        this.viewer.addDragSourceListener(new ChsFileDragSourceListener(this.viewer));
        CompoundModel model = new CompoundModel();
        model.setAsRoot();
        this.viewer.setContents(model);
        this.viewer.getControl().addMouseListener(this);
        this.popupManager = new PopupManager(this);
        this.popupManager.setRemoveAllWhenShown(true);
        this.popupManager.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                ChisioMain.this.popupManager.createActions(manager);
            }
        });
        KeyHandler keyHandler = new KeyHandler();
        ActionRegistry a = new ActionRegistry();
        keyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0), new DeleteAction(this.viewer));
        keyHandler.put(KeyStroke.getPressed('+', SWT.KEYPAD_ADD, 0), new ZoomAction(this, 1, null));
        keyHandler.put(KeyStroke.getPressed('-', SWT.KEYPAD_SUBTRACT, 0), new ZoomAction(this, -1, null));
        keyHandler.put(KeyStroke.getPressed(SWT.F2, 0), a.getAction(GEFActionConstants.DIRECT_EDIT));
        this.viewer.setKeyHandler(keyHandler);
        this.higlightColor = ColorConstants.yellow;
        this.createCombos();
        return composite;
    }
