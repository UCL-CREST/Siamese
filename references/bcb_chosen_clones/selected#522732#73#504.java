    public String drive() {
        logger.info("\n");
        logger.info("===========================================================");
        logger.info("========== Start drive method =============================");
        logger.info("===========================================================");
        logger.entering(cl, "drive");
        xstream = new XStream(new JsonHierarchicalStreamDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("AuditDiffFacade", AuditDiffFacade.class);
        File auditSchemaFile = null;
        File auditSchemaXsdFile = null;
        try {
            if (configFile == null) {
                logger.severe("Request Failed: configFile is null");
                return null;
            } else {
                if (configFile.getAuditSchemaFile() != null) {
                    logger.info("auditSchemaFile=" + configFile.getAuditSchemaFile());
                    logger.info("auditSchemaXsdFile=" + configFile.getAuditSchemaXsdFile());
                    logger.info("plnXpathFile=" + configFile.getPlnXpathFile());
                    logger.info("auditSchemaFileDir=" + configFile.getAuditSchemaFileDir());
                    logger.info("auditReportFile=" + configFile.getAuditReportFile());
                    logger.info("auditReportXsdFile=" + configFile.getAuditReportXsdFile());
                } else {
                    logger.severe("Request Failed: auditSchemaFile is null");
                    return null;
                }
            }
            File test = new File(configFile.getAuditSchemaFileDir() + File.separator + "temp.xml");
            auditSchemaFile = new File(configFile.getAuditSchemaFile());
            if (!auditSchemaFile.exists() || auditSchemaFile.length() == 0L) {
                logger.severe("Request Failed: the audit schema file does not exist or empty");
                return null;
            }
            auditSchemaXsdFile = null;
            if (configFile.getAuditSchemaXsdFile() != null) {
                auditSchemaXsdFile = new File(configFile.getAuditSchemaXsdFile());
            } else {
                logger.severe("Request Failed: the audit schema xsd file is null");
                return null;
            }
            if (!auditSchemaXsdFile.exists() || auditSchemaXsdFile.length() == 0L) {
                logger.severe("Request Failed: the audit schema xsd file does not exist or empty");
                return null;
            }
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(auditSchemaXsdFile);
            Validator validator = schema.newValidator();
            Source source = new StreamSource(auditSchemaFile);
            validator.validate(source);
        } catch (SAXException e) {
            logger.warning("SAXException caught trying to validate input Schema Files: ");
            e.printStackTrace();
        } catch (IOException e) {
            logger.warning("IOException caught trying to read input Schema File: ");
            e.printStackTrace();
        }
        String xPathFile = null;
        if (configFile.getPlnXpathFile() != null) {
            xPathFile = configFile.getPlnXpathFile();
            logger.info("Attempting to retrieve xpaths from file: '" + xPathFile + "'");
            XpathUtility.readFile(xPathFile);
        } else {
            logger.severe("Configuration file does not have a value for the Xpath Filename");
            return null;
        }
        Properties xpathProps = XpathUtility.getXpathsProps();
        if (xpathProps == null) {
            logger.severe("No Xpaths could be extracted from file: '" + xPathFile + "' - xpath properties object is null");
            return null;
        }
        if (xpathProps.isEmpty()) {
            logger.severe("No Xpaths could be extracted from file: '" + xPathFile + "' - xpath properties object is empty");
            return null;
        }
        logger.info(xpathProps.size() + " xpaths retrieved.");
        for (String key : xpathProps.stringPropertyNames()) {
            logger.info("Key=" + key + "   Value=" + xpathProps.getProperty(key));
        }
        logger.info("\n");
        logger.info("===========================================================");
        logger.info("========== Process XML Schema File BEGIN ==================");
        logger.info("===========================================================");
        SchemaSAXReader sax = new SchemaSAXReader();
        ArrayList<String> key_matches = new ArrayList<String>(sax.parseDocument(auditSchemaFile, xpathProps));
        logger.info("Check Input xpath hash against xpaths found in Schema.");
        Comparison comp_keys = new Comparison();
        ArrayList<String> in_xpath_not_in_schema = new ArrayList<String>(comp_keys.keys_not_in_both_hashes(xpathProps, Utility.arraylist_to_map(key_matches, "key_matches"), "xpath Properties", "hm_key_matches"));
        if (in_xpath_not_in_schema.size() > 0) {
            logger.severe("All XPaths in Input xpath Properties list were not found in Schema.");
            logger.severe("Xpaths in xpath Properties list missing from schema file:" + xstream.toXML(in_xpath_not_in_schema));
            logger.severe("Quitting.");
            return null;
        }
        Map<String, Map> schema_audit_hashbox = sax.get_audit_hashbox();
        logger.info("schema_audit_hashbox\n" + xstream.toXML(schema_audit_hashbox));
        Map<String, Map> schema_network_hashbox = sax.get_net_hashbox();
        logger.info("schema_network_hashbox\n" + xstream.toXML(schema_network_hashbox));
        Map<String, Map> schema_host_hashbox = sax.get_host_hashbox();
        Map<String, Map> schema_au_hashbox = sax.get_au_hashbox();
        logger.info("schema_au_hashbox\n" + xstream.toXML(schema_au_hashbox));
        Hasherator hr = new Hasherator();
        Set<String> s_host_hb_additions = new HashSet<String>();
        s_host_hb_additions.add("/SSP/network/@network_id");
        schema_host_hashbox = hr.copy_hashbox_entries(schema_network_hashbox, schema_host_hashbox, s_host_hb_additions);
        logger.info("schema_host_hashbox(after adding network name)\n" + xstream.toXML(schema_host_hashbox));
        Map<String, String> transforms_s_au_hb = new HashMap<String, String>();
        transforms_s_au_hb.put("/SSP/archivalUnits/au/auCapabilities/storageRequired/@max_size", "s_gigabytes_to_string_bytes_unformatted()");
        schema_au_hashbox = hr.convert_hashbox_vals(schema_au_hashbox, transforms_s_au_hb);
        Map<String, String> transforms_s_host_hb = new HashMap<String, String>();
        transforms_s_host_hb.put("/SSP/hosts/host/hostCapabilities/storageAvailable/@max_size", "s_gigabytes_to_string_bytes_unformatted()");
        schema_host_hashbox = hr.convert_hashbox_vals(schema_host_hashbox, transforms_s_host_hb);
        logger.info("schema_host_hashbox(after transformations)\n" + xstream.toXML(schema_host_hashbox));
        logger.info("\n");
        logger.info("========== Process Schema  END ============================");
        logger.info("\n");
        logger.info("========== Database Operations ============================");
        MYSQLWorkPlnHostSummaryDAO daowphs = new MYSQLWorkPlnHostSummaryDAO();
        daowphs.drop();
        daowphs.create();
        daowphs.updateTimestamp();
        CachedRowSet rs_q0_N = daowphs.query_0_N();
        double d_space_total = DBUtil.get_single_db_double_value(rs_q0_N, "net_sum_repo_size");
        double d_space_used = DBUtil.get_single_db_double_value(rs_q0_N, "net_sum_used_space");
        double d_space_free = d_space_total - d_space_used;
        double d_avg_uptime = DBUtil.get_single_db_double_value(rs_q0_N, "net_avg_uptime");
        long space_total = (long) d_space_total;
        long space_used = (long) d_space_used;
        long space_free = space_total - space_used;
        String f_space_total = Utility.l_bytes_to_other_units_formatted(space_total, 3, "T");
        String f_space_used = Utility.l_bytes_to_other_units_formatted(space_used, 3, "G");
        String f_space_free = Utility.l_bytes_to_other_units_formatted(space_free, 3, "T");
        String f_space_free2 = Utility.l_bytes_to_other_units_formatted(space_free, 3, null);
        logger.info("d_space_total: " + d_space_total + "\n" + "d_space_used: " + d_space_used + "\n" + "space_total: " + space_total + "\n" + "space_used: " + space_used + "\n" + "space_free: " + space_free + "\n\n" + "Double.toString( d_space_total ): " + Double.toString(d_space_total) + "\n\n" + "f_space_total: " + f_space_total + "\n" + "f_space_used: " + f_space_used + "\n" + "f_space_free: " + f_space_free + "\n" + "f_space_free2: " + f_space_free2);
        rprtCnst = new ReportData();
        logger.info("\n");
        logger.info("========== Load Report Constants from Calculations ===========");
        rprtCnst.addKV("REPORT_HOSTS_TOTAL_DISKSPACE", f_space_total);
        rprtCnst.addKV("REPORT_HOSTS_TOTAL_DISKSPACE_USED", f_space_used);
        rprtCnst.addKV("REPORT_HOSTS_TOTAL_DISKSPACE_FREE", f_space_free);
        rprtCnst.addKV("REPORT_HOSTS_MEAN_UPTIME", Utility.ms_to_dd_hh_mm_ss_formatted((long) d_avg_uptime));
        logger.info("r=\n" + rprtCnst.toString());
        logger.info("\n");
        logger.info("========== Load Report Constants from ConfigFile =============");
        rprtCnst.addKV("REPORT_FILENAME_SCHEMA_FILENAME", configFile.getAuditSchemaFile());
        rprtCnst.addKV("REPORT_FILENAME_SCHEMA_FILE_XSD_FILENAME", configFile.getAuditSchemaXsdFile());
        rprtCnst.addKV("REPORT_FILENAME_XML_DIFF_FILENAME", configFile.getAuditReportFile());
        rprtCnst.addKV("REPORT_FILENAME_XML_DIFF_FILE_XSD_FILENAME", configFile.getAuditReportXsdFile());
        logger.info("\n");
        logger.info("========== Load Report Constants from Hashboxes ==============");
        Set auditHBKeySet = hr.getMapKeyset(schema_audit_hashbox, "schema_audit_hashbox");
        String audit_id = hr.singleKeysetEntryToString(auditHBKeySet);
        logger.info("audit_id: " + audit_id);
        Set networkHBKeySet = hr.getMapKeyset(schema_network_hashbox, "schema_network_hashbox");
        String network_id = hr.singleKeysetEntryToString(networkHBKeySet);
        logger.info("network_id: " + network_id);
        rprtCnst.addKV("REPORT_AUDIT_ID", audit_id);
        rprtCnst.addKV("REPORT_AUDIT_REPORT_EMAIL", hr.extractSingleValueFromHashbox(schema_audit_hashbox, "schema_audit_hashbox", audit_id, "/SSP/audit/auditReportEmail"));
        rprtCnst.addKV("REPORT_AUDIT_INTERVAL", hr.extractSingleValueFromHashbox(schema_audit_hashbox, "schema_audit_hashbox", audit_id, "/SSP/audit/auditReportInterval/@maxDays"));
        rprtCnst.addKV("REPORT_SCHEMA_VERSION", hr.extractSingleValueFromHashbox(schema_audit_hashbox, "schema_audit_hashbox", audit_id, "/SSP/audit/schemaVersion"));
        rprtCnst.addKV("REPORT_CLASSIFICATION_GEOGRAPHIC_SUMMARY_SCHEME", hr.extractSingleValueFromHashbox(schema_audit_hashbox, "schema_audit_hashbox", audit_id, "/SSP/audit/geographicSummaryScheme"));
        rprtCnst.addKV("REPORT_CLASSIFICATION_SUBJECT_SUMMARY_SCHEME", hr.extractSingleValueFromHashbox(schema_audit_hashbox, "schema_audit_hashbox", audit_id, "/SSP/audit/subjectSummaryScheme"));
        rprtCnst.addKV("REPORT_CLASSIFICATION_OWNER_INSTITUTION_SUMMARY_SCHEME", hr.extractSingleValueFromHashbox(schema_audit_hashbox, "schema_audit_hashbox", audit_id, "/SSP/audit/ownerInstSummaryScheme"));
        rprtCnst.addKV("REPORT_NETWORK_ID", network_id);
        rprtCnst.addKV("REPORT_NETWORK_ADMIN_EMAIL", hr.extractSingleValueFromHashbox(schema_network_hashbox, "schema_network_hashbox", network_id, "/SSP/network/networkIdentity/accessBase/@adminEmail"));
        rprtCnst.addKV("REPORT_GEOGRAPHIC_CODING", hr.extractSingleValueFromHashbox(schema_network_hashbox, "schema_network_hashbox", network_id, "/SSP/network/networkIdentity/geographicCoding"));
        logger.info("\n");
        logger.info("===========================================================");
        logger.info("========== Process Network Data BEGIN======================");
        logger.info("===========================================================");
        Set<String> tableSet0 = reportAuOverviewFacade.findAllTables();
        String reportAuOverviewTable = "report_au_overview";
        int n_tabs = 0;
        if (tableSet0 != null && !tableSet0.isEmpty()) {
            logger.fine("Table List N=" + tableSet0.size());
            for (String tableName : tableSet0) {
                n_tabs++;
                if (tableName.equalsIgnoreCase(reportAuOverviewTable)) {
                    logger.fine(n_tabs + " " + tableName + " <--");
                } else {
                    logger.fine(n_tabs + " " + tableName);
                }
            }
        } else {
            logger.fine("No tables found in DB.");
        }
        if (!tableSet0.contains(reportAuOverviewTable)) {
            logger.info("Database does not contain table '" + reportAuOverviewTable + "'");
        }
        List<ReportAuOverview> repAuOvTabAllData = null;
        repAuOvTabAllData = reportAuOverviewFacade.findAll();
        if (repAuOvTabAllData != null && !(repAuOvTabAllData.isEmpty())) {
            logger.fine("\n" + reportAuOverviewTable + " table has " + repAuOvTabAllData.size() + " rows.");
            int n_rows = 0;
            for (ReportAuOverview row : repAuOvTabAllData) {
                n_rows++;
                logger.fine(n_rows + " " + row.toString());
            }
        } else {
            logger.fine(reportAuOverviewTable + " is null, empty, or nonexistent.");
        }
        logger.fine("report_au_overview Table xstream Dump:\n" + xstream.toXML(repAuOvTabAllData));
        logger.fine("\n");
        logger.fine("Iterate over repAuOvTabAllData 2");
        Iterator it = repAuOvTabAllData.iterator();
        int n_el = 0;
        while (it.hasNext()) {
            ++n_el;
            String el = it.next().toString();
            logger.fine(n_el + ". " + el);
        }
        Class aClass = edu.harvard.iq.safe.saasystem.entities.ReportAuOverview.class;
        String reportAuOverviewTableName = reportAuOverviewFacade.getTableName();
        logger.fine("\n");
        logger.fine("EntityManager Tests");
        logger.fine("Table: " + reportAuOverviewTableName);
        logger.fine("\n");
        logger.fine("Schema: " + reportAuOverviewFacade.getSchema());
        logger.fine("\n");
        Set columnList = reportAuOverviewFacade.getColumnList(reportAuOverviewFacade.getTableName());
        logger.fine("Columns (fields) in table '" + reportAuOverviewTableName + "' (N=" + columnList.size() + ")");
        Set<String> colList = new HashSet();
        Iterator colNames = columnList.iterator();
        int n_el2 = 0;
        while (colNames.hasNext()) {
            ++n_el2;
            String el = colNames.next().toString();
            logger.fine(n_el2 + ". " + el);
            colList.add(el);
        }
        logger.fine(colList.size() + " entries in Set 'colList' ");
        logger.info("========== Query 'au_overview_table'=============");
        MySQLAuOverviewDAO daoao = new MySQLAuOverviewDAO();
        CachedRowSet rs_q1_A = daoao.query_q1_A();
        int[] au_table_rc = DBUtil.get_rs_dims(rs_q1_A);
        logger.info("Au Table Query ResultSet has " + au_table_rc[0] + " rows and " + au_table_rc[1] + " columns.");
        rprtCnst.addKV("REPORT_N_AUS_IN_NETWORK", Integer.toString(au_table_rc[0]));
        logger.info("========== Create 'network_au_hashbox' ==========");
        Map<String, Map> network_au_hashbox = new TreeMap<String, Map>(DBUtil.rs_to_hashbox(rs_q1_A, null, "au_id"));
        logger.info("network_au_hashbox before transformations\n" + xstream.toXML(network_au_hashbox));
        Map<String, String> transforms_n_au_hb = new HashMap<String, String>();
        transforms_n_au_hb.put("last_s_crawl_end", "ms_to_decimal_days_elapsed()");
        transforms_n_au_hb.put("last_s_poll_end", "ms_to_decimal_days_elapsed()");
        transforms_n_au_hb.put("crawl_duration", "ms_to_decimal_days()");
        network_au_hashbox = hr.convert_hashbox_vals(network_au_hashbox, transforms_n_au_hb);
        Map<String, String> auNVerifiedRegions = reportAuOverviewFacade.getAuNVerifiedRegions();
        logger.fine("auNVerifiedRegions\n" + xstream.toXML(auNVerifiedRegions));
        network_au_hashbox = hr.addNewInnerHashEntriesToHashbox(network_au_hashbox, auNVerifiedRegions, "au_n_verified_regions");
        logger.info("network_au_hashbox after Transformations and Addition of 'au_n_verified_regions'" + xstream.toXML(network_au_hashbox));
        logger.info("========== Compare AUs BEGIN ==============================");
        ArrayList<String> al_aus_in_schema_not_in_network = new ArrayList<String>(comp_keys.keys_not_in_both_hashes(schema_au_hashbox, network_au_hashbox, "schema_aus", "network_aus"));
        Map<String, String> h_aus_in_schema_not_in_network = hr.get_names_from_id_list(schema_au_hashbox, al_aus_in_schema_not_in_network, "/SSP/archivalUnits/au/auIdentity/name");
        rprtCnst.addKV("REPORT_N_AUS_IN_SCHEMA_NOT_IN_NETWORK", Integer.toString(al_aus_in_schema_not_in_network.size()));
        rprtCnst.set_h_aus_in_schema_not_in_network(h_aus_in_schema_not_in_network);
        MYSQLReportAusInSchemaNotInNetworkDAO daoraisnin = new MYSQLReportAusInSchemaNotInNetworkDAO();
        daoraisnin.create();
        daoraisnin.update(h_aus_in_schema_not_in_network);
        ArrayList<String> al_aus_in_network_not_in_schema = new ArrayList<String>(comp_keys.keys_not_in_both_hashes(network_au_hashbox, schema_au_hashbox, "network_aus", "schema_aus"));
        Utility.print_arraylist(al_aus_in_network_not_in_schema, "aus in_network_not_in_schema");
        Map<String, String> h_aus_in_network_not_in_schema = hr.get_names_from_id_list(network_au_hashbox, al_aus_in_network_not_in_schema, "au_name");
        rprtCnst.addKV("REPORT_N_AUS_IN_NETWORK_NOT_IN_SCHEMA", Integer.toString(al_aus_in_network_not_in_schema.size()));
        rprtCnst.set_h_aus_in_network_not_in_schema(h_aus_in_network_not_in_schema);
        MYSQLReportAusInNetworkNotInSchemaDAO daorainnis = new MYSQLReportAusInNetworkNotInSchemaDAO();
        daorainnis.create();
        daorainnis.update(h_aus_in_network_not_in_schema);
        Comparison comp_au = new Comparison(schema_au_hashbox, "Schema_AU", network_au_hashbox, "Network_AU", XpathUtility.getXpathToDbColumnMap(), XpathUtility.getXpathToCompOpMap());
        comp_au.init();
        logger.info("Attempting to create DB table 'lockss_audit.audit_results_au'");
        MYSQLAuditResultsAuDAO daoara = new MYSQLAuditResultsAuDAO();
        daoara.create();
        String results_table_au = "audit_results_au";
        String sql_vals_au_schema = comp_au.iterate_hbs_au(daoara, results_table_au, "au", h_aus_in_network_not_in_schema);
        CachedRowSet rs_RA2 = daoara.query_q1_RA();
        String n_aus_not_verified = DBUtil.get_single_count_from_rs(rs_RA2);
        rprtCnst.addKV("REPORT_N_AUS_NOT_VERIFIED", DBUtil.get_single_count_from_rs(rs_RA2));
        logger.info("\nInstantiating Result Class from main()");
        DiffResult result = new DiffResult();
        Map au_comp_host = result.get_result_hash("au");
        logger.info("========== Compare AUs END ================================");
        logger.info("========== Process Network Host Table =====================");
        logger.info("========== Query 'lockss_box_table' and =========");
        logger.info("================ 'repository_space_table' =======\n");
        MySQLLockssBoxRepositorySpaceDAO daolbrs = new MySQLLockssBoxRepositorySpaceDAO();
        CachedRowSet rs_q1_H = daolbrs.query_q1_H();
        int[] host_table_rc = DBUtil.get_rs_dims(rs_q1_H);
        logger.info("Host Table Query ResultSet has " + host_table_rc[0] + " rows and " + host_table_rc[1] + " columns.");
        rprtCnst.addKV("REPORT_N_HOSTS_IN_NETWORK", Integer.toString(host_table_rc[0]));
        Long numberOfMemberHosts;
        if (StringUtils.isNotBlank(saasConfigurationRegistry.getSaasConfigProperties().getProperty("saas.ip.fromlockssxml"))) {
            numberOfMemberHosts = Long.parseLong(Integer.toString(saasConfigurationRegistry.getSaasConfigProperties().getProperty("saas.ip.fromlockssxml").split(",").length));
        } else {
            if (StringUtils.isNotBlank(saasConfigurationRegistry.getSaasAuditConfigProperties().getProperty("saas.targetIp"))) {
                numberOfMemberHosts = Long.parseLong(Integer.toString(saasConfigurationRegistry.getSaasAuditConfigProperties().getProperty("saas.targetIp").split(",").length));
            } else {
                numberOfMemberHosts = 0L;
            }
        }
        rprtCnst.addKV("REPORT_N_HOSTS_IN_NETWORK_2", Long.toString(numberOfMemberHosts));
        Long numberOfReachableHosts;
        numberOfReachableHosts = lockssBoxFacade.getTotalHosts();
        rprtCnst.addKV("REPORT_N_HOSTS_IN_NETWORK_REACHABLE", Long.toString(numberOfReachableHosts));
        Map<String, Map> network_host_hashbox = new TreeMap<String, Map>(DBUtil.rs_to_hashbox(rs_q1_H, null, "ip_address"));
        logger.info("network_host_hashbox before transformations\n" + xstream.toXML(network_host_hashbox));
        Map<String, String> transforms_n_host_hb = new HashMap<String, String>();
        transforms_n_host_hb.put("repo_size", "SciToStr2()");
        transforms_n_host_hb.put("used_space", "SciToStr2()");
        network_host_hashbox = hr.convert_hashbox_vals(network_host_hashbox, transforms_n_host_hb);
        logger.info("network_host_hashbox(after transformations)\n" + xstream.toXML(network_host_hashbox));
        Map<String, String> network_host_hb_sel_used_space = hr.join_hash_pk_to_inner_hash_value(network_host_hashbox, "used_space");
        Map<String, String> schema_host_hb_sel_size = hr.join_hash_pk_to_inner_hash_value(schema_host_hashbox, "/SSP/hosts/host/hostCapabilities/storageAvailable/@max_size");
        logger.info("\n========== Process Network  END ===========================");
        logger.info("========== Compare Key Sets (IDs)==========================");
        Set<String> sa_hb_keys = hr.gen_hash_keyset(schema_au_hashbox, "schema_au_hashbox");
        hr.set_hash_keyset(sa_hb_keys, "s_au_hb");
        Set<String> sh_hb_keys = hr.gen_hash_keyset(schema_host_hashbox, "schema_host_hashbox");
        hr.set_hash_keyset(sh_hb_keys, "s_h_hb");
        Set<String> na_hb_keys = hr.gen_hash_keyset(network_au_hashbox, "network_au_hashbox");
        hr.set_hash_keyset(na_hb_keys, "n_au_hb");
        Set<String> nh_hb_keys = hr.gen_hash_keyset(network_host_hashbox, "network_host_hashbox");
        hr.set_hash_keyset(nh_hb_keys, "n_h_hb");
        Set<String> aus_in_schema_not_in_network = new TreeSet<String>(hr.get_hash_keyset("s_au_hb"));
        aus_in_schema_not_in_network.removeAll(hr.get_hash_keyset("n_au_hb"));
        Set<String> aus_in_network_not_in_schema = new TreeSet<String>(hr.get_hash_keyset("n_au_hb"));
        aus_in_network_not_in_schema.removeAll(hr.get_hash_keyset("s_au_hb"));
        Set<String> symmetricDiff = new HashSet<String>(hr.get_hash_keyset("s_au_hb"));
        symmetricDiff.addAll(hr.get_hash_keyset("n_au_hb"));
        Set<String> tmp = new HashSet<String>(hr.get_hash_keyset("s_au_hb"));
        tmp.retainAll(hr.get_hash_keyset("n_au_hb"));
        symmetricDiff.removeAll(tmp);
        Set<String> hosts_in_network_not_in_schema = new TreeSet<String>(hr.get_hash_keyset("n_h_hb"));
        hosts_in_network_not_in_schema.removeAll(hr.get_hash_keyset("s_h_hb"));
        Set<String> hosts_in_schema_not_in_network = new TreeSet<String>(hr.get_hash_keyset("s_h_hb"));
        hosts_in_schema_not_in_network.removeAll(hr.get_hash_keyset("n_h_hb"));
        ArrayList<String> al_hosts_in_schema_not_in_network = new ArrayList<String>(comp_keys.keys_not_in_both_hashes(schema_host_hashbox, network_host_hashbox, "schema_hosts", "network_hosts"));
        Map<String, String> h_hosts_in_schema_not_in_network = hr.get_names_from_id_list(schema_host_hashbox, al_hosts_in_schema_not_in_network, "/SSP/hosts/host/hostIdentity/name");
        rprtCnst.addKV("REPORT_N_HOSTS_IN_SCHEMA_NOT_IN_NETWORK", Integer.toString(al_hosts_in_schema_not_in_network.size()));
        rprtCnst.set_h_hosts_in_schema_not_in_network(h_hosts_in_schema_not_in_network);
        MYSQLReportHostsInSchemaNotInNetworkDAO daorhisnin = new MYSQLReportHostsInSchemaNotInNetworkDAO();
        daorhisnin.create();
        daorhisnin.update(h_hosts_in_schema_not_in_network);
        ArrayList<String> al_hosts_in_network_not_in_schema = new ArrayList<String>(comp_keys.keys_not_in_both_hashes(network_host_hashbox, schema_host_hashbox, "network_hosts", "schema_hosts"));
        Map<String, String> h_hosts_in_network_not_in_schema = hr.get_names_from_id_list(network_host_hashbox, al_hosts_in_network_not_in_schema, "host_name");
        rprtCnst.addKV("REPORT_N_HOSTS_IN_NETWORK_NOT_IN_SCHEMA", Integer.toString(al_hosts_in_network_not_in_schema.size()));
        rprtCnst.set_h_hosts_in_network_not_in_schema(h_hosts_in_network_not_in_schema);
        MYSQLReportHostsInNetworkNotInSchemaDAO rhinnis = new MYSQLReportHostsInNetworkNotInSchemaDAO();
        rhinnis.create();
        rhinnis.update(h_hosts_in_network_not_in_schema);
        logger.info("========== Compare Hosts BEGIN ============================");
        Comparison comp_host = new Comparison(schema_host_hashbox, "Schema_Host", network_host_hashbox, "Network_Host", XpathUtility.getXpathToDbColumnMap(), XpathUtility.getXpathToCompOpMap());
        comp_host.init();
        MYSQLAuditResultsHostDAO daoarh = new MYSQLAuditResultsHostDAO();
        daoarh.create();
        String sql_vals_host_schema = comp_host.iterate_hbs_host(daoarh, "audit_results_host", "host", h_hosts_in_network_not_in_schema);
        CachedRowSet rs_RH = daoarh.query_q1_RH();
        String n_hosts_not_meeting_storage = DBUtil.get_single_count_from_rs(rs_RH);
        rprtCnst.addKV("REPORT_N_HOSTS_NOT_MEETING_STORAGE", n_hosts_not_meeting_storage);
        logger.info("Calling result.get_result_hash( \"host\" ) from main()");
        Map host_comp_hash = result.get_result_hash("host");
        Map au_comp_hash2 = result.get_result_hash("au");
        logger.info("========== Compare Hosts END ==============================");
        Map<String, String> map_host_ip_to_host_name = hr.make_id_hash(schema_host_hashbox, "/SSP/hosts/host/hostIdentity/name");
        rprtCnst.addKV("REPORT_N_HOSTS_IN_SCHEMA", Integer.toString(map_host_ip_to_host_name.size()));
        String[] host_ip_list = hr.hash_keys_to_array(schema_host_hashbox);
        String[][] col2 = Utility.add_column_to_array1(map_host_ip_to_host_name.values().toArray(new String[0]), host_ip_list, null);
        Map<String, String> map_au_key_string_to_au_name = hr.make_id_hash(schema_au_hashbox, "/SSP/archivalUnits/au/auIdentity/name");
        logger.info("Length map_au_key_string_to_au_name.values().toArray(new String[0]: " + map_au_key_string_to_au_name.values().toArray(new String[0]).length);
        rprtCnst.addKV("REPORT_N_AUS_IN_SCHEMA", Integer.toString(map_au_key_string_to_au_name.size()));
        MySQLLockssBoxArchivalUnitStatusDAO daolbaus = new MySQLLockssBoxArchivalUnitStatusDAO();
        int[] rc = daolbaus.getResultSetDimensions();
        int n_rs_rows = rc[0];
        int n_rs_cols = rc[1];
        logger.info("\n" + n_rs_rows + " rows (Host-AU's). " + n_rs_cols + " columns.");
        rprtCnst.addKV("REPORT_N_HOST_AUS_IN_NETWORK", Integer.toString(n_rs_rows));
        logger.info("================== Query 'audit_results_host' Table ==========");
        CachedRowSet NNonCompliantAUsCRS = daoara.getNNonCompliantAUs();
        String NNonCompliantAUs = DBUtil.get_single_count_from_rs(NNonCompliantAUsCRS);
        rprtCnst.addKV("REPORT_N_AUS_NONCOMPLIANT", NNonCompliantAUs);
        logger.info("================== Query 'audit_results_host' Table END ======");
        logger.info("========== Output Report ==================================");
        MYSQLReportConstantsDAO daorc = new MYSQLReportConstantsDAO();
        daorc.create();
        daorc.update(rprtCnst.getBox());
        MYSQLReportHostSummaryDAO daorhs = new MYSQLReportHostSummaryDAO();
        daorhs.create();
        CachedRowSet crsarh = daoarh.queryAll();
        daorhs.update(crsarh);
        daorhs.update_new_column("space_offered", schema_host_hb_sel_size);
        daorhs.update_new_column("space_used", network_host_hb_sel_used_space);
        Map<String, String> computation_cols_in_net_host_summary = new HashMap<String, String>();
        computation_cols_in_net_host_summary.put("space_total", "1");
        computation_cols_in_net_host_summary.put("space_used", "2");
        daorhs.update_compute_column("space_free", computation_cols_in_net_host_summary);
        logger.info("========== Audit Report Writer ======================================");
        AuditReportXMLWriter arxw = new AuditReportXMLWriter(rprtCnst, configFile.getAuditReportFile());
        Set<String> tableSet = tracAuditChecklistDataFacade.findAllTables();
        String tracResultTable = "trac_audit_checklist_data";
        List<TracAuditChecklistData> evidenceList = null;
        if (tableSet.contains(tracResultTable)) {
            evidenceList = tracAuditChecklistDataFacade.findAll();
            logger.info("TRAC evidence list is size:" + evidenceList.size());
        } else {
            logger.info("Database does not contain table 'trac_audit_checklist_data'");
        }
        Map<String, String> tracDataMap = new LinkedHashMap<String, String>();
        for (TracAuditChecklistData tracdata : evidenceList) {
            tracDataMap.put(tracdata.getAspectId(), tracdata.getEvidence());
        }
        String writeTimestamp = arxw.write(daoarh, daoara, daorc, tracDataMap);
        File target = new File(configFile.getAuditReportFileDir() + File.separator + configFile.getAuditSchemaFileName() + "." + writeTimestamp);
        FileChannel sourceChannel = null;
        FileChannel targetChannel = null;
        try {
            sourceChannel = new FileInputStream(auditSchemaFile).getChannel();
            targetChannel = new FileOutputStream(target).getChannel();
            targetChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO exception occurred while copying file", e);
        } finally {
            try {
                if (sourceChannel != null) {
                    sourceChannel.close();
                }
                if (targetChannel != null) {
                    targetChannel.close();
                }
            } catch (IOException e) {
                logger.info("closing channels failed");
            }
        }
        logger.info("\n========== EXIT drive() ===========================================");
        return writeTimestamp;
    }
