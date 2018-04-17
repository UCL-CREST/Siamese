    public static IAuthenificator getAuthenificator(String authenificatorClassName, PaloConfiguration cfg) throws PaloWebViewerException {
        try {
            Class authClass = Class.forName(authenificatorClassName);
            Constructor constructor = authClass.getConstructor(new Class[] { PaloConfiguration.class });
            IAuthenificator authenificator = (IAuthenificator) constructor.newInstance(new Object[] { cfg });
            return authenificator;
        } catch (Exception e) {
            throw new PaloWebViewerException("Can't create authenificator", e);
        }
    }
