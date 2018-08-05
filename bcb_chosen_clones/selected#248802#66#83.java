    public static PXBundle bundleWithURL(URL url, boolean createIfNeeded) {
        if (url == null) {
            return null;
        }
        String protocol = url.getProtocol();
        Class handler = _handlerForProtocol(protocol);
        if (handler == null) {
            throw new IllegalArgumentException("PXBundle does not support bundles with protocol '" + protocol + "'.");
        }
        try {
            Constructor constructor = handler.getConstructor(DefaultConstructorArguments);
            PXBundle bundle = (PXBundle) constructor.newInstance(new Object[] { url, new Boolean(createIfNeeded) });
            return bundle;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
