    private byte[] showScanResult(HTTPurl urlData) throws Exception {
        int freq = 0;
        int band = 0;
        try {
            freq = Integer.parseInt(urlData.getParameter("freq"));
            band = Integer.parseInt(urlData.getParameter("band"));
        } catch (Exception e) {
            throw new Exception("Freq or Band not valid: " + e.toString());
        }
        PageTemplate template = new PageTemplate(store.getProperty("path.template") + File.separator + "channel-scanresult.html");
        CaptureDeviceList devList = CaptureDeviceList.getInstance();
        if (devList.getActiveDeviceCount() > 0) {
            template.replaceAll("$scanresult", "Can not scan channels while captures are active!");
            return template.getPageBytes();
        }
        if (devList.getDeviceCount() == 0) {
            template.replaceAll("$scanresult", "No Devices Available!");
            return template.getPageBytes();
        }
        String name = urlData.getParameter("name");
        StringBuffer buff = new StringBuffer(1024);
        buff.append("<form action='/servlet/" + urlData.getServletClass() + "' method='POST' accept-charset=\"UTF-8\">\n");
        buff.append("<input type='hidden' name='action' value='05'>\n");
        buff.append("<table class='channelScanResult'>\n");
        buff.append("<tr class='scanChannelHeading'>");
        buff.append("<td class='scanChannelHeadingDataName'>" + name + "</td>");
        buff.append("<td class='scanChannelHeadingData'>Program</td>");
        buff.append("<td class='scanChannelHeadingData'>Video</td>");
        buff.append("<td class='scanChannelHeadingData'>Audio</td>");
        buff.append("<td class='scanChannelHeadingData'>Add</td>");
        buff.append("</tr>\n");
        boolean testMode = "1".equals(store.getProperty("tools.testmode"));
        CaptureDevice cap = (CaptureDevice) devList.getDevice(0);
        Runtime runner = Runtime.getRuntime();
        String[] com = null;
        if (testMode == true) {
            String scanCommand = "win32/scan.exe -test";
            System.out.println("Running channel scan command: " + scanCommand);
            com = new String[2];
            com[0] = "win32/scan.exe";
            com[1] = "-test";
        } else {
            String scanCommand = "win32/scan.exe " + freq + " " + band + " \"" + cap.getID() + "\"";
            System.out.println("Running channel scan command: " + scanCommand);
            com = new String[4];
            com[0] = "win32/scan.exe";
            com[1] = new Integer(freq).toString();
            com[2] = new Integer(band).toString();
            com[3] = "\"" + cap.getID() + "\"";
        }
        Process scan = runner.exec(com);
        ScanResult result = new ScanResult(freq, band);
        result.readInput(scan.getInputStream());
        result.parseXML();
        int channelCount = 0;
        if (result.getResult().size() > 0) {
            Vector<Channel> scanResult = result.getResult();
            Channel chData = null;
            for (int x = 0; x < scanResult.size(); x++) {
                chData = (Channel) scanResult.get(x);
                if (chData != null) {
                    buff.append("<tr class='scanChannelResult'>");
                    buff.append("<td>\n<input type='text' name='name" + channelCount + "' value='" + checkName(chData.getName()) + "'>\n");
                    buff.append("<input type='hidden' name='freq" + channelCount + "' value='" + chData.getFrequency() + "'>\n");
                    buff.append("<input type='hidden' name='band" + channelCount + "' value='" + chData.getBandWidth() + "'>\n");
                    buff.append("</td>\n\n");
                    buff.append("<td align='center'>" + chData.getProgramID());
                    buff.append("<input type='hidden' name='programid" + channelCount + "' value='" + chData.getProgramID() + "'>\n");
                    buff.append("</td>\n");
                    buff.append("<td align='center'>");
                    int count = getStreamTypeCount(chData, Channel.TYPE_VIDEO);
                    if (count == 0) {
                        buff.append("-1");
                        buff.append("<input type='hidden' name='videoid" + channelCount + "' value='-1'>\n");
                    }
                    if (count == 1) {
                        int[] data = getFirstOfType(chData, Channel.TYPE_VIDEO);
                        buff.append(data[0]);
                        buff.append("<input type='hidden' name='videoid" + channelCount + "' value='" + data[0] + "'>\n");
                    } else if (count > 1) {
                        buff.append("<SELECT NAME='videoid" + channelCount + "'>\n");
                        for (int st = 0; st < chData.getStreams().size(); st++) {
                            int[] streamData = (int[]) chData.getStreams().get(st);
                            if (streamData[1] == Channel.TYPE_VIDEO) {
                                buff.append("<OPTION value=\"" + streamData[0] + "\">");
                                buff.append(streamData[0]);
                                buff.append("</OPTION>\n");
                            }
                        }
                        buff.append("</SELECT>");
                    }
                    buff.append("</td>\n");
                    buff.append("<td align='center'>");
                    count = getStreamTypeCount(chData, Channel.TYPE_AUDIO_AC3);
                    count += getStreamTypeCount(chData, Channel.TYPE_AUDIO_MPG);
                    if (count == 0) {
                        buff.append("-1");
                        buff.append("<input type='hidden' name='audioid" + channelCount + "' value='-1'>\n");
                    }
                    if (count == 1) {
                        int[] data = getFirstOfType(chData, Channel.TYPE_AUDIO_AC3);
                        if (data == null) data = getFirstOfType(chData, Channel.TYPE_AUDIO_MPG);
                        buff.append(data[0]);
                        if (data[1] == Channel.TYPE_AUDIO_AC3) buff.append(" AC3"); else buff.append(" MPG");
                        buff.append("<input type='hidden' name='audioid" + channelCount + "' value='" + data[0] + ":" + data[1] + "'>\n");
                    } else if (count > 1) {
                        buff.append("<SELECT NAME='audioid" + channelCount + "'>\n");
                        for (int st = 0; st < chData.getStreams().size(); st++) {
                            int[] streamData = (int[]) chData.getStreams().get(st);
                            if (streamData[1] == Channel.TYPE_AUDIO_MPG || streamData[1] == Channel.TYPE_AUDIO_AC3) {
                                buff.append("<OPTION value=\"" + streamData[0] + ":" + streamData[1] + "\">");
                                buff.append(streamData[0]);
                                if (streamData[1] == Channel.TYPE_AUDIO_AC3) buff.append(" AC3"); else buff.append(" MPG");
                                buff.append("</OPTION>\n");
                            }
                        }
                        buff.append("</SELECT>");
                    }
                    buff.append("</td>\n");
                    buff.append("<td align='center'><input type='checkbox' name='add" + channelCount + "' value='add'></td>\n");
                    buff.append("</tr>\n\n");
                    channelCount++;
                } else {
                    break;
                }
            }
        } else {
            buff.append("<tr class='scanChannelResult'>");
            buff.append("<td>No Programs Found</td>\n");
            buff.append("<td align='center'>N/A</td>\n");
            buff.append("<td align='center'>N/A</td>\n");
            buff.append("<td align='center'>N/A</td>\n");
            buff.append("<td align='center'>N/A</td>\n");
            buff.append("</tr>\n\n");
        }
        buff.append("</table><br>\n");
        if (channelCount > 0) buff.append("<input type='submit' value='Add Selected'>\n");
        buff.append("</form>\n");
        template.replaceAll("$scanresult", buff.toString());
        return template.getPageBytes();
    }
