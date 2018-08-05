    public Entity createEntity(String type, String instname) throws Exception {
        Entity ent = null;
        try {
            Class entclass = findEntityClass(type);
            Constructor<String> ctor = entclass.getConstructor(String.class);
            Object obj = ctor.newInstance(instname);
            if (obj instanceof Entity) {
                ((BaseEntity) obj).setInstanceName(instname);
                ent = (Entity) obj;
            } else {
                throw new Exception("requested entity " + type + " is not compatible to BaseEntity.");
            }
        } catch (ClassNotFoundException e) {
            throw new Exception("could not create entity " + type + ".\n  reason: " + e);
        } catch (Exception e) {
            throw new Exception("could not create entity " + type + ".\n  reason: " + e);
        }
        return ent;
    }
