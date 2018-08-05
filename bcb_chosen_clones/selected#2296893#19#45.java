    @SuppressWarnings("unchecked")
    public static Session getJCRSession(String host, boolean crx) throws RepositoryException {
        try {
            Class providerClass;
            if (crx) {
                providerClass = Class.forName("com.ceg.online.jackknife.session.CRXProvider");
            } else {
                providerClass = Class.forName("com.ceg.online.jackknife.session.JackrabbitProvider");
            }
            Constructor ctr = providerClass.getConstructor(new Class[0]);
            JCRProvider provider = (JCRProvider) ctr.newInstance(new Object[0]);
            return provider.getSession(host);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
