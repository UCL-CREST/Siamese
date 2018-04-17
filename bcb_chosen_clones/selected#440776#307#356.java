    private static Object[] getNetworkConnections() {
        ArrayList<String> netConnections = new ArrayList<String>();
        netConnections.add("This machine only /127.0.0.1");
        netConnections.add("Any Connected Network (including Internet)");
        try {
            InetAddress[] networks = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
            int unknownConnections = 0;
            for (InetAddress network : networks) {
                if (network.isLoopbackAddress()) continue;
                String name = null;
                try {
                    NetworkInterface netAdapter = NetworkInterface.getByInetAddress(network);
                    if (netAdapter != null) {
                        name = netAdapter.getDisplayName();
                        if (name == null) {
                            byte[] mac = netAdapter.getHardwareAddress();
                            if (mac != null) {
                                StringBuilder macAddress = new StringBuilder(21);
                                macAddress.append("MAC: ");
                                for (int b : mac) {
                                    if (b < 0) b = 255 + b;
                                    if (b < 16) macAddress.append("0");
                                    macAddress.append(Integer.toHexString(b).toUpperCase());
                                    macAddress.append("-");
                                }
                                macAddress.deleteCharAt(macAddress.length() - 1);
                                name = macAddress.toString();
                            }
                        }
                    }
                } catch (SocketException e) {
                }
                if (name == null) {
                    unknownConnections++;
                    name = "Unknown Network Card " + Integer.toString(unknownConnections);
                }
                name = name + " /" + network.getHostAddress();
                netConnections.add(name);
            }
        } catch (UnknownHostException e) {
            if (GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance()) {
                System.err.println("No network card supporting TCP/IP could be detected.  MighTyD requires TCP/IP networking capabilities.");
            } else {
                SystemSounds.warning();
                JOptionPane.showMessageDialog(null, "No network card supporting TCP/IP could be detected.  MighTyD requires TCP/IP networking capabilities.", "MighTyD Server", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SecurityException e) {
        }
        return netConnections.toArray(new String[0]);
    }
