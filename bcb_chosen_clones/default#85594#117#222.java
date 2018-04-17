    private void showReportPage(HTTPurl urlData, OutputStream outStream) throws Exception {
        String download = urlData.getParameter("download");
        String id = urlData.getParameter("id");
        if (download == null || download.length() == 0) {
            PageTemplate template = new PageTemplate(store.getProperty("path.template") + File.separator + "submit.html");
            template.replaceAll("$schedule_id", id);
            outStream.write(template.getPageBytes());
            return;
        }
        ScheduleItem si = store.getScheduleItem(id);
        if (si == null) {
            String redirect = "HTTP/1.0 302 Moved Temporarily\n";
            redirect += "Location: /servlet/ScheduleDataRes\n\n";
            outStream.write(redirect.getBytes());
            return;
        }
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        ZipOutputStream out = new ZipOutputStream(bytesOut);
        Vector<String> logFiles = si.getLogFileNames();
        for (int x = 0; x < logFiles.size(); x++) {
            File log = new File(logFiles.get(x));
            if (log.exists()) {
                out.putNextEntry(new ZipEntry(log.getName()));
                byte[] data = new byte[1024];
                FileInputStream is = new FileInputStream(log);
                int read = is.read(data);
                while (read > -1) {
                    out.write(data, 0, read);
                    read = is.read(data);
                }
                out.closeEntry();
            }
        }
        out.putNextEntry(new ZipEntry("ItemLog.txt"));
        out.write(si.getLog().getBytes("UTF-8"));
        out.closeEntry();
        StringBuffer buff = new StringBuffer();
        buff.append("Name         : " + si.getName() + "\r\n");
        buff.append("Start        : " + si.getStart().toString() + "\r\n");
        buff.append("Stop         : " + si.getStop().toString() + "\r\n");
        buff.append("Duration     : " + si.getDuration() + "\r\n");
        buff.append("Channel      : " + si.getChannel() + "\r\n");
        buff.append("Path Index   : " + si.getCapturePathIndex() + "\r\n");
        buff.append("Capture Type : " + si.getCapType() + "\r\n");
        buff.append("Filename     : " + si.getFileName() + "\r\n");
        buff.append("File Pattern : " + si.getFilePattern() + "\r\n");
        buff.append("Keep For     : " + si.getKeepFor() + "\r\n");
        buff.append("Post Task    : " + si.getPostTask() + "\r\n");
        buff.append("State        : " + si.getState() + "\r\n");
        buff.append("Status       : " + si.getStatus() + "\r\n");
        buff.append("Type         : " + si.getType() + "\r\n");
        buff.append("\r\nWarnings:\r\n");
        Vector<String> warns = si.getWarnings();
        for (int x = 0; x < warns.size(); x++) {
            buff.append(warns.get(x) + "\r\n");
        }
        buff.append("\r\n");
        buff.append("Log Files:\r\n");
        Vector<String> logs = si.getLogFileNames();
        for (int x = 0; x < logs.size(); x++) {
            buff.append(logs.get(x) + "\r\n");
        }
        buff.append("\r\n");
        GuideItem item = si.getCreatedFrom();
        if (item != null) {
            buff.append("Created From:\r\n");
            buff.append("Name     : " + item.getName() + "\r\n");
            buff.append("Start    : " + item.getStart().toString() + "\r\n");
            buff.append("Stop     : " + item.getStop().toString() + "\r\n");
            buff.append("Duration : " + item.getDuration() + "\r\n");
            buff.append("\r\n");
        }
        HashMap<Date, SignalStatistic> signal = si.getSignalStatistics();
        if (signal.size() > 0) {
            buff.append("Signal Statistics: (Locked, Strength, Quality)\r\n");
            Date[] keys = signal.keySet().toArray(new Date[0]);
            for (int x = 0; x < signal.size(); x++) {
                SignalStatistic stat = signal.get(keys[x]);
                buff.append(keys[x].toString() + " - " + stat.getLocked() + ", " + stat.getStrength() + ", " + stat.getQuality() + "\r\n");
            }
            buff.append("\r\n");
        }
        out.putNextEntry(new ZipEntry("ItemDetails.txt"));
        out.write(buff.toString().getBytes("UTF-8"));
        out.closeEntry();
        out.flush();
        out.close();
        StringBuffer header = new StringBuffer();
        header.append("HTTP/1.1 200 OK\n");
        header.append("Content-Type: application/zip\n");
        header.append("Content-Length: " + bytesOut.size() + "\n");
        header.append("Content-Disposition: attachment; filename=\"ScheduleErrorReport.zip\"\n");
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
        return;
    }
