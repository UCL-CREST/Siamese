        public Shell createShell(Object handle) throws Exception {
            if (NativeInterface.isInProcess()) {
                Canvas canvas = (Canvas) handle;
                ComponentListener[] componentListeners = canvas.getComponentListeners();
                Shell shell = SWT_AWT.new_Shell(NativeInterface.getDisplay(), canvas);
                for (ComponentListener componentListener : canvas.getComponentListeners()) {
                    canvas.removeComponentListener(componentListener);
                }
                for (ComponentListener componentListener : componentListeners) {
                    canvas.addComponentListener(componentListener);
                }
                return shell;
            }
            Method shellCreationMethod = null;
            try {
                shellCreationMethod = Shell.class.getMethod(SWT.getPlatform() + "_new", Display.class, int.class);
            } catch (Exception e) {
            }
            if (shellCreationMethod != null) {
                return (Shell) shellCreationMethod.invoke(null, NativeInterface.getDisplay(), ((Number) handle).intValue());
            }
            try {
                shellCreationMethod = Shell.class.getMethod(SWT.getPlatform() + "_new", Display.class, long.class);
            } catch (Exception e) {
            }
            if (shellCreationMethod != null) {
                return (Shell) shellCreationMethod.invoke(null, NativeInterface.getDisplay(), ((Number) handle).longValue());
            }
            Constructor<Shell> shellConstructor = null;
            try {
                shellConstructor = Shell.class.getConstructor(Display.class, Shell.class, int.class, int.class);
            } catch (Exception e) {
            }
            if (shellConstructor != null) {
                shellConstructor.setAccessible(true);
                return shellConstructor.newInstance(NativeInterface.getDisplay(), null, SWT.NO_TRIM, ((Number) handle).intValue());
            }
            try {
                shellConstructor = Shell.class.getConstructor(Display.class, Shell.class, int.class, long.class);
            } catch (Exception e) {
            }
            if (shellConstructor != null) {
                shellConstructor.setAccessible(true);
                return shellConstructor.newInstance(NativeInterface.getDisplay(), null, SWT.NO_TRIM, ((Number) handle).longValue());
            }
            throw new IllegalStateException("Failed to create a Shell!");
        }
