    public static synchronized IParameterDialog createDialog(Class<? extends ISerializable> valcls, Composite c) {
        String valclsname = valcls.getName();
        Class<?> dcls = dialogs.get(valclsname);
        if (dcls == null) return null;
        IParameterDialog dialog = null;
        try {
            Constructor<?> ctor = dcls.getConstructor(new Class<?>[] { Composite.class, int.class });
            dialog = (IParameterDialog) ctor.newInstance(new Object[] { c, SWT.NONE });
        } catch (Exception e) {
            Application.logexcept("Could not create parameter dialog.", e);
        }
        return dialog;
    }
