    private Object getNonpersistableObjectFromJSON(final JSONObject jsonobj, final Class cls, final ClassLoaderResolver clr) {
        if (cls.getName().equals("com.google.appengine.api.users.User")) {
            return getComGoogleAppengineApiUsersUserFromJSON(jsonobj, cls, clr);
        } else if (cls.getName().equals("com.google.appengine.api.datastore.Key")) {
            return getComGoogleAppengineApiDatastoreKeyFromJSON(jsonobj, cls, clr);
        } else {
            try {
                return AccessController.doPrivileged(new PrivilegedAction() {

                    public Object run() {
                        try {
                            Constructor c = ClassUtils.getConstructorWithArguments(cls, new Class[] {});
                            c.setAccessible(true);
                            Object obj = c.newInstance(new Object[] {});
                            String[] fieldNames = JSONObject.getNames(jsonobj);
                            for (int i = 0; i < jsonobj.length(); i++) {
                                if (!fieldNames[i].equals("class")) {
                                    Field field = cls.getField(fieldNames[i]);
                                    field.setAccessible(true);
                                    field.set(obj, jsonobj.get(fieldNames[i]));
                                }
                            }
                            return obj;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                });
            } catch (SecurityException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
