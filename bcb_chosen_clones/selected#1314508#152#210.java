    public static boolean installMetricsCfg(Db db, String xmlFileName) throws Exception {
        String xmlText = FileHelper.asString(xmlFileName);
        Bundle bundle = new Bundle();
        loadMetricsCfg(bundle, xmlFileName, xmlText);
        try {
            db.begin();
            PreparedStatement psExists = db.prepareStatement("SELECT e_bundle_id, xml_decl_path, xml_text FROM sdw.e_bundle WHERE xml_decl_path = ?;");
            psExists.setString(1, xmlFileName);
            ResultSet rsExists = db.executeQuery(psExists);
            if (rsExists.next()) {
                db.rollback();
                return false;
            }
            PreparedStatement psId = db.prepareStatement("SELECT currval('sdw.e_bundle_serial');");
            PreparedStatement psAdd = db.prepareStatement("INSERT INTO sdw.e_bundle (xml_decl_path, xml_text, sdw_major_version, sdw_minor_version, file_major_version, file_minor_version) VALUES (?, ?, ?, ?, ?, ?);");
            psAdd.setString(1, xmlFileName);
            psAdd.setString(2, xmlText);
            FileInformation fi = bundle.getSingleFileInformation();
            if (!xmlFileName.equals(fi.filename)) throw new IllegalStateException("FileInformation bad for " + xmlFileName);
            psAdd.setInt(3, Globals.SDW_MAJOR_VER);
            psAdd.setInt(4, Globals.SDW_MINOR_VER);
            psAdd.setInt(5, fi.majorVer);
            psAdd.setInt(6, fi.minorVer);
            if (1 != db.executeUpdate(psAdd)) {
                throw new IllegalStateException("Could not add " + xmlFileName);
            }
            int bundleId = DbHelper.getIntKey(psId);
            PreparedStatement psGroupId = db.prepareStatement("SELECT currval('sdw.e_metric_group_serial');");
            PreparedStatement psAddGroup = db.prepareStatement("INSERT INTO sdw.e_metric_group (bundle_id, metric_group_name) VALUES (?, ?);");
            psAddGroup.setInt(1, bundleId);
            PreparedStatement psMetricId = db.prepareStatement("SELECT currval('sdw.e_metric_name_serial');");
            PreparedStatement psAddMetric = db.prepareStatement("INSERT INTO sdw.e_metric_name (bundle_id, metric_name) VALUES (?, ?);");
            psAddMetric.setInt(1, bundleId);
            PreparedStatement psAddGroup2Metric = db.prepareStatement("INSERT INTO sdw.e_metric_groups (metric_name_id, metric_group_id) VALUES (?, ?);");
            Iterator<MetricGroup> i = bundle.getAllMetricGroups();
            while (i.hasNext()) {
                MetricGroup grp = i.next();
                psAddGroup.setString(2, grp.groupName);
                if (1 != db.executeUpdate(psAddGroup)) throw new IllegalStateException("Could not add group " + grp.groupName + " from " + xmlFileName);
                int groupId = DbHelper.getIntKey(psGroupId);
                psAddGroup2Metric.setInt(2, groupId);
                Iterator<String> j = grp.getAllMetricNames();
                while (j.hasNext()) {
                    String metricName = j.next();
                    psAddMetric.setString(2, metricName);
                    if (1 != db.executeUpdate(psAddMetric)) throw new IllegalStateException("Could not add " + metricName + " from " + xmlFileName);
                    int metricId = DbHelper.getIntKey(psMetricId);
                    psAddGroup2Metric.setInt(1, metricId);
                    if (1 != db.executeUpdate(psAddGroup2Metric)) throw new IllegalStateException("Could not add group " + grp.groupName + " -> " + metricName + " from " + xmlFileName);
                }
            }
            return true;
        } catch (Exception e) {
            db.rollback();
            throw e;
        } finally {
            db.commitUnless();
        }
    }
