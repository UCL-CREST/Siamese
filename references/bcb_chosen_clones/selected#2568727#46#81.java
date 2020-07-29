    public static JFrame launchClient(int partId, String deviceName) throws java.io.IOException {
        String ior = Variables.controller.getDeviceIor(deviceName);
        JFrame frame = null;
        if (ior.equals("")) {
            doors.util.Swing.errorMessage("Couldn't get IOR for " + deviceName, "Error Launching Client");
            return null;
        }
        int invokationType = INVOKE_JAVA;
        if (invokationType == INVOKE_SHELL) {
            String command = "/bin/sh /home/adam/work/doors/bin/midifileplayer-client.sh " + ior;
            Runtime.getRuntime().exec(command);
        } else if (invokationType == INVOKE_JAVA) {
            org.omg.CORBA.Object obj = Variables.orb.string_to_object(ior);
            doors.Device deviceObj = doors.DeviceHelper.narrow(obj);
            String className = "doors.midifileplayer.client.MidiFilePlayerClient";
            IDeviceClient client = null;
            try {
                Class deviceClass = Class.forName(className);
                Constructor constructor = deviceClass.getConstructor(new Class[] {});
                client = (IDeviceClient) constructor.newInstance(new Object[] {});
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.toString());
            }
            frame = new JFrame(deviceName);
            JPanel panel = client.getPanel(partId, deviceObj);
            frame.setContentPane(panel);
            frame.setVisible(true);
            frame.setSize(panel.getPreferredSize());
            frame.setLocation(cascadeX + cascadeWidth * cascadeCount, cascadeY + cascadeWidth * cascadeCount);
            cascadeCount++;
            if (cascadeCount >= cascadeMax) cascadeCount = 0;
        } else {
            doors.util.Swing.errorMessage("Cannot invoke " + deviceName + " client - unknown invokationType in properties file", "Error launching " + deviceName);
        }
        return frame;
    }
