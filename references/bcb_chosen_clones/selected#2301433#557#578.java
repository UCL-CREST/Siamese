    private String createNewObject(FedoraClient fedora, String oid) throws Exception {
        InputStream in = null;
        byte[] template = null;
        try {
            if (foxmlTemplate != null) {
                in = new FileInputStream(foxmlTemplate);
                template = IOUtils.toByteArray(in);
            } else {
                in = getClass().getResourceAsStream("/foxml_template.xml");
                template = IOUtils.toByteArray(in);
            }
        } catch (IOException ex) {
            throw new Exception("Error accessing FOXML Template, please check system configuration!");
        } finally {
            if (in != null) {
                in.close();
            }
        }
        String vitalPid = fedora.getAPIM().ingest(template, Strings.FOXML_VERSION, "ReDBox creating new object: '" + oid + "'");
        log.info("New VITAL PID: '{}'", vitalPid);
        return vitalPid;
    }
