    private void initGui() {
        d = new debug(debug.LEVEL.DEBUG);
        w = new MainWindow();
        w.setTitle(defWinTitle);
        w.setLocationByPlatform(true);
        w.Info.setText(defWinTitle);
        w.RHost.setText(defRHost);
        w.RPort.setText(Integer.toString(defRPort));
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
                    w.Mac.addItem(ss);
                }
            }
        } catch (Exception e) {
            w.Mac.addItem(e.toString());
        }
        w.LHost.setText(defLHost);
        w.LTPort.setText(Integer.toString(defLTPort));
        w.RHost.setText(defRHost);
        w.RPort.setText(Integer.toString(defRPort));
        w.LUPort.setText(Integer.toString(defLUPort));
        w.RUPort.setText("");
        w.RUPort.setText("");
        w.Message.setText("");
        setCode(0, ' ');
    }
