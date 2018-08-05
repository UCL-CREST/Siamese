    public ArchiveType getArchiveType(SectionInterface section) {
        ArchiveType archiveType = null;
        String name = null;
        try {
            name = PropertyHelper.getProperty(_props, section.getName() + ".archiveType");
        } catch (NullPointerException e) {
            return null;
        }
        Constructor constructor = null;
        Class[] classParams = { Archive.class, SectionInterface.class, Properties.class };
        Object[] objectParams = { this, section, _props };
        try {
            constructor = Class.forName("org.drftpd.mirroring.archivetypes." + name).getConstructor(classParams);
            archiveType = (ArchiveType) constructor.newInstance(objectParams);
        } catch (Exception e2) {
            logger.error("Unable to load ArchiveType for section " + section.getName(), e2);
        }
        return archiveType;
    }
