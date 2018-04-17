    private void dumpConfig() throws Exception {
        IOUtils.copy(new FileInputStream(m_snmpConfigFile), System.out);
    }
