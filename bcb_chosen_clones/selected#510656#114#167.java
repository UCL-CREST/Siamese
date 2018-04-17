    private ScrollingGraphicalViewer createGraphicalViewer(final Composite parent) {
        final ScrollingGraphicalViewer viewer = new ScrollingGraphicalViewer();
        viewer.createControl(parent);
        _root = new EditRootEditPart();
        viewer.setRootEditPart(_root);
        getEditDomain().addViewer(viewer);
        getSite().setSelectionProvider(viewer);
        viewer.setEditPartFactory(getEditPartFactory());
        final KeyHandler keyHandler = new GraphicalViewerKeyHandler(viewer) {

            @SuppressWarnings("unchecked")
            @Override
            public boolean keyPressed(final KeyEvent event) {
                if (event.stateMask == SWT.MOD1 && event.keyCode == SWT.DEL) {
                    final List<? extends EditorPart> objects = viewer.getSelectedEditParts();
                    if (objects == null || objects.isEmpty()) return true;
                    final GroupRequest deleteReq = new GroupRequest(RequestConstants.REQ_DELETE);
                    final CompoundCommand compoundCmd = new CompoundCommand("Delete");
                    for (int i = 0; i < objects.size(); i++) {
                        final EditPart object = (EditPart) objects.get(i);
                        deleteReq.setEditParts(object);
                        final Command cmd = object.getCommand(deleteReq);
                        if (cmd != null) compoundCmd.add(cmd);
                    }
                    getCommandStack().execute(compoundCmd);
                    return true;
                }
                if (event.stateMask == SWT.MOD3 && (event.keyCode == SWT.ARROW_DOWN || event.keyCode == SWT.ARROW_LEFT || event.keyCode == SWT.ARROW_RIGHT || event.keyCode == SWT.ARROW_UP)) {
                    final List<? extends EditorPart> objects = viewer.getSelectedEditParts();
                    if (objects == null || objects.isEmpty()) return true;
                    final GroupRequest moveReq = new ChangeBoundsRequest(RequestConstants.REQ_MOVE);
                    final CompoundCommand compoundCmd = new CompoundCommand("Move");
                    for (int i = 0; i < objects.size(); i++) {
                        final EditPart object = (EditPart) objects.get(i);
                        moveReq.setEditParts(object);
                        final LocationCommand cmd = (LocationCommand) object.getCommand(moveReq);
                        if (cmd != null) {
                            cmd.setLocation(new Point(event.keyCode == SWT.ARROW_LEFT ? -1 : event.keyCode == SWT.ARROW_RIGHT ? 1 : 0, event.keyCode == SWT.ARROW_DOWN ? 1 : event.keyCode == SWT.ARROW_UP ? -1 : 0));
                            cmd.setRelative(true);
                            compoundCmd.add(cmd);
                        }
                    }
                    getCommandStack().execute(compoundCmd);
                    return true;
                }
                return super.keyPressed(event);
            }
        };
        keyHandler.put(KeyStroke.getPressed(SWT.F2, 0), getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
        viewer.setKeyHandler(keyHandler);
        viewer.setContents(getEditorInput().getAdapter(NamedUuidEntity.class));
        viewer.addDropTargetListener(createTransferDropTargetListener(viewer));
        return viewer;
    }
