    public static void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException {
        log.debug("Start MemberPortletActionMethod.processAction()");
        MemberProcessingActionRequest mp = null;
        try {
            ModuleManager moduleManager = ModuleManager.getInstance(PropertiesProvider.getConfigPath());
            mp = new MemberProcessingActionRequest(actionRequest, moduleManager);
            String moduleName = RequestTools.getString(actionRequest, MemberConstants.MEMBER_MODULE_PARAM);
            String actionName = RequestTools.getString(actionRequest, MemberConstants.MEMBER_ACTION_PARAM);
            String subActionName = RequestTools.getString(actionRequest, MemberConstants.MEMBER_SUBACTION_PARAM).trim();
            if (log.isDebugEnabled()) {
                Map parameterMap = actionRequest.getParameterMap();
                if (!parameterMap.entrySet().isEmpty()) {
                    log.debug("Action request parameter");
                    for (Object o : parameterMap.entrySet()) {
                        Map.Entry entry = (Map.Entry) o;
                        log.debug("    key: " + entry.getKey() + ", value: " + entry.getValue());
                    }
                } else {
                    log.debug("Action request map is empty");
                }
                log.debug("   Point #4.1 module '" + moduleName + "'");
                log.debug("   Point #4.2 action '" + actionName + "'");
                log.debug("   Point #4.3 subAction '" + subActionName + "'");
            }
            if (mp.mod == null) {
                actionResponse.setRenderParameter(MemberConstants.ERROR_TEXT, "Point #4.2. Module '" + moduleName + "' not found");
                return;
            }
            if (mp.mod.getType() != null && mp.mod.getType().getType() == ModuleTypeTypeType.LOOKUP_TYPE && (mp.getFromParam() == null || mp.getFromParam().length() == 0)) {
                actionResponse.setRenderParameter(MemberConstants.ERROR_TEXT, "Point #4.4. Module " + moduleName + " is lookup module");
                return;
            }
            int actionType = ContentTypeActionType.valueOf(actionName).getType();
            if (log.isDebugEnabled()) {
                log.debug("action name " + actionName);
                log.debug("ContentTypeActionType " + ContentTypeActionType.valueOf(actionName).toString());
                log.debug("action type " + actionType);
            }
            mp.content = MemberServiceClass.getContent(mp.mod, actionType);
            if (mp.content == null) {
                actionResponse.setRenderParameter(MemberConstants.ERROR_TEXT, "Module: '" + moduleName + "', action '" + actionName + "', not found");
                return;
            }
            if (log.isDebugEnabled()) {
                log.debug("Debug. Unmarshal sqlCache object");
                synchronized (syncFile) {
                    XmlTools.writeToFile(mp.content.getQueryArea().getSqlCache(), SiteUtils.getTempDir() + File.separatorChar + "member-content-site-start-0.xml", "windows-1251");
                }
            }
            if (!MemberServiceClass.checkRole(actionRequest, mp.content)) {
                actionResponse.setRenderParameter(MemberConstants.ERROR_TEXT, "Access denied");
                return;
            }
            if (log.isDebugEnabled()) {
                log.debug("Unmarshal sqlCache object");
                synchronized (syncFile) {
                    XmlTools.writeToFile(mp.content.getQueryArea().getSqlCache(), SiteUtils.getTempDir() + File.separatorChar + "member-content-site-start-2.xml", "windows-1251");
                }
            }
            initRenderParameters(actionRequest.getParameterMap(), actionResponse);
            if ("commit".equalsIgnoreCase(subActionName)) {
                DatabaseAdapter dbDyn = null;
                PreparedStatement ps = null;
                try {
                    dbDyn = mp.getDatabaseAdapter();
                    int i1;
                    switch(actionType) {
                        case ContentTypeActionType.INSERT_TYPE:
                            if (log.isDebugEnabled()) log.debug("Start prepare data for inserting.");
                            String validateStatus = mp.validateFields(dbDyn);
                            if (log.isDebugEnabled()) log.debug("Validating status - " + validateStatus);
                            if (validateStatus != null) {
                                WebmillErrorPage.setErrorInfo(actionResponse, validateStatus, MemberConstants.ERROR_TEXT, null, "Continue", MemberConstants.ERROR_URL_NAME);
                                return;
                            }
                            if (log.isDebugEnabled()) {
                                log.debug("Unmarshal sqlCache object");
                                synchronized (syncFile) {
                                    XmlTools.writeToFile(mp.content.getQueryArea().getSqlCache(), SiteUtils.getTempDir() + File.separatorChar + "member-content-before-yesno.xml", "windows-1251");
                                }
                            }
                            if (log.isDebugEnabled()) log.debug("Start looking for field with type " + FieldsTypeJspTypeType.YES_1_NO_N.toString());
                            if (MemberServiceClass.hasYesNoField(actionRequest.getParameterMap(), mp.mod, mp.content)) {
                                if (log.isDebugEnabled()) log.debug("Found field with type " + FieldsTypeJspTypeType.YES_1_NO_N.toString());
                                mp.process_Yes_1_No_N_Fields(dbDyn);
                            } else {
                                if (log.isDebugEnabled()) log.debug("Field with type " + FieldsTypeJspTypeType.YES_1_NO_N.toString() + " not found");
                            }
                            String sql_ = MemberServiceClass.buildInsertSQL(mp.content, mp.getFromParam(), mp.mod, dbDyn, actionRequest.getServerName(), mp.getModuleManager(), mp.authSession);
                            if (log.isDebugEnabled()) {
                                log.debug("insert SQL:\n" + sql_ + "\n");
                                log.debug("Unmarshal sqlCache object");
                                synchronized (syncFile) {
                                    XmlTools.writeToFile(mp.content.getQueryArea().getSqlCache(), SiteUtils.getTempDir() + File.separatorChar + "member-content.xml", "windows-1251");
                                }
                            }
                            boolean checkStatus = false;
                            switch(dbDyn.getFamaly()) {
                                case DatabaseManager.MYSQL_FAMALY:
                                    break;
                                default:
                                    checkStatus = mp.checkRestrict();
                                    if (!checkStatus) throw new ServletException("check status of restrict failed");
                                    break;
                            }
                            if (log.isDebugEnabled()) log.debug("check status - " + checkStatus);
                            ps = dbDyn.prepareStatement(sql_);
                            Object idNewRec = mp.bindInsert(dbDyn, ps);
                            i1 = ps.executeUpdate();
                            if (log.isDebugEnabled()) log.debug("Number of inserter record - " + i1);
                            DatabaseManager.close(ps);
                            ps = null;
                            if (log.isDebugEnabled()) {
                                outputDebugOfInsertStatus(mp, dbDyn, idNewRec);
                            }
                            mp.prepareBigtextData(dbDyn, idNewRec, false);
                            for (int i = 0; i < mp.mod.getRelateClassCount(); i++) {
                                RelateClassType rc = mp.mod.getRelateClass(i);
                                if (log.isDebugEnabled()) log.debug("#7.003.003 terminate class " + rc.getClassName());
                                CacheFactory.terminate(rc.getClassName(), null, Boolean.TRUE.equals(rc.getIsFullReinitCache()));
                            }
                            break;
                        case ContentTypeActionType.CHANGE_TYPE:
                            if (log.isDebugEnabled()) log.debug("Commit change page");
                            validateStatus = mp.validateFields(dbDyn);
                            if (validateStatus != null) {
                                WebmillErrorPage.setErrorInfo(actionResponse, validateStatus, MemberConstants.ERROR_TEXT, null, "Continue", MemberConstants.ERROR_URL_NAME);
                                return;
                            }
                            if (MemberServiceClass.hasYesNoField(actionRequest.getParameterMap(), mp.mod, mp.content)) {
                                if (log.isDebugEnabled()) log.debug("Found field with type " + FieldsTypeJspTypeType.YES_1_NO_N);
                                mp.process_Yes_1_No_N_Fields(dbDyn);
                            }
                            Object idCurrRec;
                            if (log.isDebugEnabled()) log.debug("PrimaryKeyType " + mp.content.getQueryArea().getPrimaryKeyType());
                            switch(mp.content.getQueryArea().getPrimaryKeyType().getType()) {
                                case PrimaryKeyTypeType.NUMBER_TYPE:
                                    log.debug("PrimaryKeyType - 'number'");
                                    idCurrRec = PortletService.getLong(actionRequest, mp.mod.getName() + '.' + mp.content.getQueryArea().getPrimaryKey());
                                    break;
                                case PrimaryKeyTypeType.STRING_TYPE:
                                    log.debug("PrimaryKeyType - 'string'");
                                    idCurrRec = RequestTools.getString(actionRequest, mp.mod.getName() + '.' + mp.content.getQueryArea().getPrimaryKey());
                                    break;
                                default:
                                    throw new Exception("Change. Wrong type of primary key - " + mp.content.getQueryArea().getPrimaryKeyType());
                            }
                            if (log.isDebugEnabled()) log.debug("mp.isSimpleField(): " + mp.isSimpleField());
                            if (mp.isSimpleField()) {
                                log.debug("start build SQL");
                                sql_ = MemberServiceClass.buildUpdateSQL(dbDyn, mp.content, mp.getFromParam(), mp.mod, true, actionRequest.getParameterMap(), actionRequest.getRemoteUser(), actionRequest.getServerName(), mp.getModuleManager(), mp.authSession);
                                if (log.isDebugEnabled()) log.debug("update SQL:" + sql_);
                                ps = dbDyn.prepareStatement(sql_);
                                mp.bindUpdate(dbDyn, ps, idCurrRec, true);
                                i1 = ps.executeUpdate();
                                if (log.isDebugEnabled()) log.debug("Number of updated record - " + i1);
                            }
                            log.debug("prepare big text");
                            mp.prepareBigtextData(dbDyn, idCurrRec, true);
                            if (mp.content.getQueryArea().getPrimaryKeyType().getType() != PrimaryKeyTypeType.NUMBER_TYPE) throw new Exception("PK of 'Bigtext' table must be a 'number' type");
                            log.debug("start sync cache data");
                            for (int i = 0; i < mp.mod.getRelateClassCount(); i++) {
                                RelateClassType rc = mp.mod.getRelateClass(i);
                                if (log.isDebugEnabled()) log.debug("#7.003.002 terminate class " + rc.getClassName() + ", id_rec " + idCurrRec);
                                if (mp.content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.NUMBER_TYPE) {
                                    CacheFactory.terminate(rc.getClassName(), (Long) idCurrRec, Boolean.TRUE.equals(rc.getIsFullReinitCache()));
                                } else {
                                    actionResponse.setRenderParameter(MemberConstants.ERROR_TEXT, "Change. Wrong type of primary key - " + mp.content.getQueryArea().getPrimaryKeyType());
                                    return;
                                }
                            }
                            break;
                        case ContentTypeActionType.DELETE_TYPE:
                            log.debug("Commit delete page<br>");
                            Object idRec;
                            if (mp.content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.NUMBER_TYPE) {
                                idRec = PortletService.getLong(actionRequest, mp.mod.getName() + '.' + mp.content.getQueryArea().getPrimaryKey());
                            } else if (mp.content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.STRING_TYPE) {
                                idRec = RequestTools.getString(actionRequest, mp.mod.getName() + '.' + mp.content.getQueryArea().getPrimaryKey());
                            } else {
                                actionResponse.setRenderParameter(MemberConstants.ERROR_TEXT, "Delete. Wrong type of primary key - " + mp.content.getQueryArea().getPrimaryKeyType());
                                return;
                            }
                            if (dbDyn.getFamaly() == DatabaseManager.MYSQL_FAMALY) mp.deleteBigtextData(dbDyn, idRec);
                            sql_ = MemberServiceClass.buildDeleteSQL(dbDyn, mp.mod, mp.content, mp.getFromParam(), actionRequest.getParameterMap(), actionRequest.getRemoteUser(), actionRequest.getServerName(), moduleManager, mp.authSession);
                            if (log.isDebugEnabled()) log.debug("delete SQL: " + sql_ + "<br>\n");
                            ps = dbDyn.prepareStatement(sql_);
                            mp.bindDelete(ps);
                            i1 = ps.executeUpdate();
                            if (log.isDebugEnabled()) log.debug("Number of deleted record - " + i1);
                            if (idRec != null && (idRec instanceof Long)) {
                                for (int i = 0; i < mp.mod.getRelateClassCount(); i++) {
                                    RelateClassType rc = mp.mod.getRelateClass(i);
                                    if (log.isDebugEnabled()) log.debug("#7.003.001 terminate class " + rc.getClassName() + ", id_rec " + idRec.toString());
                                    CacheFactory.terminate(rc.getClassName(), (Long) idRec, Boolean.TRUE.equals(rc.getIsFullReinitCache()));
                                }
                            }
                            break;
                        default:
                            actionResponse.setRenderParameter(MemberConstants.ERROR_TEXT, "Unknown type of action - " + actionName);
                            return;
                    }
                    log.debug("do commit");
                    dbDyn.commit();
                } catch (Exception e1) {
                    try {
                        dbDyn.rollback();
                    } catch (Exception e001) {
                        log.info("error in rolback()");
                    }
                    log.error("Error while processing this page", e1);
                    if (dbDyn.testExceptionIndexUniqueKey(e1)) {
                        WebmillErrorPage.setErrorInfo(actionResponse, "You input value already exists in DB. Try again with other value", MemberConstants.ERROR_TEXT, null, "Continue", MemberConstants.ERROR_URL_NAME);
                    } else {
                        WebmillErrorPage.setErrorInfo(actionResponse, "Error while processing request", MemberConstants.ERROR_TEXT, e1, "Continue", MemberConstants.ERROR_URL_NAME);
                    }
                } finally {
                    DatabaseManager.close(dbDyn, ps);
                }
            }
        } catch (Exception e) {
            final String es = "General processing error ";
            log.error(es, e);
            throw new PortletException(es, e);
        } finally {
            if (mp != null) {
                mp.destroy();
            }
        }
    }
