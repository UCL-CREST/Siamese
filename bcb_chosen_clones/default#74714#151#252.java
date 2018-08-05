    public static void main(String args[]) throws Exception {
        int device = 0;
        byte[] writeArray = null;
        String dumpPrefix = null;
        OSIDataLinkDevice[] devices = OSIDataLinkDevice.getDevices();
        try {
            for (int ii = 0; ii < args.length; ++ii) {
                if ("-dev".equals(args[ii])) {
                    String devStr = args[++ii];
                    try {
                        device = Integer.parseInt(devStr);
                    } catch (NumberFormatException nfe) {
                        FileInputStream fis = new FileInputStream(devStr);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        int ch;
                        while ((ch = fis.read()) != -1) baos.write(ch);
                        device = OSIDataLinkDevice.findBestMatch(devices, new String(baos.toByteArray()));
                    }
                } else if ("-l".equals(args[ii])) {
                    listDev = true;
                } else if ("-xml".equals(args[ii])) {
                    showXML = true;
                } else if ("-ip".equals(args[ii])) {
                    addIp = true;
                    String[] fromTo = new String[2];
                    ii = extractFromTo(args, ii + 1, fromTo);
                    if (fromTo[0] != null || fromTo[1] != null) {
                        InetAddress fromIP = null, toIP = null;
                        if (fromTo[0] != null) fromIP = InetAddress.getByName(fromTo[0]);
                        if (fromTo[1] != null) toIP = InetAddress.getByName(fromTo[1]);
                        ipFilters.add(new IPFromToFilter(fromIP, toIP));
                    }
                } else if ("-udp".equals(args[ii])) {
                    addIp = addUdp = true;
                    String[] fromTo = new String[2];
                    ii = extractFromTo(args, ii + 1, fromTo);
                    if (fromTo[0] != null || fromTo[1] != null) {
                        udpFilters.add(new UDPPortFilter(fromTo[0] != null ? Short.parseShort(fromTo[0]) : UDPPortFilter.UNSPECIFIED_PORT, fromTo[1] != null ? Short.parseShort(fromTo[1]) : UDPPortFilter.UNSPECIFIED_PORT));
                    }
                } else if ("-tcp".equals(args[ii])) {
                    addIp = addTcp = true;
                    String[] fromTo = new String[2];
                    ii = extractFromTo(args, ii + 1, fromTo);
                    if (fromTo[0] != null || fromTo[1] != null) {
                        tcpFilters.add(new TCPPortFilter(fromTo[0] != null ? Short.parseShort(fromTo[0]) : TCPPortFilter.UNSPECIFIED_PORT, fromTo[1] != null ? Short.parseShort(fromTo[1]) : TCPPortFilter.UNSPECIFIED_PORT));
                    }
                } else if ("-icmp".equals(args[ii])) {
                    addIp = addIcmp = true;
                } else if ("-arp".equals(args[ii])) {
                    addArp = true;
                } else if ("-raw".equals(args[ii])) {
                    showRaw = true;
                } else if ("-dump".equals(args[ii])) {
                    dumpPrefix = args[++ii];
                } else if ("-write".equals(args[ii])) {
                    FileInputStream fis = new FileInputStream(args[++ii]);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int ch;
                    while ((ch = fis.read()) != -1) baos.write(ch);
                    writeArray = baos.toByteArray();
                } else if ("-n".equals(args[ii])) {
                    byteLimit = Integer.parseInt(args[++ii]);
                } else {
                    showHelp();
                    return;
                }
            }
        } catch (ArrayIndexOutOfBoundsException aie) {
            aie.printStackTrace();
            System.err.println("Invalid arguments.");
            showHelp();
            return;
        }
        if (listDev) {
            listDevices(devices);
            return;
        }
        OSIDataLinkDevice osld = devices[device];
        devices = null;
        System.out.println(osld);
        if (showXML) System.out.println(osld.getXMLDescription());
        if (addIp) {
            if (ipFilters.size() == 0) {
                IPHandler iph = new IPHandler(osld);
                addHandlers(iph, "IP Packet");
            } else for (Iterator iter = ipFilters.iterator(); iter.hasNext(); ) {
                IPHandler iph = new IPHandler(osld);
                IPFilter f = (IPFilter) iter.next();
                iph.addFilter(f);
                addHandlers(iph, f.toString());
            }
        } else if (addArp) {
            ARPHandler arph = new ARPHandler(osld);
            arph.addPacketListener(new MyPacketListener("ARP Packet"));
        } else showRaw = true;
        if (showRaw) osld.addPacketListener(new MyPacketListener("Raw"));
        if (dumpPrefix != null) osld.addPacketListener(new DumpPacketListener(dumpPrefix));
        osld.startCapture();
        if (writeArray != null) osld.sendPacket(writeArray);
        while (System.in.read() != '\n') ;
        osld.stopCapture();
    }
