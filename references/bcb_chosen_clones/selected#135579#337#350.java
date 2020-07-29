    private static Object newDAO(Class clazz) throws InstantiationException {
        try {
            Constructor c = clazz.getConstructor(new Class[] {});
            if (c == null) throw new InstantiationException("No zero argument construtor found");
            Object dao = c.newInstance((Object[]) null);
            log.debug("Created Object : " + dao);
            return dao;
        } catch (InstantiationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Can't create DAO object - " + e.getMessage(), e);
            throw new InstantiationException(e.getMessage());
        }
    }
