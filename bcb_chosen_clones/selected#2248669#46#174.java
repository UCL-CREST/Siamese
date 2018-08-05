    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/octet-stream");
        String reportIds = request.getParameter("reportIds");
        String dashboardIds = request.getParameter("dashboardIds");
        ArrayList<Object> xmlDatas = new ArrayList<Object>();
        TreeMap<String, ReportCategory> categories = new TreeMap<String, ReportCategory>();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        TreeMap<String, Report> reports = new TreeMap<String, Report>();
        if (reportIds != null) {
            for (StringTokenizer st = new StringTokenizer(reportIds, ","); st.hasMoreTokens(); ) {
                Long reportId = Long.valueOf(st.nextToken());
                Report report = getReportService().findReportById(reportId);
                reports.put(report.getName(), report);
                if (!categories.containsKey(report.getCategory().getName())) {
                    categories.put(report.getCategory().getName(), report.getCategory());
                }
            }
        }
        List<Dashboard> dashboards = new ArrayList<Dashboard>();
        if (dashboardIds != null) {
            for (StringTokenizer st = new StringTokenizer(dashboardIds, ","); st.hasMoreTokens(); ) {
                Long dashboardId = Long.valueOf(st.nextToken());
                Dashboard dashboard = getReportService().findDashboardById(dashboardId);
                dashboards.add(dashboard);
                if (!categories.containsKey(dashboard.getCategory().getName())) {
                    categories.put(dashboard.getCategory().getName(), dashboard.getCategory());
                }
                for (DashboardColumn column : dashboard.getColumns()) {
                    for (DashboardReport dashboardReport : column.getDashboardReports()) {
                        Report report = dashboardReport.getReport();
                        if (!reports.containsKey(report.getName())) {
                            reports.put(report.getName(), report);
                            if (!categories.containsKey(report.getCategory().getName())) {
                                categories.put(report.getCategory().getName(), report.getCategory());
                            }
                        }
                    }
                }
            }
        }
        for (ReportCategory category : categories.values()) {
            XmlReportCategory xmlCategory = new XmlReportCategory();
            xmlCategory.setName(category.getName());
            xmlDatas.add(xmlCategory);
        }
        for (Report report : reports.values()) {
            XmlReport xmlReport = new XmlReport();
            xmlReport.setName(report.getName());
            xmlReport.setHidden(report.getHidden());
            xmlReport.setReportCategoryName(report.getCategory().getName());
            String fileName = report.getName() + ".rptdesign";
            xmlReport.setReportContentPath(fileName);
            ZipEntry ze = new ZipEntry(fileName);
            zos.putNextEntry(ze);
            IOUtils.write(report.getReportContent(), zos);
            zos.closeEntry();
            xmlReport.setReportingSystem(report.getReportingSystem().getName());
            List<XmlReportParam> xmlReportParams = new ArrayList<XmlReportParam>();
            for (ReportParam param : report.getParams()) {
                XmlReportParam xmlReportParam = new XmlReportParam();
                xmlReportParam.setAsk(param.getAsk());
                xmlReportParam.setChooser(param.getChooser().getName());
                xmlReportParam.setDefault(param.getDefault());
                xmlReportParam.setName(param.getName());
                xmlReportParam.setPrompt(param.getPrompt());
                xmlReportParams.add(xmlReportParam);
            }
            xmlReport.setReportParams(xmlReportParams);
            xmlDatas.add(xmlReport);
        }
        for (Dashboard dashboard : dashboards) {
            XmlDashboard xmlDashboard = new XmlDashboard();
            xmlDashboard.setName(dashboard.getName());
            xmlDashboard.setHidden(dashboard.getHidden());
            xmlDashboard.setReportCategoryName(dashboard.getCategory().getName());
            List<XmlDashboardColumn> xmlDashboardColumns = new ArrayList<XmlDashboardColumn>();
            for (DashboardColumn dashboardColumn : dashboard.getColumns()) {
                XmlDashboardColumn xmlDashboardColumn = new XmlDashboardColumn();
                xmlDashboardColumn.setIndex(dashboardColumn.getIndex());
                xmlDashboardColumn.setWidth(dashboardColumn.getWidth());
                List<XmlDashboardReport> xmlDashboardReports = new ArrayList<XmlDashboardReport>();
                for (DashboardReport dashboardReport : dashboardColumn.getDashboardReports()) {
                    XmlDashboardReport xmlDashboardReport = new XmlDashboardReport();
                    xmlDashboardReport.setHeight(dashboardReport.getHeight());
                    xmlDashboardReport.setReportName(dashboardReport.getReport().getName());
                    xmlDashboardReport.setRowIndex(dashboardReport.getRowIndex());
                    xmlDashboardReport.setTitle(dashboardReport.getTitle());
                    List<XmlDashboardParamBinding> xmlDashboardParamBindings = new ArrayList<XmlDashboardParamBinding>();
                    for (DashboardParamBinding dashboardParamBinding : dashboardReport.getDashboardParamBindings()) {
                        XmlDashboardParamBinding xmlDashboardParamBinding = new XmlDashboardParamBinding();
                        xmlDashboardParamBinding.setDashboardParamName(dashboardParamBinding.getDashboardParam().getName());
                        xmlDashboardParamBinding.setReportParamName(dashboardParamBinding.getReportParam().getName());
                        xmlDashboardParamBindings.add(xmlDashboardParamBinding);
                    }
                    xmlDashboardReport.setDashboardParamBindings(xmlDashboardParamBindings);
                    xmlDashboardReports.add(xmlDashboardReport);
                }
                xmlDashboardColumn.setDashboardReports(xmlDashboardReports);
                xmlDashboardColumns.add(xmlDashboardColumn);
            }
            xmlDashboard.setDashboardColumns(xmlDashboardColumns);
            List<XmlReportParam> xmlReportParams = new ArrayList<XmlReportParam>();
            for (ReportParam param : dashboard.getParams()) {
                XmlReportParam xmlReportParam = new XmlReportParam();
                xmlReportParam.setAsk(param.getAsk());
                xmlReportParam.setChooser(param.getChooser().getName());
                xmlReportParam.setDefault(param.getDefault());
                xmlReportParam.setName(param.getName());
                xmlReportParam.setPrompt(param.getPrompt());
                xmlReportParams.add(xmlReportParam);
            }
            xmlDashboard.setReportParams(xmlReportParams);
            xmlDatas.add(xmlDashboard);
        }
        ZipEntry ze = new ZipEntry("report-datas.xml");
        zos.putNextEntry(ze);
        XStream xstream = new XStream();
        String xmlDatasAsString = xstream.toXML(xmlDatas);
        IOUtils.write(xmlDatasAsString.getBytes(), zos);
        zos.closeEntry();
        zos.close();
        byte[] zipContent = baos.toByteArray();
        response.setContentLength(zipContent.length);
        response.setHeader("Content-Disposition", "attachment; filename=\"appspy-export.zip\"");
        OutputStream os = response.getOutputStream();
        org.apache.commons.io.IOUtils.write(zipContent, os);
        os.close();
    }
