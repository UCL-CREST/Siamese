    protected DicoModelAbstract createDico(final String _name, final String _version, final int _language) {
        try {
            final Class c = Class.forName(packageName_ + DicoGenerator.getClassName(_name, _version));
            return (DicoModelAbstract) c.getConstructor(new Class[] { int.class }).newInstance(new Object[] { new Integer(_language) });
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }
