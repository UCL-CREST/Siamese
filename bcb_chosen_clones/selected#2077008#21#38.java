    public Controller lookupController(HttpServletRequest request, AbstractTransformerFactory factory) {
        Class[] argsClass = new Class[] { HttpServletRequest.class };
        Object[] args = new Object[] { request };
        for (Class<? extends AbstractInspectRequest> c : classes) {
            try {
                Constructor<? extends AbstractInspectRequest> constructor = c.getConstructor(argsClass);
                AbstractInspectRequest inspectRequest = (AbstractInspectRequest) constructor.newInstance(args);
                if (inspectRequest.isValidate()) {
                    if (inspectRequest instanceof IControllerProvider) {
                        return ((IControllerProvider) inspectRequest).getController(factory);
                    }
                }
            } catch (Exception e) {
                LOG.error("Couldn't resolve AbstractInspectRequest class [" + c.getName() + "], using constructor: " + c, e);
            }
        }
        return null;
    }
