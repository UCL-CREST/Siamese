    private synchronized boolean createOrganization(String organizationName, HttpServletRequest req) {
        if ((organizationName == null) || (organizationName.equals(""))) {
            message = "invalid new_organization_name.";
            return false;
        }
        String tmpxml = TextUtil.xmlEscape(organizationName);
        String tmpdb = DBAccess.SQLEscape(organizationName);
        if ((!organizationName.equals(tmpxml)) || (!organizationName.equals(tmpdb)) || (!TextUtil.isValidFilename(organizationName))) {
            message = "invalid new_organization_name.";
            return false;
        }
        if ((organizationName.indexOf('-') > -1) || (organizationName.indexOf(' ') > -1)) {
            message = "invalid new_organization_name.";
            return false;
        }
        String[] orgnames = ServerConsoleServlet.getOrganizationNames();
        for (int i = 0; i < orgnames.length; i++) {
            if (orgnames.equals(organizationName)) {
                message = "already exists.";
                return false;
            }
        }
        message = "create new organization: " + organizationName;
        File newOrganizationDirectory = new File(ServerConsoleServlet.RepositoryLocalDirectory.getAbsolutePath() + File.separator + organizationName);
        if (!newOrganizationDirectory.mkdir()) {
            message = "cannot create directory.";
            return false;
        }
        File cacheDir = new File(newOrganizationDirectory.getAbsolutePath() + File.separator + ServerConsoleServlet.getConfigByTagName("CacheDirName"));
        cacheDir.mkdir();
        File confDir = new File(newOrganizationDirectory.getAbsolutePath() + File.separator + ServerConsoleServlet.getConfigByTagName("ConfDirName"));
        confDir.mkdir();
        File rdfDir = new File(newOrganizationDirectory.getAbsolutePath() + File.separator + ServerConsoleServlet.getConfigByTagName("RDFDirName"));
        rdfDir.mkdir();
        File resourceDir = new File(newOrganizationDirectory.getAbsolutePath() + File.separator + ServerConsoleServlet.getConfigByTagName("ResourceDirName"));
        resourceDir.mkdir();
        File obsoleteDir = new File(resourceDir.getAbsolutePath() + File.separator + "obsolete");
        obsoleteDir.mkdir();
        File schemaDir = new File(newOrganizationDirectory.getAbsolutePath() + File.separator + ServerConsoleServlet.getConfigByTagName("SchemaDirName"));
        schemaDir.mkdir();
        String organization_temp_dir = ServerConsoleServlet.convertToAbsolutePath(ServerConsoleServlet.getConfigByTagName("OrganizationTemplate"));
        File templ = new File(organization_temp_dir);
        File[] confFiles = templ.listFiles();
        for (int i = 0; i < confFiles.length; i++) {
            try {
                FileReader fr = new FileReader(confFiles[i]);
                FileWriter fw = new FileWriter(confDir.getAbsolutePath() + File.separator + confFiles[i].getName());
                int c = -1;
                while ((c = fr.read()) != -1) fw.write(c);
                fw.flush();
                fw.close();
                fr.close();
            } catch (IOException e) {
            }
        }
        SchemaModelHolder.reloadSchemaModel(organizationName);
        ResourceModelHolder.reloadResourceModel(organizationName);
        UserLogServlet.initializeUserLogDB(organizationName);
        MetaEditServlet.createNewProject(organizationName, "standard", MetaEditServlet.convertProjectIdToProjectUri(organizationName, "standard", req), this.username);
        ResourceModelHolder.reloadResourceModel(organizationName);
        message = organizationName + " is created. Restart Tomcat to activate this organization.";
        return true;
    }
