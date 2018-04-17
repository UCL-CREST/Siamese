    @SuppressWarnings("unchecked")
    public Session getSession(String host) throws RepositoryException {
        Repository crxRepository;
        try {
            Class clazz = Class.forName("com.day.crx.rmi.client.CRXClientRepositoryFactory");
            Constructor constructor = clazz.getConstructor(new Class[] {});
            constructor.setAccessible(true);
            Object instance = constructor.newInstance(new Object[] {});
            Class[] params = { String.class };
            Method method = clazz.getMethod("getRepository", params);
            crxRepository = (Repository) method.invoke(instance, "//" + host + ":9901/crx");
            Credentials scred = new SimpleCredentials("admin", "admin".toCharArray());
            return crxRepository.login(scred);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
