    @Override
    @SuppressWarnings("unchecked")
    protected void createActions() {
        super.createActions();
        ActionRegistry registry = getActionRegistry();
        Class<?> actions[] = { CopyAction.class, CutAction.class, PasteAction.class, PrintAction.class, SelectAllAction.class, SetRefinementAction.class };
        for (Class<?> clz : actions) {
            try {
                Constructor<?> ctor = clz.getConstructor(IWorkbenchPart.class);
                IAction action = (IAction) ctor.newInstance(this);
                registry.registerAction(action);
                getSelectionActions().add(action.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
