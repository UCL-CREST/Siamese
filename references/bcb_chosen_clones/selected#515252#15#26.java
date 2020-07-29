    public IRest CreateInstance(Class RestImp) {
        try {
            Constructor constructor = RestImp.getConstructor(new Class[] {});
            Object Rest = constructor.newInstance(new Object[] {});
            if (Rest instanceof IRest) {
                return (IRest) Rest;
            }
        } catch (Exception ex) {
            Logger.getLogger(RestFactoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
