    public static ObjectID[] sortDecending(ObjectID[] oids) {
        for (int i = 1; i < oids.length; i++) {
            ObjectID iId = oids[i];
            for (int j = 0; j < oids.length - i; j++) {
                if (oids[j].getTypePrefix() > oids[j + 1].getTypePrefix()) {
                    ObjectID temp = oids[j];
                    oids[j] = oids[j + 1];
                    oids[j + 1] = temp;
                }
            }
        }
        return oids;
    }
