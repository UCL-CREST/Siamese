    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        IDatabaseConnection connection = null;
        try {
            connection = new DatabaseConnection(dataSource.getConnection());
            DatabaseConfig _config = connection.getConfig();
            _config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
            QueryDataSet partialDataSet = new QueryDataSet(connection);
            partialDataSet.addTable("OSM_GROUPS");
            partialDataSet.addTable("OSM_USERS");
            partialDataSet.addTable("OSM_USER_GROUPS");
            partialDataSet.addTable("OSM_TAGS");
            partialDataSet.addTable("OSM_TYPEVENTS_D");
            partialDataSet.addTable("OSM_EVENT_TEMPLATES");
            partialDataSet.addTable("OSM_TYPEVENTS_TEMPLATES_D");
            partialDataSet.addTable("OSM_INTERFACES_D");
            partialDataSet.addTable("OSM_INTERFACES_TYPINST_D");
            partialDataSet.addTable("OSM_INTERFACE_USEREVENTS");
            partialDataSet.addTable("OSM_INTERFACE_PARAMS_D");
            partialDataSet.addTable("OSM_INSTANCES", "SELECT IDN_INSTANCE,DES_INSTANCE,TYP_INSTANCE,DTI_CREATED,USR_CREATION,0 AS TYP_CRITICITY,'osmiusNow' AS DTI_CRITICITY, 1 AS IND_AVAILABILITY, 'osmiusNow' AS DTI_AVAILABILITY, CONN_INFO, IDN_NODENAME, SNM_COMMUNITY, SNM_PORT FROM OSM_INSTANCES");
            partialDataSet.addTable("OSM_INSTANCES_POINTS");
            partialDataSet.addTable("OSM_INSTANCE_TAGS");
            partialDataSet.addTable("OSM_INSTANCE_INFO");
            partialDataSet.addTable("OSM_INSTANCE_EVENTS");
            partialDataSet.addTable("OSM_INSTANCE_DEPENDENCIES");
            partialDataSet.addTable("OSM_GROUP_INSTANCES", "SELECT IDN_GROUP, IDN_INSTANCE FROM OSM_GROUP_INSTANCES WHERE IDN_GROUP<>'00000000'");
            partialDataSet.addTable("OSM_MASTERAGENTS");
            partialDataSet.addTable("OSM_MASTERAGENTS_DISCOVERED");
            partialDataSet.addTable("OSM_MASTERAGENT_DEFPARAM_D");
            partialDataSet.addTable("OSM_MASTERAGENT_PARAMETERS");
            partialDataSet.addTable("OSM_AGENTS");
            partialDataSet.addTable("OSM_AGENT_PARAMETERS");
            partialDataSet.addTable("OSM_AGENT_INSTANCES");
            partialDataSet.addTable("OSM_SCHEDULES");
            partialDataSet.addTable("OSM_TIMESCHEDULES");
            partialDataSet.addTable("OSM_RULES");
            partialDataSet.addTable("OSM_RULES_INSTANCES");
            partialDataSet.addTable("OSM_SLAS");
            partialDataSet.addTable("OSM_INTERRUPTIONS");
            partialDataSet.addTable("OSM_AGGRUPATIONS");
            partialDataSet.addTable("OSM_SERVICES", "SELECT IDN_SERVICE, DES_SERVICE, 0 AS TYP_CRITICITY, 'osmiusNow' AS DTI_CRITICITY, 1 AS IND_AVAILABILITY, 'osmiusNow' AS DTI_AVAILABILITY, IDN_SCHEDULE,IDN_RESPONSIBLE FROM OSM_SERVICES");
            partialDataSet.addTable("OSM_SERVICES_POINTS");
            partialDataSet.addTable("OSM_SERVICE_SLAS");
            partialDataSet.addTable("OSM_SERVICE_RULES");
            partialDataSet.addTable("OSM_SERVICE_INTERRUPTIONS");
            partialDataSet.addTable("OSM_SERVICE_INSTANCES");
            partialDataSet.addTable("OSM_SERVICE_AGGRUPATIONS");
            partialDataSet.addTable("OSM_SEQUENCES");
            partialDataSet.addTable("OSM_INSTANCE_INSTRUCTIONS");
            partialDataSet.addTable("OSM_INSTRUCTIONS");
            partialDataSet.addTable("OSM_USERSCRIPTS");
            partialDataSet.addTable("OSM_USERSCRIPT_TYPPLATFORM");
            partialDataSet.addTable("OSM_USERSCRIPT_MASTERAGENT");
            partialDataSet.addTable("OSM_GENDATA");
            partialDataSet.addTable("OSM_DWH_CFG", "SELECT IND_ACTIVATEROUNDROBIN,NUM_DAYSAGGR_H,NUM_DAYSAGGR_D,NUM_DAYSAGGR_E,IND_BACKUPEVENTS,NUM_DAYSERASE_OTHERS,0 AS IND_STATE,'osmiusNow' AS DTI_INIEXEC,'osmiusNow' AS DTI_FINEXEC,'osmiusNowII' AS DAY_LASTSTATS,'osmiusNowII' AS DAY_LASTAGGR FROM OSM_DWH_CFG");
            partialDataSet.addTable("OSM_N_TYPNOTIFIWAY_D");
            partialDataSet.addTable("OSM_N_TYPNOTIFICATIONS_D");
            partialDataSet.addTable("OSM_N_TYPNOTIFIWAY_USERDATA_D");
            partialDataSet.addTable("OSM_N_TYPNOTIFICATIONS_DATA_D");
            partialDataSet.addTable("OSM_N_NOTIFIWAY");
            partialDataSet.addTable("OSM_N_NOTIFIWAY_SCHEDULE");
            partialDataSet.addTable("OSM_N_USER_TYPNOTIFIWAY");
            partialDataSet.addTable("OSM_N_SUBSCRIPTION");
            partialDataSet.addTable("OSM_N_USER_SUBSCRIPTION");
            partialDataSet.addTable("OSM_N_SLA_SUBSCRIPTION");
            partialDataSet.addTable("OSM_N_SERVICE_SUBSCRIPTION");
            partialDataSet.addTable("OSM_N_INSTANCE_SUBSCRIPTION");
            partialDataSet.addTable("OSM_N_OUTOFOFFICE");
            partialDataSet.addTable("OSM_N_GUARD");
            partialDataSet.addTable("OSM_N_TIMEGUARDS");
            partialDataSet.addTable("OSM_T_ENTERPRISES");
            partialDataSet.addTable("OSM_T_TYPEVENTS");
            partialDataSet.addTable("OSM_T_EVENT_TYPINSTANCES");
            partialDataSet.addTable("OSM_T_EVENT_TRAPS");
            partialDataSet.addTable("OSM_T_TRAP_VARSBIND");
            partialDataSet.addTable("OSM_T_TRAPS_RECEIVED");
            partialDataSet.addTable("OSM_T_TRAPS_DISCOVERED");
            partialDataSet.addTable("OSM_T_MASTERAGENT_INSTANCES");
            partialDataSet.addTable("OSM_T_INSTANCE_EVENTS");
            partialDataSet.addTable("OSM_PUBLIC_HOLIDAYS");
            partialDataSet.addTable("OSM_TYP_WIDGETS");
            partialDataSet.addTable("OSM_TYP_WIDGETS_PARAMS");
            partialDataSet.addTable("OSM_WIDGETS");
            partialDataSet.addTable("OSM_WIDGETS_VIEWS");
            partialDataSet.addTable("OSM_WIDGETS_PARAMS");
            String path = request.getSession().getServletContext().getRealPath("/exports") + File.separator;
            FlatDtdDataSet.write(partialDataSet, new FileOutputStream(path + "osmius.dtd"));
            FlatXmlWriter datasetWriter = new FlatXmlWriter(new FileOutputStream(path + "osmius.xml"));
            datasetWriter.setDocType("osmius.dtd");
            datasetWriter.write(partialDataSet);
            String[] filenames = new String[] { "osmius.xml", "osmius.dtd", "version" };
            byte[] buf = new byte[1024];
            try {
                String outFilename = path + "osmiusExport.zip";
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
                for (int i = 0; i < filenames.length; i++) {
                    FileInputStream in = new FileInputStream(path + filenames[i]);
                    out.putNextEntry(new ZipEntry(filenames[i]));
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.closeEntry();
                    in.close();
                }
                out.close();
            } catch (IOException e) {
            }
            byte[] content = getBytesFromFile(new File(path + "osmiusExport.zip"));
            response.setContentLength(content.length);
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=\"osmiusExport.zip\"");
            ServletOutputStream ouputStream = response.getOutputStream();
            ouputStream.write(content, 0, content.length);
            ouputStream.flush();
            ouputStream.close();
            new File(path + "osmius.xml").delete();
            new File(path + "osmius.dtd").delete();
            new File(path + "osmiusExport.zip").delete();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
        return null;
    }
