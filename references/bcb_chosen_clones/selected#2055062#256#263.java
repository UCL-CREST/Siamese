    private Communication getCommunicationSecure(NameModel nameModel, FrameworkConfiguration configuration, GSSCredential credential) throws Exception {
        Communication commSecure;
        Class<?> commClass = Class.forName("com.abiquo.framework.comm.CommunicationSecure");
        Class<?>[] types = new Class[] { NameModel.class, FrameworkConfiguration.class, GSSCredential.class };
        Constructor<?> commConstructor = commClass.getConstructor(types);
        commSecure = (Communication) commConstructor.newInstance(nameModel, configuration, defaultCredential);
        return commSecure;
    }
