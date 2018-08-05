    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        super.execute(context);
        debug("Start execute job " + this.getClass().getName());
        String dir = this.path_app_root + "/" + this.properties.get("dir") + "/";
        try {
            File dir_f = new File(dir);
            if (!dir_f.exists()) {
                debug("(0) - make dir: " + dir_f + " - ");
                org.apache.commons.io.FileUtils.forceMkdir(dir_f);
            }
        } catch (IOException ex) {
            fatal("IOException", ex);
        }
        debug("Files:" + this.properties.get("url"));
        String[] url_to_download = properties.get("url").split(";");
        for (String u : url_to_download) {
            if (StringUtil.isNullOrEmpty(u)) {
                continue;
            }
            u = StringUtil.trim(u);
            debug("(0) url: " + u);
            String f_name = u.substring(u.lastIndexOf("/"), u.length());
            debug("(1) - start download: " + u + " to file name: " + new File(dir + "/" + f_name).toString());
            com.utils.HttpUtil.downloadData(u, new File(dir + "/" + f_name).toString());
        }
        try {
            conn_stats.setAutoCommit(false);
        } catch (SQLException e) {
            fatal("SQLException", e);
        }
        String[] query_delete = properties.get("query_delete").split(";");
        for (String q : query_delete) {
            if (StringUtil.isNullOrEmpty(q)) {
                continue;
            }
            q = StringUtil.trim(q);
            debug("(2) - " + q);
            try {
                Statement stat = conn_stats.createStatement();
                stat.executeUpdate(q);
                stat.close();
            } catch (SQLException e) {
                try {
                    conn_stats.rollback();
                } catch (SQLException ex) {
                    fatal("SQLException", ex);
                }
                fatal("SQLException", e);
            }
        }
        for (String u : url_to_download) {
            if (StringUtil.isNullOrEmpty(u)) {
                continue;
            }
            u = StringUtil.trim(u);
            try {
                Statement stat = conn_stats.createStatement();
                String f_name = new File(dir + "/" + u.substring(u.lastIndexOf("/"), u.length())).toString();
                debug("(3) - start import: " + f_name);
                BigFile lines = new BigFile(f_name);
                int n = 0;
                for (String l : lines) {
                    String fields[] = l.split(",");
                    String query = "";
                    if (f_name.indexOf("hip_countries.csv") != -1) {
                        query = "insert into hip_countries values (" + fields[0] + ",'" + normalize(fields[1]) + "','" + normalize(fields[2]) + "')";
                    } else if (f_name.indexOf("hip_ip4_city_lat_lng.csv") != -1) {
                        query = "insert into hip_ip4_city_lat_lng values (" + fields[0] + ",'" + normalize(fields[1]) + "'," + fields[2] + "," + fields[3] + ")";
                    } else if (f_name.indexOf("hip_ip4_country.csv") != -1) {
                        query = "insert into hip_ip4_country values (" + fields[0] + "," + fields[1] + ")";
                    }
                    debug(n + " - " + query);
                    stat.executeUpdate(query);
                    n++;
                }
                debug("(4) tot import per il file" + f_name + " : " + n);
                stat.close();
                new File(f_name).delete();
            } catch (SQLException ex) {
                fatal("SQLException", ex);
                try {
                    conn_stats.rollback();
                } catch (SQLException ex2) {
                    fatal("SQLException", ex2);
                }
            } catch (IOException ex) {
                fatal("IOException", ex);
            } catch (Exception ex3) {
                fatal("Exception", ex3);
            }
        }
        try {
            conn_stats.commit();
        } catch (SQLException e) {
            fatal("SQLException", e);
        }
        try {
            conn_stats.setAutoCommit(true);
        } catch (SQLException e) {
            fatal("SQLException", e);
        }
        try {
            debug("(6) Vacuum");
            Statement stat = this.conn_stats.createStatement();
            stat.executeUpdate("VACUUM");
            stat.close();
        } catch (SQLException e) {
            fatal("SQLException", e);
        }
        debug("End execute job " + this.getClass().getName());
    }
