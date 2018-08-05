    public CampoAbstrato<T> novo(Field campo) {
        try {
            return this.getClass().getConstructor(Field.class).newInstance(campo);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
