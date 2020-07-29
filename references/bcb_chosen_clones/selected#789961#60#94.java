    private void initGui() {
        d = new debug(debug.LEVEL.DEBUG);
        swin = new SessionWindow();
        swin.setTitle(defSWinTitle);
        swin.setLocationByPlatform(true);
        swin.Info.setText(defSWinTitle);
        swin.RHost.setText(defRHost);
        swin.RPort.setText(Integer.toString(defRPort));
        try {
            java.util.Enumeration eth = java.net.NetworkInterface.getNetworkInterfaces();
            while (eth.hasMoreElements()) {
                java.net.NetworkInterface eth0 = (java.net.NetworkInterface) eth.nextElement();
                byte mac[] = eth0.getHardwareAddress();
                if (mac != null) {
                    String ss = "";
                    for (int i = 0; i < mac.length; i++) {
                        String sss = String.format("%02X", mac[i]);
                        if (i == 0) {
                            ss = sss;
                        } else {
                            ss += ((i % 2 == 0) ? " " : "") + sss;
                        }
                    }
                    swin.Mac.addItem(ss);
                }
            }
        } catch (Exception e) {
            swin.Mac.addItem(e.toString());
        }
        swin.LHost.setText(defLHost);
        swin.LPort.setText(Integer.toString(defLPort));
        swin.RHost.setText(defRHost);
        swin.RPort.setText(Integer.toString(defRPort));
        setCode(0, ' ');
    }
