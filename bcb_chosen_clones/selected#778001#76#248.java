    public void processAction(ActionMapping mapping, ActionForm form, PortletConfig config, ActionRequest req, ActionResponse res) throws Exception {
        boolean editor = false;
        req.setAttribute(ViewReportsAction.REPORT_EDITOR_OR_ADMIN, false);
        User user = _getUser(req);
        List<Role> roles = RoleFactory.getAllRolesForUser(user.getUserId());
        for (Role role : roles) {
            if (role.getName().equals("Report Administrator") || role.getName().equals("Report Editor") || role.getName().equals("CMS Administrator")) {
                req.setAttribute(ViewReportsAction.REPORT_EDITOR_OR_ADMIN, true);
                editor = true;
                break;
            }
        }
        requiresInput = false;
        badParameters = false;
        newReport = false;
        ActionRequestImpl reqImpl = (ActionRequestImpl) req;
        HttpServletRequest httpReq = reqImpl.getHttpServletRequest();
        String cmd = req.getParameter(Constants.CMD);
        Logger.debug(this, "Inside EditReportAction cmd=" + cmd);
        ReportForm rfm = (ReportForm) form;
        ArrayList<String> ds = (DbConnectionFactory.getAllDataSources());
        ArrayList<DataSource> dsResults = new ArrayList<DataSource>();
        for (String dataSource : ds) {
            DataSource d = rfm.getNewDataSource();
            if (dataSource.equals(com.dotmarketing.util.Constants.DATABASE_DEFAULT_DATASOURCE)) {
                d.setDsName("DotCMS Datasource");
            } else {
                d.setDsName(dataSource);
            }
            dsResults.add(d);
        }
        rfm.setDataSources(dsResults);
        httpReq.setAttribute("dataSources", rfm.getDataSources());
        Long reportId = UtilMethods.parseLong(req.getParameter("reportId"), 0);
        String referrer = req.getParameter("referrer");
        if (reportId > 0) {
            report = ReportFactory.getReport(reportId);
            ArrayList<String> adminRoles = new ArrayList<String>();
            adminRoles.add(com.dotmarketing.util.Constants.ROLE_REPORT_ADMINISTRATOR);
            if (user.getUserId().equals(report.getOwner())) {
                _checkWritePermissions(report, user, httpReq, adminRoles);
            }
            if (cmd == null || !cmd.equals(Constants.EDIT)) {
                rfm.setSelectedDataSource(report.getDs());
                rfm.setReportName(report.getReportName());
                rfm.setReportDescription(report.getReportDescription());
                rfm.setReportId(report.getInode());
                rfm.setWebFormReport(report.isWebFormReport());
                httpReq.setAttribute("selectedDS", report.getDs());
            }
        } else {
            if (!editor) {
                throw new DotRuntimeException("user not allowed to create a new report");
            }
            report = new Report();
            report.setOwner(_getUser(req).getUserId());
            newReport = true;
        }
        req.setAttribute(WebKeys.PERMISSION_INODE_EDIT, report);
        if ((cmd != null) && cmd.equals(Constants.EDIT)) {
            if (Validator.validate(req, form, mapping)) {
                report.setReportName(rfm.getReportName());
                report.setReportDescription(rfm.getReportDescription());
                report.setWebFormReport(rfm.isWebFormReport());
                if (rfm.isWebFormReport()) report.setDs("None"); else report.setDs(rfm.getSelectedDataSource());
                String jrxmlPath = "";
                String jasperPath = "";
                try {
                    HibernateUtil.startTransaction();
                    ReportFactory.saveReport(report);
                    _applyPermissions(req, report);
                    if (!rfm.isWebFormReport()) {
                        if (UtilMethods.isSet(Config.getStringProperty("ASSET_REAL_PATH"))) {
                            jrxmlPath = Config.getStringProperty("ASSET_REAL_PATH") + File.separator + Config.getStringProperty("REPORT_PATH") + File.separator + report.getInode() + ".jrxml";
                            jasperPath = Config.getStringProperty("ASSET_REAL_PATH") + File.separator + Config.getStringProperty("REPORT_PATH") + File.separator + report.getInode() + ".jasper";
                        } else {
                            jrxmlPath = Config.CONTEXT.getRealPath(File.separator + Config.getStringProperty("REPORT_PATH") + File.separator + report.getInode() + ".jrxml");
                            jasperPath = Config.CONTEXT.getRealPath(File.separator + Config.getStringProperty("REPORT_PATH") + File.separator + report.getInode() + ".jasper");
                        }
                        UploadPortletRequest upr = PortalUtil.getUploadPortletRequest(req);
                        File importFile = upr.getFile("jrxmlFile");
                        if (importFile.exists()) {
                            byte[] currentData = new byte[0];
                            FileInputStream is = new FileInputStream(importFile);
                            int size = is.available();
                            currentData = new byte[size];
                            is.read(currentData);
                            File f = new File(jrxmlPath);
                            FileChannel channelTo = new FileOutputStream(f).getChannel();
                            ByteBuffer currentDataBuffer = ByteBuffer.allocate(currentData.length);
                            currentDataBuffer.put(currentData);
                            currentDataBuffer.position(0);
                            channelTo.write(currentDataBuffer);
                            channelTo.force(false);
                            channelTo.close();
                            try {
                                JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);
                            } catch (Exception e) {
                                Logger.error(this, "Unable to compile or save jrxml: " + e.toString());
                                try {
                                    f = new File(jrxmlPath);
                                    f.delete();
                                } catch (Exception ex) {
                                    Logger.info(this, "Unable to delete jrxml. This is usually a permissions problem.");
                                }
                                try {
                                    f = new File(jasperPath);
                                    f.delete();
                                } catch (Exception ex) {
                                    Logger.info(this, "Unable to delete jasper. This is usually a permissions problem.");
                                }
                                HibernateUtil.rollbackTransaction();
                                SessionMessages.add(req, "error", UtilMethods.htmlLineBreak(e.getMessage()));
                                setForward(req, "portlet.ext.report.edit_report");
                                return;
                            }
                            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperPath);
                            ReportParameterFactory.deleteReportsParameters(report);
                            _loadReportParameters(jasperReport.getParameters());
                            report.setRequiresInput(requiresInput);
                            HibernateUtil.save(report);
                        } else if (newReport) {
                            HibernateUtil.rollbackTransaction();
                            SessionMessages.add(req, "error", "message.report.compile.error");
                            setForward(req, "portlet.ext.report.edit_report");
                            return;
                        }
                    }
                    HibernateUtil.commitTransaction();
                    HashMap params = new HashMap();
                    SessionMessages.add(req, "message", "message.report.upload.success");
                    params.put("struts_action", new String[] { "/ext/report/view_reports" });
                    referrer = com.dotmarketing.util.PortletURLUtil.getRenderURL(((ActionRequestImpl) req).getHttpServletRequest(), javax.portlet.WindowState.MAXIMIZED.toString(), params);
                    _sendToReferral(req, res, referrer);
                    return;
                } catch (Exception ex) {
                    HibernateUtil.rollbackTransaction();
                    Logger.error(this, "Unable to save Report: " + ex.toString());
                    File f;
                    Logger.info(this, "Trying to delete jrxml");
                    try {
                        f = new File(jrxmlPath);
                        f.delete();
                    } catch (Exception e) {
                        Logger.info(this, "Unable to delete jrxml. This is usually because the file doesn't exist.");
                    }
                    try {
                        f = new File(jasperPath);
                        f.delete();
                    } catch (Exception e) {
                        Logger.info(this, "Unable to delete jasper. This is usually because the file doesn't exist.");
                    }
                    if (badParameters) {
                        SessionMessages.add(req, "error", ex.getMessage());
                    } else {
                        SessionMessages.add(req, "error", "message.report.compile.error");
                    }
                    setForward(req, "portlet.ext.report.edit_report");
                    return;
                }
            } else {
                setForward(req, "portlet.ext.report.edit_report");
            }
        }
        if ((cmd != null) && cmd.equals("downloadReportSource")) {
            ActionResponseImpl resImpl = (ActionResponseImpl) res;
            HttpServletResponse response = resImpl.getHttpServletResponse();
            if (!downloadSourceReport(reportId, httpReq, response)) {
                SessionMessages.add(req, "error", "message.report.source.file.not.found");
            }
        }
        setForward(req, "portlet.ext.report.edit_report");
    }
