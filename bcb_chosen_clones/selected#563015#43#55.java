    static Provider buildProvider(String providerKlazz, String providerArgExpression) {
        try {
            Object arg = resolveArgExpression(providerArgExpression);
            Class cls = Class.forName(providerKlazz);
            Constructor ctor = cls.getConstructor(new Class[] { arg.getClass() });
            Provider provider = (Provider) ctor.newInstance(new Object[] { arg });
            return provider;
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
