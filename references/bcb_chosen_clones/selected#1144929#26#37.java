    @SuppressWarnings("unchecked")
    public GarmanKlassOpenClose(final Class<? extends GarmanKlassAbstract> classT, final double y, final double marketOpenFraction, final double a) {
        this.classT = classT;
        this.delegate = null;
        try {
            delegate = (T) classT.getConstructor(double.class).newInstance(y);
        } catch (final Exception e) {
            throw new LibraryException(e);
        }
        this.f = marketOpenFraction;
        this.a = a;
    }
