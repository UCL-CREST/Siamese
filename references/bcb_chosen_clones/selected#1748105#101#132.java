    public AmazonSimpleDB getClient(final String access, final String secret, final String endpoint) throws SQLException {
        try {
            AWSCredentials credentials = new BasicAWSCredentials(access, secret);
            AwsClientUtils clientUtils = new AwsClientUtils();
            String userAgent = clientUtils.formUserAgentString("SimpleDBEclipsePlugin", Activator.getDefault());
            ClientConfiguration config = new ClientConfiguration();
            config.setUserAgent(userAgent);
            Activator plugin = Activator.getDefault();
            if (plugin != null) {
                IProxyService proxyService = AwsToolkitCore.getDefault().getProxyService();
                if (proxyService.isProxiesEnabled()) {
                    IProxyData proxyData = proxyService.getProxyDataForHost(endpoint, IProxyData.HTTPS_PROXY_TYPE);
                    if (proxyData != null) {
                        config.setProxyHost(proxyData.getHost());
                        config.setProxyPort(proxyData.getPort());
                        if (proxyData.isRequiresAuthentication()) {
                            config.setProxyUsername(proxyData.getUserId());
                            config.setProxyPassword(proxyData.getPassword());
                        }
                    }
                }
            }
            Constructor<?> cstr = this.driverClass.getConstructor(new Class[] { AWSCredentials.class, ClientConfiguration.class });
            AmazonSimpleDB service = (AmazonSimpleDB) cstr.newInstance(new Object[] { credentials, config });
            service.setEndpoint("https://" + endpoint.trim());
            return service;
        } catch (Exception e) {
            SQLException se = new SQLException(e.getLocalizedMessage(), "08001", 8001);
            se.initCause(e);
            throw se;
        }
    }
