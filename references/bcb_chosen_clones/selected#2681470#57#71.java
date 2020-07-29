    private TrafficType getTrafficType(int count, String type, Properties props) {
        TrafficType trafficType = null;
        Class<?>[] SIG = { Properties.class, int.class, String.class };
        if (!_typesMap.containsKey(type)) {
            logger.error("Traffic Type: " + type + " wasn't loaded.");
        } else {
            try {
                Class<TrafficType> clazz = _typesMap.get(type);
                trafficType = clazz.getConstructor(SIG).newInstance(new Object[] { props, count, type.toLowerCase() });
            } catch (Exception e) {
                logger.error("Unable to load TrafficType for section " + count + ".type=" + type, e);
            }
        }
        return trafficType;
    }
