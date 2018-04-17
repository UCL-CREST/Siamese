    private void exportAllSettings(HTTPurl urlData, OutputStream outStream) throws Exception {
        CaptureDeviceList devList = CaptureDeviceList.getInstance();
        if (devList.getActiveDeviceCount() > 0) {
            PageTemplate template = new PageTemplate(store.getProperty("path.template") + File.separator + "SettingsLoad.html");
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
        boolean authSettings = "true".equalsIgnoreCase(urlData.getParameter("AuthSettings"));
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
            serverProp.put("epg.showunlinked", store.getProperty("epg.showunlinked"));
            serverProp.put("path.theme", store.getProperty("path.theme"));
            serverProp.put("path.theme.epg", store.getProperty("path.theme.epg"));
            serverProp.put("capture.path", store.getProperty("capture.path"));
            serverProp.put("capture.averagedatarate", store.getProperty("capture.averagedatarate"));
            serverProp.put("capture.autoselectmethod", store.getProperty("capture.autoselectmethod"));
            serverProp.put("capture.minspacesoft", store.getProperty("capture.minspacesoft"));
            serverProp.put("capture.includecalculatedusage", store.getProperty("capture.includecalculatedusage"));
            serverProp.put("capture.deftype", store.getProperty("capture.deftype"));
            serverProp.put("capture.filename.patterns", store.getProperty("capture.filename.patterns"));
            serverProp.put("capture.path.details", store.getProperty("capture.path.details"));
            serverProp.put("capture.capturefailedtimeout", store.getProperty("capture.capturefailedtimeout"));
            serverProp.put("schedule.buffer.start", store.getProperty("schedule.buffer.start"));
            serverProp.put("schedule.buffer.end", store.getProperty("schedule.buffer.end"));
            serverProp.put("schedule.buffer.end.epg", store.getProperty("schedule.buffer.end.epg"));
            serverProp.put("schedule.wake.system", store.getProperty("schedule.wake.system"));
            serverProp.put("schedule.overlap", store.getProperty("schedule.overlap"));
            serverProp.put("schedule.noaachan", store.getProperty("schedule.noaachan"));
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
            serverProp.put("email.send.weeklyreport", store.getProperty("email.send.weeklyreport"));
            serverProp.put("email.send.capfinished", store.getProperty("email.send.capfinished"));
            serverProp.put("email.send.epgloaded", store.getProperty("email.send.epgloaded"));
            serverProp.put("email.send.onwarning", store.getProperty("email.send.onwarning"));
            serverProp.put("email.send.freespacelow", store.getProperty("email.send.freespacelow"));
            serverProp.put("email.send.serverstarted", store.getProperty("email.send.serverstarted"));
            serverProp.put("tasks.deftask", store.getProperty("tasks.deftask"));
            serverProp.put("tasks.pretask", store.getProperty("tasks.pretask"));
            serverProp.put("tasks.nodataerrortask", store.getProperty("tasks.nodataerrortask"));
            serverProp.put("tasks.starterrortask", store.getProperty("tasks.starterrortask"));
            serverProp.put("filebrowser.dirsattop", store.getProperty("filebrowser.dirsattop"));
            serverProp.put("filebrowser.masks", store.getProperty("filebrowser.masks"));
            serverProp.put("server.kbled", store.getProperty("server.kbled"));
            ByteArrayOutputStream serverpropBytes = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(serverpropBytes);
            oos.writeObject(serverProp);
            oos.close();
            out.putNextEntry(new ZipEntry("ServerProperties.sof"));
            out.write(serverpropBytes.toByteArray());
            out.closeEntry();
        }
        if (authSettings) {
            File authFile = new File(store.getProperty("path.data") + File.separator + "authentication.prop");
            if (authFile.exists()) {
                out.putNextEntry(new ZipEntry("authentication.prop"));
                FileInputStream is = new FileInputStream(authFile);
                byte[] buff = new byte[1024];
                int read = is.read(buff);
                while (read != -1) {
                    out.write(buff, 0, read);
                    read = is.read(buff);
                }
                out.closeEntry();
                is.close();
            }
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
