    private ClusterCommunicator initialiseClusterCommunication(String ccClassName, Topology topology) throws MapPSOConfigurationException {
        logger.info("Initialising cluster communication ...");
        ClusterCommunicator clusterCommunicator;
        Class<?> classInstance;
        try {
            classInstance = Class.forName(ccClassName);
            Constructor<?> cons = classInstance.getConstructor(new Class[] { Topology.class });
            clusterCommunicator = (ClusterCommunicator) cons.newInstance(topology);
        } catch (ClassNotFoundException e) {
            final String errMsg = "Cannot find specified cluster communication class " + ccClassName;
            logger.error(errMsg, e);
            throw new MapPSOConfigurationException(errMsg, e);
        } catch (Exception e) {
            final String errMsg = "Unable to instantiate specified cluster communication class " + ccClassName;
            logger.error(errMsg, e);
            throw new MapPSOConfigurationException(errMsg, e);
        }
        if (logger.isDebugEnabled()) logger.debug("Created cluster communication " + clusterCommunicator.getClass().getName());
        return clusterCommunicator;
    }
