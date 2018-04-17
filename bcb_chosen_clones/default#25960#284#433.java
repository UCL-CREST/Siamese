    private void scanAll(HTTPurl urlData, OutputStream outStream) throws Exception {
        int country = 0;
        int region = 0;
        try {
            country = Integer.parseInt(urlData.getParameter("country"));
            region = Integer.parseInt(urlData.getParameter("region"));
        } catch (Exception e) {
            throw new Exception("country or region code not valid: " + e.toString());
        }
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
        String out = "<form action=\"/servlet/" + urlData.getServletClass() + "\" method=\"POST\" accept-charset=\"UTF-8\">\n" + "<input type=\"hidden\" name=\"action\" value='05'>\n";
        outStream.write(out.getBytes());
        outStream.flush();
        ChannelList list = new ChannelList(store.getProperty("path.data") + File.separator + "stationdata.list");
        Channel[] channelList = list.getStations(country, region);
        list.close();
        try {
            int channelCount = 0;
            Channel ch = null;
            for (int x = 0; x < channelList.length; x++) {
                int channelsFound = 0;
                ch = channelList[x];
                if (ch != null) {
                    StringBuffer buff = new StringBuffer(2048);
                    buff.append("<table class='channelScanResult'>\n");
                    buff.append("<tr class=\"scanChannelHeading\">\n");
                    buff.append("<td class=\"scanChannelHeadingDataName\">" + ch.getName() + "</td>\n");
                    buff.append("<td class=\"scanChannelHeadingData\">Program</td>\n");
                    buff.append("<td class=\"scanChannelHeadingData\">Video</td>\n");
                    buff.append("<td class=\"scanChannelHeadingData\">Audio</td>\n");
                    buff.append("<td class=\"scanChannelHeadingData\">Add</td>\n");
                    buff.append("</tr>\n");
                    if (x > 0) Thread.sleep(5000);
                    CaptureDevice cap = (CaptureDevice) devList.getDevice(0);
                    String scanCommand = "scan.exe " + ch.getFrequency() + " " + ch.getBandWidth() + " \"" + cap.getID() + "\"";
                    System.out.println("Running channel scan command: " + scanCommand);
                    Runtime runner = Runtime.getRuntime();
                    String[] com = new String[4];
                    com[0] = "scan.exe";
                    com[1] = new Integer(ch.getFrequency()).toString();
                    com[2] = new Integer(ch.getBandWidth()).toString();
                    com[3] = "\"" + cap.getID() + "\"";
                    Process scan = runner.exec(com);
                    ScanResult result = new ScanResult(ch.getFrequency(), ch.getBandWidth());
                    result.readInput(scan.getInputStream());
                    result.parseXML();
                    Vector scanResult = result.getResult();
                    Channel chData = null;
                    for (int y = 0; y < scanResult.size(); y++) {
                        chData = (Channel) scanResult.get(y);
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
                            channelsFound++;
                        } else {
                            break;
                        }
                    }
                    if (channelsFound == 0) {
                        buff.append("<tr class='scanChannelResult'>");
                        buff.append("<td>No Programs Found</td>\n");
                        buff.append("<td align='center'>N/A</td>\n");
                        buff.append("<td align='center'>N/A</td>\n");
                        buff.append("<td align='center'>N/A</td>\n");
                        buff.append("<td align='center'>N/A</td>\n");
                        buff.append("</tr>\n\n");
                    }
                    buff.append("</table><br>\n");
                    outStream.write(buff.toString().getBytes());
                    outStream.flush();
                    outStream.write("\n\n\n\n\n           \n\n\n\n\n\n".getBytes());
                    outStream.flush();
                }
            }
            if (channelCount > 0) out = "<input type=\"submit\" value=\"Add Selected\"></form></body></html><br><br>\n";
            outStream.write(out.getBytes());
            outStream.flush();
        } catch (Exception e) {
            throw e;
        }
    }
