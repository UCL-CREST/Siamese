    private void readData(BufferedReader in) {
        try {
            String data;
            while ((data = in.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(data);
                if (st.hasMoreTokens()) {
                    String command = st.nextToken();
                    if (command.equals("host")) {
                        if (st.hasMoreTokens()) {
                            conf_host = st.nextToken();
                        } else {
                            JOptionPane.showMessageDialog(panel, "Config file has a bad " + "'host' entry!");
                        }
                    } else if (command.equals("configdb")) {
                        if (st.hasMoreTokens()) {
                            conf_configdb = st.nextToken();
                        } else {
                            JOptionPane.showMessageDialog(panel, "Config file has a bad " + "'configdb' entry!");
                        }
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(panel, "Couldn't load " + "config file :\n" + e);
        }
    }
