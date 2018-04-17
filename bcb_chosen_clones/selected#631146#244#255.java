    protected static SdlBpelEngine createNewSdlEngine(ISdlQueueManager aQueueManager, ISdlProcessManager aProcessManager, ISdlAlarmManager aAlarmManager, ISdlLockManager aLockManager) throws SdlException {
        logger.debug("createNewEngine(ISdlQueueManager, ISdlProcessManager, ISdlAlarmManager, ISdlLockManager)");
        String engineClass = getSdlEngineConfig().getEntry(ISdlEngineConfiguration.ENGINE_IMPL_ENTRY, SdlBpelEngine.class.getName());
        try {
            Class clazz = Class.forName(engineClass);
            Constructor cons = clazz.getConstructor(new Class[] { ISdlEngineConfiguration.class, ISdlQueueManager.class, ISdlProcessManager.class, ISdlAlarmManager.class, ISdlLockManager.class });
            return (SdlBpelEngine) cons.newInstance(new Object[] { getSdlEngineConfig(), aQueueManager, aProcessManager, aAlarmManager, aLockManager });
        } catch (Exception e) {
            logger.error("Error: " + e);
            throw new SdlException("Error creating engine.", e);
        }
    }
