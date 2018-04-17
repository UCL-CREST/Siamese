    public RemoteAccess(FrameworkConfiguration configuration) throws NoDefaultRemoteFwException {
        config = configuration;
        securityModel = new DbSecurityModel(configuration);
        nameModel = new NameModel(configuration);
        if (configuration.isSecured()) {
            try {
                ICredentialManager credentialManager = (ICredentialManager) Class.forName("com.abiquo.framework.security.credentials.CredentialManager").newInstance();
                defaultCredential = credentialManager.getCredential(configuration.getCommunityCertPath(), configuration.getCommunityKeyPath());
                comm = getCommunicationSecure(nameModel, configuration, defaultCredential);
                Class<?> commClass = Class.forName("com.abiquo.framework.model.db.EnhancedDbSecurityModel");
                Class<?>[] types = new Class[] { FrameworkConfiguration.class };
                Constructor<?> securityModelConstructor = commClass.getConstructor(types);
                securityModel = (ISecurityModel) securityModelConstructor.newInstance(configuration);
            } catch (Exception e) {
                final String msg = "Secure communication with the remote framework could not be stablished";
                logger.error(msg, e);
                throw new NoDefaultRemoteFwException(msg, e);
            }
        } else {
            comm = new CommunicationSimple(nameModel, configuration);
        }
        nameFramework = configuration.getFqdn();
        locateDefaultRemoteFw();
        model = new QueryModel(this);
    }
