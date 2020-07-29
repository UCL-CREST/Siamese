    @Override
    protected ServletRequestDataBinder createBinder(HttpServletRequest request, Object command) throws Exception {
        if (binderTemplate == null) {
            return super.createBinder(request, command);
        }
        Constructor<?> c = binderTemplate.getClass().getConstructor(new Class[] { Object.class, String.class, DataBinder.class });
        ServletRequestDataBinder binder = (ServletRequestDataBinder) c.newInstance(new Object[] { command, getCommandName(), binderTemplate });
        if (getMessageCodesResolver() != null) {
            binder.setMessageCodesResolver(getMessageCodesResolver());
        }
        initBinder(request, binder);
        return binder;
    }
