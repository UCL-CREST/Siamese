    protected void initSipStack() throws LifecycleException {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Initializing SIP stack");
            }
            initializeSystemPortProperties();
            String catalinaBase = getCatalinaBase();
            if (sipStackPropertiesFileLocation != null && !sipStackPropertiesFileLocation.startsWith("file:///")) {
                sipStackPropertiesFileLocation = "file:///" + catalinaBase.replace(File.separatorChar, '/') + "/" + sipStackPropertiesFileLocation;
            }
            boolean isPropsLoaded = false;
            if (sipStackProperties == null) {
                sipStackProperties = new Properties();
            } else {
                isPropsLoaded = true;
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Loading SIP stack properties from following file : " + sipStackPropertiesFileLocation);
            }
            if (sipStackPropertiesFileLocation != null) {
                File sipStackPropertiesFile = null;
                URL url = null;
                try {
                    url = new URL(sipStackPropertiesFileLocation);
                } catch (MalformedURLException e) {
                    logger.fatal("Cannot find the sip stack properties file ! ", e);
                    throw new IllegalArgumentException("The Default Application Router file Location : " + sipStackPropertiesFileLocation + " is not valid ! ", e);
                }
                try {
                    sipStackPropertiesFile = new File(new URI(sipStackPropertiesFileLocation));
                } catch (URISyntaxException e) {
                    sipStackPropertiesFile = new File(url.getPath());
                }
                FileInputStream sipStackPropertiesInputStream = null;
                try {
                    sipStackPropertiesInputStream = new FileInputStream(sipStackPropertiesFile);
                    sipStackProperties.load(sipStackPropertiesInputStream);
                } catch (Exception e) {
                    logger.warn("Could not find or problem when loading the sip stack properties file : " + sipStackPropertiesFileLocation, e);
                } finally {
                    if (sipStackPropertiesInputStream != null) {
                        try {
                            sipStackPropertiesInputStream.close();
                        } catch (IOException e) {
                            logger.error("fail to close the following file " + sipStackPropertiesFile.getAbsolutePath(), e);
                        }
                    }
                }
                String debugLog = sipStackProperties.getProperty(DEBUG_LOG_STACK_PROP);
                if (debugLog != null && debugLog.length() > 0 && !debugLog.startsWith("file:///")) {
                    sipStackProperties.setProperty(DEBUG_LOG_STACK_PROP, catalinaBase + "/" + debugLog);
                }
                String serverLog = sipStackProperties.getProperty(SERVER_LOG_STACK_PROP);
                if (serverLog != null && serverLog.length() > 0 && !serverLog.startsWith("file:///")) {
                    sipStackProperties.setProperty(SERVER_LOG_STACK_PROP, catalinaBase + "/" + serverLog);
                }
                sipStackProperties.setProperty(AUTOMATIC_DIALOG_SUPPORT_STACK_PROP, "off");
                sipStackProperties.setProperty(LOOSE_DIALOG_VALIDATION, "true");
                sipStackProperties.setProperty(PASS_INVITE_NON_2XX_ACK_TO_LISTENER, "true");
                isPropsLoaded = true;
            } else {
                logger.warn("no sip stack properties file defined ");
            }
            if (!isPropsLoaded) {
                logger.warn("loading default Mobicents Sip Servlets sip stack properties");
                sipStackProperties.setProperty("gov.nist.javax.sip.LOG_MESSAGE_CONTENT", "true");
                sipStackProperties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "32");
                sipStackProperties.setProperty(DEBUG_LOG_STACK_PROP, catalinaBase + "/" + "mss-jsip-" + getName() + "-debug.txt");
                sipStackProperties.setProperty(SERVER_LOG_STACK_PROP, catalinaBase + "/" + "mss-jsip-" + getName() + "-messages.xml");
                sipStackProperties.setProperty("javax.sip.STACK_NAME", "mss-" + getName());
                sipStackProperties.setProperty(AUTOMATIC_DIALOG_SUPPORT_STACK_PROP, "off");
                sipStackProperties.setProperty("gov.nist.javax.sip.DELIVER_UNSOLICITED_NOTIFY", "true");
                sipStackProperties.setProperty("gov.nist.javax.sip.THREAD_POOL_SIZE", "64");
                sipStackProperties.setProperty("gov.nist.javax.sip.REENTRANT_LISTENER", "true");
                sipStackProperties.setProperty("gov.nist.javax.sip.MAX_FORK_TIME_SECONDS", "0");
                sipStackProperties.setProperty(LOOSE_DIALOG_VALIDATION, "true");
                sipStackProperties.setProperty(PASS_INVITE_NON_2XX_ACK_TO_LISTENER, "true");
                sipStackProperties.setProperty("gov.nist.javax.sip.AUTOMATIC_DIALOG_ERROR_HANDLING", "false");
            }
            if (sipStackProperties.get(TCP_POST_PARSING_THREAD_POOL_SIZE) == null) {
                sipStackProperties.setProperty(TCP_POST_PARSING_THREAD_POOL_SIZE, "30");
            }
            if (dnsServerLocatorClass != null && dnsServerLocatorClass.trim().length() > 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Sip Stack " + sipStackProperties.getProperty("javax.sip.STACK_NAME") + " will be using " + dnsServerLocatorClass + " as DNSServerLocator");
                }
                try {
                    Class[] paramTypes = new Class[0];
                    Constructor dnsServerLocatorConstructor = Class.forName(dnsServerLocatorClass).getConstructor(paramTypes);
                    Object[] conArgs = new Object[0];
                    DNSServerLocator dnsServerLocator = (DNSServerLocator) dnsServerLocatorConstructor.newInstance(conArgs);
                    sipApplicationDispatcher.setDNSServerLocator(dnsServerLocator);
                    if (sipStackProperties.getProperty("javax.sip.ROUTER_PATH") == null) {
                        sipStackProperties.setProperty("javax.sip.ROUTER_PATH", DNSAwareRouter.class.getCanonicalName());
                    }
                } catch (Exception e) {
                    logger.error("Couldn't set the AddressResolver " + addressResolverClass, e);
                    throw e;
                }
            } else {
                if (logger.isInfoEnabled()) {
                    logger.info("no DNSServerLocator will be used since none has been specified.");
                }
            }
            String serverHeaderValue = sipStackProperties.getProperty(SERVER_HEADER);
            if (serverHeaderValue != null) {
                List<String> serverHeaderList = new ArrayList<String>();
                StringTokenizer stringTokenizer = new StringTokenizer(serverHeaderValue, ",");
                while (stringTokenizer.hasMoreTokens()) {
                    serverHeaderList.add(stringTokenizer.nextToken());
                }
                ServerHeader serverHeader = sipApplicationDispatcher.getSipFactory().getHeaderFactory().createServerHeader(serverHeaderList);
                ((MessageFactoryExt) sipApplicationDispatcher.getSipFactory().getMessageFactory()).setDefaultServerHeader(serverHeader);
            }
            String userAgent = sipStackProperties.getProperty(USER_AGENT_HEADER);
            if (userAgent != null) {
                List<String> userAgentList = new ArrayList<String>();
                StringTokenizer stringTokenizer = new StringTokenizer(userAgent, ",");
                while (stringTokenizer.hasMoreTokens()) {
                    userAgentList.add(stringTokenizer.nextToken());
                }
                UserAgentHeader userAgentHeader = sipApplicationDispatcher.getSipFactory().getHeaderFactory().createUserAgentHeader(userAgentList);
                ((MessageFactoryExt) sipApplicationDispatcher.getSipFactory().getMessageFactory()).setDefaultUserAgentHeader(userAgentHeader);
            }
            if (balancers != null) {
                if (sipStackProperties.get(LoadBalancerHeartBeatingService.LB_HB_SERVICE_CLASS_NAME) == null) {
                    sipStackProperties.put(LoadBalancerHeartBeatingService.LB_HB_SERVICE_CLASS_NAME, LoadBalancerHeartBeatingServiceImpl.class.getCanonicalName());
                }
                if (sipStackProperties.get(LoadBalancerHeartBeatingService.BALANCERS) == null) {
                    sipStackProperties.put(LoadBalancerHeartBeatingService.BALANCERS, balancers);
                }
            }
            String replicationStrategyString = sipStackProperties.getProperty(ClusteredSipStack.REPLICATION_STRATEGY_PROPERTY);
            if (replicationStrategyString == null) {
                replicationStrategyString = ReplicationStrategy.ConfirmedDialog.toString();
            }
            boolean replicateApplicationData = false;
            if (replicationStrategyString.equals(ReplicationStrategy.EarlyDialog.toString())) {
                replicateApplicationData = true;
            }
            if (replicationStrategyString != null) {
                replicationStrategy = ReplicationStrategy.valueOf(replicationStrategyString);
            }
            sipStackProperties.put(ClusteredSipStack.REPLICATION_STRATEGY_PROPERTY, replicationStrategyString);
            sipStackProperties.put(ClusteredSipStack.REPLICATE_APPLICATION_DATA, Boolean.valueOf(replicateApplicationData).toString());
            if (logger.isInfoEnabled()) {
                logger.info("Mobicents Sip Servlets sip stack properties : " + sipStackProperties);
            }
            sipStack = sipApplicationDispatcher.getSipFactory().getJainSipFactory().createSipStack(sipStackProperties);
            LoadBalancerHeartBeatingService loadBalancerHeartBeatingService = null;
            if (sipStack instanceof ClusteredSipStack) {
                loadBalancerHeartBeatingService = ((ClusteredSipStack) sipStack).getLoadBalancerHeartBeatingService();
                if ((this.container != null) && (this.container instanceof Engine) && ((Engine) container).getJvmRoute() != null) {
                    final String jvmRoute = ((Engine) container).getJvmRoute();
                    if (jvmRoute != null) {
                        loadBalancerHeartBeatingService.setJvmRoute(jvmRoute);
                        setJvmRoute(jvmRoute);
                    }
                }
            }
            if (sipApplicationDispatcher != null && loadBalancerHeartBeatingService != null && sipApplicationDispatcher instanceof LoadBalancerHeartBeatingListener) {
                loadBalancerHeartBeatingService.addLoadBalancerHeartBeatingListener((LoadBalancerHeartBeatingListener) sipApplicationDispatcher);
            }
            if (sipStack instanceof SipStackExt && addressResolverClass != null && addressResolverClass.trim().length() > 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Sip Stack " + sipStack.getStackName() + " will be using " + addressResolverClass + " as AddressResolver");
                }
                try {
                    Class[] paramTypes = new Class[1];
                    paramTypes[0] = SipApplicationDispatcher.class;
                    Constructor addressResolverConstructor = Class.forName(addressResolverClass).getConstructor(paramTypes);
                    Object[] conArgs = new Object[1];
                    conArgs[0] = sipApplicationDispatcher;
                    AddressResolver addressResolver = (AddressResolver) addressResolverConstructor.newInstance(conArgs);
                    ((SipStackExt) sipStack).setAddressResolver(addressResolver);
                } catch (Exception e) {
                    logger.error("Couldn't set the AddressResolver " + addressResolverClass, e);
                    throw e;
                }
            } else {
                if (logger.isInfoEnabled()) {
                    logger.info("no AddressResolver will be used since none has been specified.");
                }
            }
            if (logger.isInfoEnabled()) {
                logger.info("SIP stack initialized");
            }
        } catch (Exception ex) {
            throw new LifecycleException("A problem occured while initializing the SIP Stack", ex);
        }
    }
