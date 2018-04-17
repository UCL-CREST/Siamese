    public static void loadConfig(DeviceEntry defaultDevice, EmulatorContext emulatorContext) {
        Config.defaultDevice = defaultDevice;
        Config.emulatorContext = emulatorContext;
        File configFile = new File(getConfigPath(), "config2.xml");
        try {
            if (configFile.exists()) {
                loadConfigFile("config2.xml");
            } else {
                configFile = new File(getConfigPath(), "config.xml");
                if (configFile.exists()) {
                    loadConfigFile("config.xml");
                    for (Enumeration e = getDeviceEntries().elements(); e.hasMoreElements(); ) {
                        DeviceEntry entry = (DeviceEntry) e.nextElement();
                        if (!entry.canRemove()) {
                            continue;
                        }
                        removeDeviceEntry(entry);
                        File src = new File(getConfigPath(), entry.getFileName());
                        File dst = File.createTempFile("dev", ".jar", getConfigPath());
                        IOUtils.copyFile(src, dst);
                        entry.setFileName(dst.getName());
                        addDeviceEntry(entry);
                    }
                } else {
                    createDefaultConfigXml();
                }
                saveConfig();
            }
        } catch (IOException ex) {
            Logger.error(ex);
            createDefaultConfigXml();
        } finally {
            if (configXml == null) {
                createDefaultConfigXml();
            }
        }
        urlsMRU.read(configXml.getChildOrNew("files").getChildOrNew("recent"));
        initSystemProperties();
    }
