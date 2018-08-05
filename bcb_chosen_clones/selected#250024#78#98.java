    public Object createInst() {
        try {
            Object z;
            if (_prototype != null) {
                z = _prototype.createSame();
            } else if (_ownerObject == null) {
                z = getCls().newInstance();
            } else {
                Constructor[] c = getCls().getConstructors();
                z = c[0].newInstance(_ownerObject);
            }
            if (_initers != null) {
                for (IInitObject initer : _initers) {
                    initer.initObject(z);
                }
            }
            return z;
        } catch (Exception e) {
            throw new MfError(e, "Ошибка при создании объекта класса [{0}]", getIdObject());
        }
    }
