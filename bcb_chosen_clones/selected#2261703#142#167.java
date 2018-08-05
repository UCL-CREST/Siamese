    static Object buildObject(Hashtable ht, Class expectedType) throws XmlRpcException {
        Object o;
        ObjectRegistry reg = getObjectRegistry();
        try {
            String className = (String) ht.remove(OBJECT_TYPE);
            int oid = Integer.parseInt(className.substring(className.indexOf(",") + 1));
            o = reg.getObject(oid);
            if (o == null) {
                className = className.substring(0, className.indexOf(","));
                Class cl = Class.forName(className);
                if (!expectedType.isAssignableFrom(cl)) {
                    throw new XmlRpcException("Field type does not match. Cannot convert " + cl.getName() + " into " + expectedType.getName());
                }
                Constructor constructor = cl.getConstructor(new Class[0]);
                constructor.setAccessible(true);
                o = constructor.newInstance(new Object[0]);
                reg.registerObject(o, oid);
                populateObject(ht, o);
            }
        } catch (Exception e) {
            throw new XmlRpcException("Unable to reconstruct object: " + e.getClass().getName() + ": " + e.getMessage());
        } finally {
            freeObjectRegistry(reg);
        }
        return o;
    }
