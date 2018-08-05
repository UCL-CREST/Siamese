    public void doGetZip(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            int dayIdFrom = Integer.parseInt(req.getParameter("dateFrom"));
            int dayIdTo = Integer.parseInt(req.getParameter("dateTo"));
            WDCDay dateFrom = new WDCDay(dayIdFrom);
            WDCDay dateTo = new WDCDay(dayIdTo);
            DateInterval dateInterval = new DateInterval(dateFrom, dateTo);
            long curTime = (new java.util.Date()).getTime();
            res.addHeader("content-disposition", "attachment; filename=spidr_" + curTime + ".zip");
            String elements = req.getParameter("elem");
            String[] elemArray = elements.split(";");
            Connection con = null;
            Statement stmt = null;
            WDCTable metaElem = null;
            try {
                con = ConnectionPool.getConnection("metadata");
                stmt = con.createStatement();
                metaElem = new WDCTable(stmt, SQL_LOAD_META_ELEMENTS);
                stmt.close();
            } finally {
                try {
                    ConnectionPool.releaseConnection(con);
                } catch (Exception ignore) {
                }
            }
            LocalApi api = new LocalApi();
            DataSequenceSet dss = new DataSequenceSet("");
            for (int i = 0; i < elemArray.length; i++) {
                String[] s = elemArray[i].split(",");
                String elem = s[0];
                int sampling = Integer.parseInt(s[1]);
                if (sampling < 0) {
                    sampling = 0;
                }
                String table = s[2];
                Station stn = null;
                if (s.length == 4) {
                    stn = new Station(s[3], table, "");
                }
                int indElemElement = metaElem.getColumnIndex("element");
                int indElemTable = metaElem.getColumnIndex("elemTable");
                int indElemDescription = metaElem.getColumnIndex("description");
                int indElemMultiplier = metaElem.getColumnIndex("multiplier");
                int indElemMissingValue = metaElem.getColumnIndex("missingValue");
                int indElemUnits = metaElem.getColumnIndex("units");
                int elInd = metaElem.findRow(indElemTable, table, indElemElement, elem);
                String elemDescr = (String) metaElem.getValueAt(elInd, indElemDescription);
                String elemUnits = (String) metaElem.getValueAt(elInd, indElemUnits);
                float multiplier = Float.parseFloat((String) metaElem.getValueAt(elInd, indElemMultiplier));
                float missingValue = Float.parseFloat((String) metaElem.getValueAt(elInd, indElemMissingValue));
                DataDescription descr = new DataDescription(table, elem, "", elemDescr, elemUnits, elem);
                descr.setMultiplier(multiplier);
                descr.setMissingValue(missingValue);
                Vector v = api.getData(descr, stn, dateInterval, sampling);
                if (v != null && v.size() != 0) {
                    DataSequence ds = (DataSequence) v.get(0);
                    dss.add(ds);
                }
            }
            for (int i = 0; i < dss.size(); i++) {
                String group = "";
                DailyData dd = null;
                try {
                    dd = (DailyData) ((DataSequence) dss.elementAt(i)).elementAt(0);
                    DataDescription ddescr = dd.getDescription();
                    String table = ddescr.getTable();
                    String[] groupList = Utilities.splitString(Settings.get("viewGroups.groupOrder"));
                    boolean flag = false;
                    for (int k = 0; (k < groupList.length) && !flag; k++) {
                        String[] tables = UpdateMetadata.getTablesForGroup(groupList[k]);
                        for (int j = 0; (j < tables.length) && !flag; j++) {
                            if (tables[j].equals(table)) {
                                flag = true;
                                group = groupList[k];
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("Unexpected error: " + e.toString());
                }
                ZipOutputStream zos = new ZipOutputStream(res.getOutputStream());
                try {
                    String spidrServerName = Settings.get("sites.localSite");
                    String spidrServerUrl = Settings.get("sites." + spidrServerName + ".url");
                    String metadataCollection = Settings.get("viewGroups." + group + ".metadataCollection");
                    String fileUrl = spidrServerUrl + (spidrServerUrl.endsWith("/") ? "" : "/") + "osproxy.do?specialRequest=document&docId=" + metadataCollection + dd.getStation().getStn().split("_")[0];
                    BufferedReader in = new BufferedReader(new InputStreamReader((new URL(fileUrl)).openStream()));
                    zos.putNextEntry(new ZipEntry(dd.getStation().getStn() + ".xml"));
                    int buf;
                    while ((buf = in.read()) != -1) {
                        zos.write(buf);
                    }
                    in.close();
                } catch (Exception e) {
                    log.error("Couldn't add metadata to ZIP file: " + e.toString());
                }
                String entryFileName = "spidr_" + curTime + "_" + i + ".txt";
                zos.putNextEntry(new ZipEntry(entryFileName));
                PrintWriter plt = new PrintWriter(zos);
                if (log.isDebugEnabled()) {
                    log.debug("asciiExportDataSet() files are ready");
                }
                String dateFormat = "yyyy-MM-dd HH:mm";
                TimeZone tz = new SimpleTimeZone(0, "GMT");
                Calendar utc = new GregorianCalendar(tz);
                utc.setTime(new java.util.Date());
                SimpleDateFormat df = new SimpleDateFormat(dateFormat, Locale.US);
                df.setTimeZone(tz);
                df.setCalendar(utc);
                plt.println("#Spidr data output file in ASCII format created at " + df.format(utc.getTime()));
                if (log.isDebugEnabled()) {
                    log.debug("asciiExportDataSet() datestamp is ready");
                }
                plt.println("#GMT time is used");
                plt.println("#");
                plt.println("#");
                plt.println("#--------------------------------------------------");
                for (int j = 0; j < dss.size(); j++) {
                    DSSExport.vec2stream(plt, (DataSequence) dss.elementAt(j));
                }
                plt.close();
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
