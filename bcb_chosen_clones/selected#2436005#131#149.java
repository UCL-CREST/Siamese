    public Collection<V> createCollection(Collection coll) {
        if (this.collectionClass != null) try {
            if (coll == null) {
                return this.collectionClass.newInstance();
            } else {
                return this.collectionClass.getConstructor(new Class[] { Collection.class }).newInstance(new Object[] { coll });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } else if (this.collectionSpecimen != null) {
            try {
                final Collection<V> res = CopyUtils.copy(this.collectionSpecimen);
                if (coll != null) res.addAll(coll);
                return res;
            } catch (Exception e) {
                throw ExceptionUtils.createExn(IllegalStateException.class, "clone() failed", e);
            }
        } else return super.createCollection(coll);
    }
