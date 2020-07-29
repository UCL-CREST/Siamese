    @SuppressWarnings("unchecked")
    public DataSet(DataSet DS) {
        this.ojbConcreteClass = this.getClass().getName();
        if (DS == null) {
            return;
        }
        this.setNom(DS.getNom());
        this.setTypeBD(DS.getTypeBD());
        this.setModele(DS.getModele());
        this.setZone(DS.getZone());
        this.setDate(DS.getDate());
        this.setCommentaire(DS.getCommentaire());
        for (IPopulation<? extends IFeature> p : DS.getPopulations()) {
            try {
                IPopulation<? extends IFeature> f = p.getClass().getConstructor(p.getClass()).newInstance(p);
                this.populations.add(f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
