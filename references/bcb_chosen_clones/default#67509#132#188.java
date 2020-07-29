        public void run() {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                ChannelMap cm = new ChannelMap();
                for (int i = 0; i < picm.NumberOfChannels(); i++) {
                    cm.Add(picm.GetName(i));
                }
                String[] folder = picm.GetFolderList();
                for (int i = 0; i < folder.length; i++) {
                    cm.AddFolder(folder[i]);
                }
                sink.Request(cm, picm.GetRequestStart(), picm.GetRequestDuration(), picm.GetRequestReference());
                cm = sink.Fetch(timeout);
                if (cm.GetIfFetchTimedOut()) {
                    System.err.println("Signature Data Fetch Timed Out!");
                    picm.Clear();
                } else {
                    md.reset();
                    folder = cm.GetFolderList();
                    for (int i = 0; i < folder.length; i++) picm.AddFolder(folder[i]);
                    int sigIdx = -1;
                    for (int i = 0; i < cm.NumberOfChannels(); i++) {
                        String chan = cm.GetName(i);
                        if (chan.endsWith("/_signature")) {
                            sigIdx = i;
                            continue;
                        }
                        int idx = picm.GetIndex(chan);
                        if (idx == -1) idx = picm.Add(chan);
                        picm.PutTimeRef(cm, i);
                        picm.PutDataRef(idx, cm, i);
                        md.update(cm.GetData(i));
                        md.update((new Double(cm.GetTimeStart(i))).toString().getBytes());
                    }
                    if (cm.NumberOfChannels() > 0) {
                        byte[] amd = md.digest(signature.getBytes());
                        if (sigIdx >= 0) {
                            if (MessageDigest.isEqual(amd, cm.GetDataAsByteArray(sigIdx)[0])) {
                                System.err.println(pluginName + ": signature matched for: " + cm.GetName(0));
                            } else {
                                System.err.println(pluginName + ": failed signature test, sending null response");
                                picm.Clear();
                            }
                        } else {
                            System.err.println(pluginName + ": _signature attached for: " + cm.GetName(0));
                            int idx = picm.Add("_signature");
                            picm.PutTime(0., 0.);
                            picm.PutDataAsByteArray(idx, amd);
                        }
                    }
                }
                plugin.Flush(picm);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (threadStack.size() < 4) threadStack.push(this); else sink.CloseRBNBConnection();
        }
