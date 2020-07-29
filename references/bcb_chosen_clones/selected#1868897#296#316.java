    public ServiceAdapterIfc deploy(String session, String name, byte jarBytes[], String jarName, String serviceClass, String serviceInterface) throws RemoteException, MalformedURLException, StartServiceException, SessionException {
        try {
            File jarFile = new File(jarName);
            jarName = jarFile.getName();
            String jarName2 = jarName;
            jarFile = new File(jarName2);
            int n = 0;
            while (jarFile.exists()) {
                jarName2 = jarName + n++;
                jarFile = new File(jarName2);
            }
            FileOutputStream fos = new FileOutputStream(jarName2);
            IOUtils.copy(new ByteArrayInputStream(jarBytes), fos);
            SCClassLoader cl = new SCClassLoader(new URL[] { new URL("file://" + jarFile.getAbsolutePath()) }, getMasterNode().getSCClassLoaderCounter());
            return startService(session, name, serviceClass, serviceInterface, cl);
        } catch (SessionException e) {
            throw e;
        } catch (Exception e) {
            throw new StartServiceException("Could not deploy service: " + e.getMessage(), e);
        }
    }
