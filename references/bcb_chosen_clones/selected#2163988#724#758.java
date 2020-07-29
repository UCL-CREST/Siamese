    public String usn() {
        if (uuid == null) {
            uuid = getConfiguration().getUuid();
            if (uuid == null) {
                NetworkInterface ni = null;
                try {
                    if (configuration.getServerHostname() != null && configuration.getServerHostname().length() > 0) {
                        ni = NetworkInterface.getByInetAddress(InetAddress.getByName(configuration.getServerHostname()));
                    } else if (get().getServer().getNi() != null) {
                        ni = get().getServer().getNi();
                    }
                    if (ni != null) {
                        byte[] addr = PMSUtil.getHardwareAddress(ni);
                        if (addr != null) {
                            uuid = UUID.nameUUIDFromBytes(addr).toString();
                            logger.info(String.format("Generated new UUID based on the MAC address of the network adapter '%s'", ni.getDisplayName()));
                        }
                    }
                } catch (Throwable e) {
                }
                if (uuid == null) {
                    uuid = UUID.randomUUID().toString();
                    logger.info("Generated new random UUID");
                }
                getConfiguration().setUuid(uuid);
                try {
                    getConfiguration().save();
                } catch (ConfigurationException e) {
                    logger.error("Failed to save configuration with new UUID", e);
                }
            }
            logger.info("Using the following UUID configured in PMS.conf: " + uuid);
        }
        return "uuid:" + uuid;
    }
