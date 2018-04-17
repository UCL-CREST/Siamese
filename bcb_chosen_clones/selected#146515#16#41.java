    public static void convert(PGStream stream, Properties info, Logger logger) throws IOException, PSQLException {
        logger.debug("converting regular socket connection to ssl");
        SSLSocketFactory factory;
        String classname = info.getProperty("sslfactory");
        if (classname == null) {
            factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        } else {
            Object[] args = { info.getProperty("sslfactoryarg") };
            Constructor ctor;
            Class factoryClass;
            try {
                factoryClass = Class.forName(classname);
                try {
                    ctor = factoryClass.getConstructor(new Class[] { String.class });
                } catch (NoSuchMethodException nsme) {
                    ctor = factoryClass.getConstructor((Class[]) null);
                    args = null;
                }
                factory = (SSLSocketFactory) ctor.newInstance(args);
            } catch (Exception e) {
                throw new PSQLException(GT.tr("The SSLSocketFactory class provided {0} could not be instantiated.", classname), PSQLState.CONNECTION_FAILURE, e);
            }
        }
        Socket newConnection = factory.createSocket(stream.getSocket(), stream.getHost(), stream.getPort(), true);
        stream.changeSocket(newConnection);
    }
