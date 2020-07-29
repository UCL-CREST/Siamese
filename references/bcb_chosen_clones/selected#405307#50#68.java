    public ArchiveType getArchiveType(int count, String type, SectionInterface sec, Properties props) {
        ArchiveType archiveType = null;
        Class<?>[] SIG = { Archive.class, SectionInterface.class, Properties.class, int.class };
        if (!_typesMap.containsKey(type)) {
            logger.error("Archive Type: " + type + " wasn't loaded.");
        } else {
            if (!sec.getName().isEmpty()) {
                try {
                    Class<ArchiveType> clazz = _typesMap.get(type);
                    archiveType = clazz.getConstructor(SIG).newInstance(new Object[] { this, sec, props, count });
                } catch (Exception e) {
                    logger.error("Unable to load ArchiveType for section " + count + "." + type, e);
                }
            } else {
                logger.error("Unable to load Section for Archive " + count + "." + type);
            }
        }
        return archiveType;
    }
