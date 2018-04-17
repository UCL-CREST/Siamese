    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        boolean validClient = true;
        boolean validSession = false;
        String sessionKey = req.getParameter("sid");
        String storedKey = CLIENT_SESSION_KEYS.get(req.getRemoteAddr());
        if (sessionKey != null && storedKey != null && sessionKey.equals(storedKey)) validSession = true;
        DataStore ds = DataStore.getConnection();
        if (IPV6_DETECTED) {
            boolean doneWarning;
            synchronized (SJQServlet.class) {
                doneWarning = IPV6_WARNED;
                if (!IPV6_WARNED) IPV6_WARNED = true;
            }
            if (!doneWarning) LOG.warn("IPv6 interface detected; client restriction settings ignored [restrictions do not support IPv6 addresses]");
        } else {
            String[] clntRestrictions = ds.getSetting("ValidClients", "").split(";");
            List<IPMatcher> matchers = new ArrayList<IPMatcher>();
            if (clntRestrictions.length == 1 && clntRestrictions[0].trim().length() == 0) {
                LOG.warn("All client connections are being accepted and processed, please consider setting up client restrictions in SJQ settings");
            } else {
                for (String s : clntRestrictions) {
                    s = s.trim();
                    try {
                        matchers.add(new IPMatcher(s));
                    } catch (IPMatcherException e) {
                        LOG.error("Invalid client restriction settings; client restrictions ignored!", e);
                        matchers.clear();
                        break;
                    }
                }
                validClient = matchers.size() > 0 ? false : true;
                for (IPMatcher m : matchers) {
                    try {
                        if (m.match(req.getRemoteAddr())) {
                            validClient = true;
                            break;
                        }
                    } catch (IPMatcherException e) {
                        LOG.error("IPMatcherException", e);
                    }
                }
            }
        }
        String clntProto = req.getParameter("proto");
        if (clntProto == null || Integer.parseInt(clntProto) != SJQ_PROTO) throw new RuntimeException("Server is speaking protocol '" + SJQ_PROTO + "', but client is speaking protocol '" + clntProto + "'; install a client version that matches the server protocol version!");
        resp.setHeader("Content-Type", "text/plain");
        resp.setDateHeader("Expires", 0);
        resp.setDateHeader("Last-Modified", System.currentTimeMillis());
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        String cmd = req.getParameter("cmd");
        if (cmd == null) {
            DataStore.returnConnection(ds);
            return;
        }
        ActiveClientList list = ActiveClientList.getInstance();
        BufferedWriter bw = new BufferedWriter(resp.getWriter());
        if (cmd.equals("pop")) {
            if (!validClient) {
                LOG.warn("Client IP rejected: " + req.getRemoteAddr());
                notAuthorized(resp, bw);
            } else {
                list.add(req.getRemoteHost());
                ClientParser clnt = new ClientParser(new StringReader(ds.getClientConf(req.getRemoteHost())));
                String offDay = clnt.getGlobalOption("OFFDAY");
                String offHour = clnt.getGlobalOption("OFFHOUR");
                Calendar now = Calendar.getInstance();
                if (RangeInterpreter.inRange(now.get(Calendar.DAY_OF_WEEK), 1, 7, offDay) || RangeInterpreter.inRange(now.get(Calendar.HOUR_OF_DAY), 0, 23, offHour)) {
                    LOG.warn("Client '" + req.getRemoteAddr() + "' currently disabled via OFFDAY/OFFHOUR settings.");
                    bw.write("null");
                } else {
                    Task t = TaskQueue.getInstance().pop(req.getRemoteHost(), getPopCandidates(req.getRemoteHost(), clnt));
                    if (t == null) bw.write("null"); else {
                        t.setResourcesUsed(Integer.parseInt(clnt.getTask(t.getTaskId()).getOption("RESOURCES")));
                        Object obj = null;
                        if (t.getObjType().equals("media")) obj = Butler.SageApi.mediaFileAPI.GetMediaFileForID(Integer.parseInt(t.getObjId())); else if (t.getObjType().equals("sysmsg")) obj = SystemMessageUtils.getSysMsg(t.getObjId());
                        ClientTask cTask = clnt.getTask(t.getTaskId());
                        JSONObject jobj = cTask.toJSONObject(obj);
                        String objType = null;
                        try {
                            if (jobj != null) objType = jobj.getString(Task.JSON_OBJ_TYPE);
                        } catch (JSONException e) {
                            throw new RuntimeException("Invalid ClienTask JSON object conversion!");
                        }
                        if (obj == null || jobj == null) {
                            LOG.error("Source object has disappeared! [" + t.getObjType() + "/" + t.getObjId() + "]");
                            TaskQueue.getInstance().updateTask(t.getObjId(), t.getTaskId(), Task.State.FAILED, t.getObjType());
                            bw.write("null");
                        } else if (objType.equals("media")) {
                            try {
                                long ratio = calcRatio(jobj.getString(Task.JSON_OBJ_ID), jobj.getString(Task.JSON_NORECORDING));
                                if (ratio > 0 && new FieldTimeUntilNextRecording(null, "<=", ratio + "S").run()) {
                                    LOG.info("Client '" + req.getRemoteAddr() + "' cannot pop task '" + t.getObjType() + "/" + t.getTaskId() + "/" + t.getObjId() + "'; :NORECORDING option prevents running of this task");
                                    TaskQueue.getInstance().pushBack(t);
                                    bw.write("null");
                                } else bw.write(jobj.toString());
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        } else bw.write(jobj.toString());
                    }
                }
            }
        } else if (cmd.equals("update")) {
            if (!validClient) {
                LOG.warn("Client IP rejected: " + req.getRemoteAddr());
                notAuthorized(resp, bw);
            } else {
                list.add(req.getRemoteHost());
                try {
                    Task t = new Task(new JSONObject(req.getParameter("data")));
                    TaskQueue.getInstance().updateTask(t);
                } catch (JSONException e) {
                    throw new RuntimeException("Input error; client '" + req.getRemoteHost() + "', CMD: update", e);
                }
            }
        } else if (cmd.equals("showQ")) {
            if (validSession) bw.write(TaskQueue.getInstance().toJSONArray().toString()); else notAuthorized(resp, bw);
        } else if (cmd.equals("log")) {
            if (validSession) {
                String mediaId = req.getParameter("m");
                String taskId = req.getParameter("t");
                String objType = req.getParameter("o");
                if ((mediaId != null && !mediaId.equals("0")) && (taskId != null && !taskId.equals("0"))) bw.write(ds.readLog(mediaId, taskId, objType)); else {
                    BufferedReader r = new BufferedReader(new FileReader("sjq.log"));
                    String line;
                    while ((line = r.readLine()) != null) bw.write(line + "\n");
                    r.close();
                }
            } else notAuthorized(resp, bw);
        } else if (cmd.equals("appState")) {
            if (validSession) bw.write(Butler.dumpAppTrace()); else notAuthorized(resp, bw);
        } else if (cmd.equals("writeLog")) {
            if (!validClient) {
                LOG.warn("Client IP reject: " + req.getRemoteAddr());
                notAuthorized(resp, bw);
            } else {
                String mediaId = req.getParameter("m");
                String taskId;
                if (!mediaId.equals("-1")) taskId = req.getParameter("t"); else taskId = req.getRemoteHost();
                String objType = req.getParameter("o");
                if (!mediaId.equals("0") && Boolean.parseBoolean(ds.getSetting("IgnoreTaskOutput", "false"))) {
                    LOG.info("Dropping task output as per settings");
                    DataStore.returnConnection(ds);
                    return;
                }
                String data = req.getParameter("data");
                String[] msg = StringUtils.splitByWholeSeparator(data, "\r\n");
                if (msg == null || msg.length == 1) msg = StringUtils.split(data, '\r');
                if (msg == null || msg.length == 1) msg = StringUtils.split(data, '\n');
                long now = System.currentTimeMillis();
                for (String line : msg) ds.logForTaskClient(mediaId, taskId, line, now, objType);
                if (msg.length > 0) ds.flushLogs();
            }
        } else if (cmd.equals("ruleset")) {
            if (validSession) bw.write(ds.getSetting("ruleset", "")); else notAuthorized(resp, bw);
        } else if (cmd.equals("saveRuleset")) {
            if (validSession) {
                ds.setSetting("ruleset", req.getParameter("data"));
                bw.write("Success");
            } else notAuthorized(resp, bw);
        } else if (cmd.equals("getClients")) {
            if (validSession) bw.write(ActiveClientList.getInstance().toJSONArray().toString()); else notAuthorized(resp, bw);
        } else if (cmd.equals("loadClnt")) {
            if (validSession) bw.write(ds.getClientConf(req.getParameter("id"))); else notAuthorized(resp, bw);
        } else if (cmd.equals("saveClnt")) {
            if (validSession) {
                if (ds.saveClientConf(req.getParameter("id"), req.getParameter("data"))) bw.write("Success"); else bw.write("Failed");
            } else notAuthorized(resp, bw);
        } else if (cmd.equals("history")) {
            if (validSession) {
                int start, limit;
                try {
                    start = Integer.parseInt(req.getParameter("start"));
                    limit = Integer.parseInt(req.getParameter("limit"));
                } catch (NumberFormatException e) {
                    start = 0;
                    limit = -1;
                }
                bw.write(ds.getJobHistory(Integer.parseInt(req.getParameter("t")), start, limit, req.getParameter("sort")).toString());
            } else notAuthorized(resp, bw);
        } else if (cmd.equals("getSrvSetting")) {
            if (validSession) bw.write(ds.getSetting(req.getParameter("var"), "")); else notAuthorized(resp, bw);
        } else if (cmd.equals("setSrvSetting")) {
            if (validSession) {
                ds.setSetting(req.getParameter("var"), req.getParameter("val"));
                bw.write("Success");
            } else notAuthorized(resp, bw);
        } else if (cmd.equals("setFileCleaner")) {
            if (validSession) {
                ds.setSetting("DelRegex", req.getParameter("orphan"));
                ds.setSetting("IfRegex", req.getParameter("parent"));
                ds.setSetting("IgnoreRegex", req.getParameter("ignore"));
                new Thread(new FileCleaner()).start();
                bw.write("Success");
            } else notAuthorized(resp, bw);
        } else if (cmd.equals("getFileCleanerSettings")) {
            if (validSession) {
                bw.write(ds.getSetting("DelRegex", "") + "\n");
                bw.write(ds.getSetting("IfRegex", "") + "\n");
                bw.write(ds.getSetting("IgnoreRegex", ""));
            } else notAuthorized(resp, bw);
        } else if (cmd.equals("writeSrvSettings")) {
            if (validSession) {
                try {
                    ds.setSettings(new JSONObject(req.getParameter("data")));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                bw.write("Success");
            } else notAuthorized(resp, bw);
        } else if (cmd.equals("readSrvSettings")) {
            if (validSession) bw.write(ds.readSettings().toString()); else notAuthorized(resp, bw);
        } else if (cmd.equals("login")) {
            String pwd = ds.getSetting("password", "");
            try {
                MessageDigest msg = MessageDigest.getInstance("MD5");
                msg.update(req.getParameter("password").getBytes());
                String userPwd = new String(msg.digest());
                if (pwd.length() > 0 && pwd.equals(userPwd)) {
                    bw.write("Success");
                    int key = new java.util.Random().nextInt();
                    resp.addHeader("SJQ-Session-Token", Integer.toString(key));
                    CLIENT_SESSION_KEYS.put(req.getRemoteAddr(), Integer.toString(key));
                } else bw.write("BadPassword");
            } catch (NoSuchAlgorithmException e) {
                bw.write(e.getLocalizedMessage());
            }
        } else if (cmd.equals("editPwd")) {
            try {
                MessageDigest msg = MessageDigest.getInstance("MD5");
                String curPwd = ds.getSetting("password", "");
                String oldPwd = req.getParameter("old");
                msg.update(oldPwd.getBytes());
                oldPwd = new String(msg.digest());
                msg.reset();
                String newPwd = req.getParameter("new");
                String confPwd = req.getParameter("conf");
                if (!curPwd.equals(oldPwd)) bw.write("BadOld"); else if (!newPwd.equals(confPwd) || newPwd.length() == 0) bw.write("BadNew"); else {
                    msg.update(newPwd.getBytes());
                    newPwd = new String(msg.digest());
                    ds.setSetting("password", newPwd);
                    bw.write("Success");
                }
            } catch (NoSuchAlgorithmException e) {
                bw.write(e.getLocalizedMessage());
            }
        } else if (cmd.equals("runStats")) {
            if (validSession) {
                JSONObject o = new JSONObject();
                try {
                    o.put("last", Long.parseLong(ds.getSetting("LastRun", "0")));
                    o.put("next", Long.parseLong(ds.getSetting("NextRun", "0")));
                    bw.write(o.toString());
                } catch (JSONException e) {
                    bw.write(e.getLocalizedMessage());
                }
            } else notAuthorized(resp, bw);
        } else if (cmd.equals("runQLoader")) {
            if (validSession) {
                Butler.wakeQueueLoader();
                bw.write("Success");
            } else notAuthorized(resp, bw);
        } else if (cmd.equals("delActiveQ")) {
            if (validSession) {
                if (TaskQueue.getInstance().delete(req.getParameter("m"), req.getParameter("t"))) bw.write("Success"); else bw.write("Failed");
            } else notAuthorized(resp, bw);
        } else if (cmd.equals("clearActiveQ")) {
            if (validSession) {
                if (TaskQueue.getInstance().clear()) bw.write("Success"); else bw.write("Failed");
            } else notAuthorized(resp, bw);
        } else if (cmd.equals("editPri")) {
            if (validSession) {
                try {
                    int priority = Integer.parseInt(req.getParameter("p"));
                    if (TaskQueue.getInstance().editPriority(req.getParameter("m"), req.getParameter("t"), priority)) bw.write("Success"); else bw.write("Failed");
                } catch (NumberFormatException e) {
                    bw.write("Failed");
                }
            } else notAuthorized(resp, bw);
        } else if (cmd.equals("clearHistory")) {
            if (validSession) {
                if (ds.clear(Integer.parseInt(req.getParameter("t")))) bw.write("Success"); else bw.write("Failed");
            } else notAuthorized(resp, bw);
        } else if (cmd.equals("delHistRow")) {
            if (validSession) {
                if (ds.delTask(req.getParameter("m"), req.getParameter("t"), Integer.parseInt(req.getParameter("y")), req.getParameter("o"))) bw.write("Success"); else bw.write("Failed");
            } else notAuthorized(resp, bw);
        } else if (cmd.equals("rmLog")) {
            if (validSession) {
                String mid = req.getParameter("m");
                String tid = req.getParameter("t");
                String oid = req.getParameter("o");
                if (mid.equals("0") && tid.equals("0") && oid.equals("null")) {
                    bw.write("Failed: Can't delete server log file (sjq.log) while SageTV is running!");
                } else if (ds.clearLog(mid, tid, oid)) bw.write("Success"); else bw.write("Failed");
            } else notAuthorized(resp, bw);
        } else if (cmd.equals("qryMediaFile")) {
            if (validSession) {
                JSONArray jarr = new JSONArray();
                MediaFileAPI.List mediaList = Butler.SageApi.mediaFileAPI.GetMediaFiles(ds.getMediaMask());
                String qry = req.getParameter("q");
                int max = Integer.parseInt(req.getParameter("m"));
                for (MediaFileAPI.MediaFile mf : mediaList) {
                    if ((qry.matches("\\d+") && Integer.toString(mf.GetMediaFileID()).startsWith(qry)) || mf.GetMediaTitle().matches(".*" + Pattern.quote(qry) + ".*") || fileSegmentMatches(mf, qry)) {
                        JSONObject o = new JSONObject();
                        try {
                            o.put("value", mf.GetFileForSegment(0).getAbsolutePath());
                            String subtitle = null;
                            if (mf.GetMediaFileAiring() != null && mf.GetMediaFileAiring().GetShow() != null) subtitle = mf.GetMediaFileAiring().GetShow().GetShowEpisode();
                            String display;
                            if (subtitle != null && subtitle.length() > 0) display = mf.GetMediaTitle() + ": " + subtitle; else display = mf.GetMediaTitle();
                            o.put("display", mf.GetMediaFileID() + " - " + display);
                            jarr.put(o);
                            if (jarr.length() >= max) break;
                        } catch (JSONException e) {
                            e.printStackTrace(System.out);
                        }
                    }
                }
                bw.write(jarr.toString());
            } else notAuthorized(resp, bw);
        } else if (cmd.equals("debugMediaFile")) {
            if (validSession) {
                if (Butler.debugQueueLoader(req.getParameter("f"))) bw.write("Success"); else bw.write("Failed");
            } else notAuthorized(resp, bw);
        } else if (cmd.equals("killTask")) {
            if (validSession) {
                if (TaskQueue.getInstance().killTask(req.getParameter("m"), req.getParameter("t"), req.getParameter("o"))) bw.write("Success"); else bw.write("Failed");
            } else notAuthorized(resp, bw);
        } else if (cmd.equals("keepAlive")) {
            bw.write(Boolean.toString(!TaskQueue.getInstance().isTaskKilled(req.getParameter("m"), req.getParameter("t"), req.getParameter("o"))));
        }
        bw.close();
        DataStore.returnConnection(ds);
        LOG.info("Servlet POST request completed [" + (System.currentTimeMillis() - startTime) + "ms]");
        return;
    }
