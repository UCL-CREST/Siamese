    private void exportAllSettings(HTTPurl urlData, OutputStream outStream) throws Exception {
        CaptureDeviceList devList = CaptureDeviceList.getInstance();
        if (devList.getActiveDeviceCount() > 0) {
            PageTemplate template = new PageTemplate(store.getProperty("path.template").replace('\\', File.separatorChar) + File.separator + "SettingsLoad.html");
            StringBuffer buff = new StringBuffer();
            buff.append("<tr><td><img border=0 src='/images/stop.png' align='absmiddle' width='24' height='24'></td><td>Can not save settings while a capture is in progress.</td></tr>");
            template.replaceAll("$result", buff.toString());
            outStream.write(template.getPageBytes());
            return;
        }
        boolean matchList = "true".equalsIgnoreCase(urlData.getParameter("MatchList"));
        boolean autoAdd = "true".equalsIgnoreCase(urlData.getParameter("AutoAdd"));
        boolean channelMapping = "true".equalsIgnoreCase(urlData.getParameter("ChannelMapping"));
        boolean deviceSelection = "true".equalsIgnoreCase(urlData.getParameter("DeviceSelection"));
        boolean agentMapping = "true".equalsIgnoreCase(urlData.getParameter("AgentMapping"));
        boolean channels = "true".equalsIgnoreCase(urlData.getParameter("Channels"));
        boolean tasks = "true".equalsIgnoreCase(urlData.getParameter("Tasks"));
        boolean systemProp = "true".equalsIgnoreCase(urlData.getParameter("SystemProp"));
        boolean schedules = "true".equalsIgnoreCase(urlData.getParameter("Schedules"));
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        ZipOutputStream out = new ZipOutputStream(bytesOut);
        out.setComment("TV Scheduler Pro Settings file (Version: 1.0)");
        if (channels) {
            out.putNextEntry(new ZipEntry("Channels.xml"));
            StringBuffer channelData = new StringBuffer();
            store.saveChannels(channelData);
            byte[] channelBytes = channelData.toString().getBytes("UTF-8");
            out.write(channelBytes);
            out.closeEntry();
        }
        if (matchList) {
            out.putNextEntry(new ZipEntry("MatchList.xml"));
            StringBuffer matchData = new StringBuffer();
            store.saveMatchList(matchData);
            byte[] matchBytes = matchData.toString().getBytes("UTF-8");
            out.write(matchBytes);
            out.closeEntry();
        }
        if (autoAdd) {
            out.putNextEntry(new ZipEntry("EpgAutoAdd.xml"));
            StringBuffer addData = new StringBuffer();
            store.saveEpgAutoList(addData);
            byte[] addBytes = addData.toString().getBytes("UTF-8");
            out.write(addBytes);
            out.closeEntry();
        }
        if (tasks) {
            out.putNextEntry(new ZipEntry("Tasks.xml"));
            StringBuffer taskData = new StringBuffer();
            store.saveTaskList(taskData);
            byte[] taskBytes = taskData.toString().getBytes("UTF-8");
            out.write(taskBytes);
            out.closeEntry();
        }
        if (channelMapping) {
            GuideStore guideStore = GuideStore.getInstance();
            out.putNextEntry(new ZipEntry("ChannelMap.sof"));
            ByteArrayOutputStream chanMapBytes = new ByteArrayOutputStream();
            guideStore.saveChannelMap(chanMapBytes);
            out.write(chanMapBytes.toByteArray());
            out.closeEntry();
        }
        if (deviceSelection) {
            out.putNextEntry(new ZipEntry("CaptureDevices.sof"));
            ByteArrayOutputStream deviceBytes = new ByteArrayOutputStream();
            devList.saveDeviceList(deviceBytes);
            out.write(deviceBytes.toByteArray());
            out.closeEntry();
        }
        if (agentMapping) {
            out.putNextEntry(new ZipEntry("AgentMap.sof"));
            ByteArrayOutputStream agentMapBytes = new ByteArrayOutputStream();
            store.saveAgentToThemeMap(agentMapBytes);
            out.write(agentMapBytes.toByteArray());
            out.closeEntry();
        }
        if (schedules) {
            out.putNextEntry(new ZipEntry("Times.sof"));
            ByteArrayOutputStream timesBytes = new ByteArrayOutputStream();
            store.saveSchedule(timesBytes);
            out.write(timesBytes.toByteArray());
            out.closeEntry();
        }
        if (systemProp) {
            HashMap<String, String> serverProp = new HashMap<String, String>();
            serverProp.put("Capture.path", store.getProperty("Capture.path"));
            serverProp.put("Capture.AverageDataRate", store.getProperty("Capture.AverageDataRate"));
            serverProp.put("Capture.AutoSelectMethod", store.getProperty("Capture.AutoSelectMethod"));
            serverProp.put("Capture.minSpace", store.getProperty("Capture.minSpace"));
            serverProp.put("Capture.IncludeCalculatedUsage", store.getProperty("Capture.IncludeCalculatedUsage"));
            serverProp.put("Capture.deftype", store.getProperty("Capture.deftype"));
            serverProp.put("Capture.filename.patterns", store.getProperty("Capture.filename.patterns"));
            serverProp.put("Capture.path.details", store.getProperty("Capture.path.details"));
            serverProp.put("Capture.CaptureFailedTimeout", store.getProperty("Capture.CaptureFailedTimeout"));
            serverProp.put("Schedule.buffer.start", store.getProperty("Schedule.buffer.start"));
            serverProp.put("Schedule.buffer.end", store.getProperty("Schedule.buffer.end"));
            serverProp.put("Schedule.buffer.end.epg", store.getProperty("Schedule.buffer.end.epg"));
            serverProp.put("Schedule.wake.system", store.getProperty("Schedule.wake.system"));
            serverProp.put("sch.autodel.action", store.getProperty("sch.autodel.action"));
            serverProp.put("sch.autodel.time", store.getProperty("sch.autodel.time"));
            serverProp.put("guide.source.http.pwd", store.getProperty("guide.source.http.pwd"));
            serverProp.put("guide.source.xml.channelList", store.getProperty("guide.source.xml.channelList"));
            serverProp.put("guide.source.type", store.getProperty("guide.source.type"));
            serverProp.put("guide.source.http", store.getProperty("guide.source.http"));
            serverProp.put("guide.source.file", store.getProperty("guide.source.file"));
            serverProp.put("guide.action.name", store.getProperty("guide.action.name"));
            serverProp.put("guide.source.http.usr", store.getProperty("guide.source.http.usr"));
            serverProp.put("guide.source.schedule", store.getProperty("guide.source.schedule"));
            serverProp.put("guide.warn.overlap", store.getProperty("guide.warn.overlap"));
            serverProp.put("proxy.server", store.getProperty("proxy.server"));
            serverProp.put("proxy.port", store.getProperty("proxy.port"));
            serverProp.put("proxy.server.usr", store.getProperty("proxy.server.usr"));
            serverProp.put("proxy.server.pwd", store.getProperty("proxy.server.pwd"));
            serverProp.put("email.server", store.getProperty("email.server"));
            serverProp.put("email.from.name", store.getProperty("email.from.name"));
            serverProp.put("email.to", store.getProperty("email.to"));
            serverProp.put("email.from", store.getProperty("email.from"));
            serverProp.put("Tasks.DefTask", store.getProperty("Tasks.DefTask"));
            serverProp.put("Tasks.PreTask", store.getProperty("Tasks.PreTask"));
            serverProp.put("Tasks.NoDataErrorTask", store.getProperty("Tasks.NoDataErrorTask"));
            serverProp.put("Tasks.StartErrorTask", store.getProperty("Tasks.StartErrorTask"));
            serverProp.put("filebrowser.DirsAtTop", store.getProperty("filebrowser.DirsAtTop"));
            serverProp.put("filebrowser.masks", store.getProperty("filebrowser.masks"));
            serverProp.put("server.kbLED", store.getProperty("server.kbLED"));
            ByteArrayOutputStream serverpropBytes = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(serverpropBytes);
            oos.writeObject(serverProp);
            oos.close();
            out.putNextEntry(new ZipEntry("ServerProperties.sof"));
            out.write(serverpropBytes.toByteArray());
            out.closeEntry();
        }
        out.flush();
        out.close();
        StringBuffer header = new StringBuffer();
        header.append("HTTP/1.1 200 OK\n");
        header.append("Content-Type: application/zip\n");
        header.append("Content-Length: " + bytesOut.size() + "\n");
        header.append("Content-Disposition: attachment; filename=\"TV Scheduler Pro Settings.zip\"\n");
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss 'GMT'", new Locale("En", "Us", "Unix"));
        header.append("Last-Modified: " + df.format(new Date()) + "\n");
        header.append("\n");
        outStream.write(header.toString().getBytes());
        ByteArrayInputStream zipStream = new ByteArrayInputStream(bytesOut.toByteArray());
        byte[] bytes = new byte[4096];
        int read = zipStream.read(bytes);
        while (read > -1) {
            outStream.write(bytes, 0, read);
            outStream.flush();
            read = zipStream.read(bytes);
        }
    }
