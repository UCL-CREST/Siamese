    private void loadSectionManager(Properties cfg) {
        try {
            Class cl = Class.forName(cfg.getProperty("sectionmanager", "org.drftpd.sections.def.SectionManager"));
            Constructor c = cl.getConstructor(new Class[] { ConnectionManager.class });
            _sections = (SectionManagerInterface) c.newInstance(new Object[] { this });
        } catch (Exception e) {
            throw new FatalException(e);
        }
    }
