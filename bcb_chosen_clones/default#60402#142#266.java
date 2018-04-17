    private void rescanAll(HTTPurl urlData, OutputStream outStream) throws Exception {
        PageTemplate template = new PageTemplate(store.getProperty("path.template") + File.separator + "channelrescan.html");
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
        HashMap<String, Channel> channelMap = store.getChannels();
        Vector<Channel> stationList = new Vector<Channel>();
        HashMap<String, String> scanResult = new HashMap<String, String>();
        String[] keys = (String[]) channelMap.keySet().toArray(new String[0]);
        int numUpdated = 0;
        String resultText = "";
        try {
            for (int x = 0; x < keys.length; x++) {
                Channel ch = (Channel) channelMap.get(keys[x]);
                boolean found = false;
                for (int y = 0; y < stationList.size(); y++) {
                    Channel stCh = (Channel) stationList.get(y);
                    if (stCh.getFrequency() == ch.getFrequency() && stCh.getBandWidth() == ch.getBandWidth()) {
                        found = true;
                    }
                }
                if (found == false && ch.getProgramID() != 0) {
                    stationList.add(new Channel(ch.getName(), ch.getFrequency(), ch.getBandWidth(), ch.getProgramID(), 0, 0));
                }
            }
            for (int x = 0; x < stationList.size(); x++) {
                Channel ch = (Channel) stationList.get(x);
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
                Vector channels = result.getResult();
                for (int y = 0; y < keys.length; y++) {
                    Channel storedChannel = (Channel) channelMap.get(keys[y]);
                    for (int q = 0; q < channels.size(); q++) {
                        Channel scannedChannel = (Channel) channels.get(q);
                        if (storedChannel.getFrequency() == scannedChannel.getFrequency() && storedChannel.getBandWidth() == scannedChannel.getBandWidth() && storedChannel.getProgramID() == scannedChannel.getProgramID()) {
                            Vector streams = scannedChannel.getStreams();
                            int videoCheckFlag = 0;
                            int oldVideoPid = storedChannel.getVideoPid();
                            for (int stID = 0; stID < streams.size(); stID++) {
                                int[] streamData = (int[]) streams.get(stID);
                                if (storedChannel.getVideoPid() == streamData[0] && streamData[1] == Channel.TYPE_VIDEO) {
                                    videoCheckFlag = 1;
                                }
                            }
                            if (videoCheckFlag == 0) {
                                for (int stID = 0; stID < streams.size(); stID++) {
                                    int[] streamData = (int[]) streams.get(stID);
                                    if (streamData[1] == Channel.TYPE_VIDEO) {
                                        storedChannel.setVideoPid(streamData[0]);
                                        videoCheckFlag = 2;
                                    }
                                }
                            }
                            if (videoCheckFlag == 0) {
                                resultText = "Video pid (" + storedChannel.getVideoPid() + ") was " + "not found and a replacment could not be located.<br>";
                            } else if (videoCheckFlag == 1) {
                                resultText = "Video pid (" + storedChannel.getVideoPid() + ") has " + "not changed.<br>";
                            } else if (videoCheckFlag == 2) {
                                resultText = "Video pid (" + oldVideoPid + ") has " + "changed and was replaced with (" + storedChannel.getVideoPid() + ")<br>";
                            }
                            scanResult.put(storedChannel.getName(), resultText);
                            int audioCheckFlag = 0;
                            int oldAudioPid = storedChannel.getAudioPid();
                            for (int stID = 0; stID < streams.size(); stID++) {
                                int[] streamData = (int[]) streams.get(stID);
                                if (storedChannel.getAudioPid() == streamData[0] && streamData[1] == storedChannel.getAudioType()) {
                                    audioCheckFlag = 1;
                                }
                            }
                            if (audioCheckFlag == 0) {
                                for (int stID = 0; stID < streams.size(); stID++) {
                                    int[] streamData = (int[]) streams.get(stID);
                                    if (streamData[1] == storedChannel.getAudioType()) {
                                        storedChannel.setAudioPid(streamData[0]);
                                        audioCheckFlag = 2;
                                    }
                                }
                            }
                            if (audioCheckFlag == 0) {
                                resultText = "Audio pid (" + storedChannel.getAudioPid() + ") was " + "not found and a replacment could not be located.<br>";
                            } else if (audioCheckFlag == 1) {
                                resultText = "Audio pid (" + storedChannel.getAudioPid() + ") has " + "not changed.<br>";
                            } else if (audioCheckFlag == 2) {
                                resultText = "Audio pid (" + oldAudioPid + ") has " + "changed and was replaced with (" + storedChannel.getAudioPid() + ")<br>";
                            }
                            String errTXT = (String) scanResult.get(storedChannel.getName());
                            errTXT += resultText;
                            scanResult.put(storedChannel.getName(), errTXT);
                            if (audioCheckFlag == 2 || videoCheckFlag == 2) numUpdated++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        if (numUpdated > 0) store.saveChannels(null);
        outStream.write("<br><span class='areaTitle'>Channel Rescan Results</span><br>\n".getBytes());
        outStream.write("<table class='rescanResult'>\n".getBytes());
        keys = (String[]) scanResult.keySet().toArray(new String[0]);
        for (int x = 0; x < keys.length; x++) {
            String out = "<tr><td class='rescanName'>" + keys[x] + "</td><td class='rescanResult'>" + (String) scanResult.get(keys[x]) + "</td></tr>\n";
            outStream.write(out.getBytes());
        }
        outStream.write("</table>\n".getBytes());
        outStream.write("</body></html>\n".getBytes());
    }
