    private void scanAllLinux(HTTPurl urlData, OutputStream outStream) throws Exception {
        String tunerType;
        String region;
        tunerType = urlData.getParameter("type");
        region = urlData.getParameter("region");
        PageTemplate template = new PageTemplate(store.getProperty("path.template").replace('\\', File.separatorChar) + File.separator + "channelscan.html");
        outStream.write(template.getPageBytes());
        CaptureDeviceList devList = CaptureDeviceList.getInstance();
        if (devList.getActiveDeviceCount() > 0) {
            outStream.write("Can not scan channels, Captures Running!".getBytes());
            return;
        }
        if (devList.getDeviceCount() == 0) {
            outStream.write("No Devices Available!".getBytes());
            return;
        }
        String out = "<form action=\"/servlet/" + urlData.getServletClass() + "\" method=\"POST\" accept-charset=\"UTF-8\">\n" + "<input type=\"hidden\" name=\"action\" value='17'>\n";
        outStream.write(out.getBytes());
        outStream.flush();
        CaptureDevice cap = (CaptureDevice) devList.getDevice(0);
        String scanCommand = "scan " + store.getProperty("linux.tunedata") + File.separator + tunerType + File.separator + region;
        System.out.println("Running channel scan command: " + scanCommand);
        try {
            Runtime runner = Runtime.getRuntime();
            Process scan = runner.exec(scanCommand);
            InputStreamReader isr = new InputStreamReader(scan.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String strLine;
            int channelCount = 0;
            while ((strLine = br.readLine()) != null) {
                StringBuffer buff = new StringBuffer(2048);
                buff.append("<table class='channelScanResult'>\n");
                buff.append("<tr class='scanChannelResult'>\n");
                buff.append("<td>\n<input type='text' name='name" + channelCount + "' value='" + strLine.split(":")[0] + "'>\n");
                buff.append("<td align='center'><input type='checkbox' name='add" + channelCount + "' value='add'></td>\n");
                buff.append("</tr>\n");
                buff.append("</table>\n");
                outStream.write(buff.toString().getBytes());
                outStream.flush();
                outStream.flush();
                channelCount++;
            }
            outStream.write("<input type=\"submit\" value=\"Add Selected\"></form></body></html><br><br>\n".getBytes());
            outStream.flush();
        } catch (Exception e) {
            throw e;
        }
    }
