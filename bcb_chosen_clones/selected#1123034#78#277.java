    public void init(String[] arguments) {
        if (arguments.length < 1) {
            printHelp();
            return;
        }
        String[] valid_args = new String[] { "device*", "d*", "help", "h", "speed#", "s#", "file*", "f*", "gpsd*", "nmea", "n", "garmin", "g", "sirf", "i", "rawdata", "downloadtracks", "downloadwaypoints", "downloadroutes", "deviceinfo", "printposonce", "printpos", "p", "printalt", "printspeed", "printheading", "printsat", "template*", "outfile*", "screenshot*", "printdefaulttemplate", "helptemplate", "nmealogfile*", "l", "uploadtracks", "uploadroutes", "uploadwaypoints", "infile*" };
        CommandArguments args = null;
        try {
            args = new CommandArguments(arguments, valid_args);
        } catch (CommandArgumentException cae) {
            System.err.println("Invalid arguments: " + cae.getMessage());
            printHelp();
            return;
        }
        String filename = null;
        String serial_port_name = null;
        boolean gpsd = false;
        String gpsd_host = "localhost";
        int gpsd_port = 2947;
        int serial_port_speed = -1;
        GPSDataProcessor gps_data_processor;
        String nmea_log_file = null;
        if (args.isSet("help") || (args.isSet("h"))) {
            printHelp();
            return;
        }
        if (args.isSet("helptemplate")) {
            printHelpTemplate();
        }
        if (args.isSet("printdefaulttemplate")) {
            System.out.println(DEFAULT_TEMPLATE);
        }
        if (args.isSet("device")) {
            serial_port_name = (String) args.getValue("device");
        } else if (args.isSet("d")) {
            serial_port_name = (String) args.getValue("d");
        }
        if (args.isSet("speed")) {
            serial_port_speed = ((Integer) args.getValue("speed")).intValue();
        } else if (args.isSet("s")) {
            serial_port_speed = ((Integer) args.getValue("s")).intValue();
        }
        if (args.isSet("file")) {
            filename = (String) args.getValue("file");
        } else if (args.isSet("f")) {
            filename = (String) args.getValue("f");
        }
        if (args.isSet("gpsd")) {
            gpsd = true;
            String gpsd_host_port = (String) args.getValue("gpsd");
            if (gpsd_host_port != null && gpsd_host_port.length() > 0) {
                String[] params = gpsd_host_port.split(":");
                gpsd_host = params[0];
                if (params.length > 0) {
                    gpsd_port = Integer.parseInt(params[1]);
                }
            }
        }
        if (args.isSet("garmin") || args.isSet("g")) {
            gps_data_processor = new GPSGarminDataProcessor();
            serial_port_speed = 9600;
            if (filename != null) {
                System.err.println("ERROR: Cannot read garmin data from file, only serial port supported!");
                return;
            }
        } else if (args.isSet("sirf") || args.isSet("i")) {
            gps_data_processor = new GPSSirfDataProcessor();
            serial_port_speed = 19200;
            if (filename != null) {
                System.err.println("ERROR: Cannot read sirf data from file, only serial port supported!");
                return;
            }
        } else {
            gps_data_processor = new GPSNmeaDataProcessor();
            serial_port_speed = 4800;
        }
        if (args.isSet("nmealogfile") || (args.isSet("l"))) {
            if (args.isSet("nmealogfile")) nmea_log_file = args.getStringValue("nmealogfile"); else nmea_log_file = args.getStringValue("l");
        }
        if (args.isSet("rawdata")) {
            gps_data_processor.addGPSRawDataListener(new GPSRawDataListener() {

                public void gpsRawDataReceived(char[] data, int offset, int length) {
                    System.out.println("RAWLOG: " + new String(data, offset, length));
                }
            });
        }
        GPSDevice gps_device;
        Hashtable environment = new Hashtable();
        if (filename != null) {
            environment.put(GPSFileDevice.PATH_NAME_KEY, filename);
            gps_device = new GPSFileDevice();
        } else if (gpsd) {
            environment.put(GPSNetworkGpsdDevice.GPSD_HOST_KEY, gpsd_host);
            environment.put(GPSNetworkGpsdDevice.GPSD_PORT_KEY, new Integer(gpsd_port));
            gps_device = new GPSNetworkGpsdDevice();
        } else {
            if (serial_port_name != null) environment.put(GPSSerialDevice.PORT_NAME_KEY, serial_port_name);
            if (serial_port_speed > -1) environment.put(GPSSerialDevice.PORT_SPEED_KEY, new Integer(serial_port_speed));
            gps_device = new GPSSerialDevice();
        }
        try {
            gps_device.init(environment);
            gps_data_processor.setGPSDevice(gps_device);
            gps_data_processor.open();
            gps_data_processor.addProgressListener(this);
            if ((nmea_log_file != null) && (nmea_log_file.length() > 0)) {
                gps_data_processor.addGPSRawDataListener(new GPSRawDataFileLogger(nmea_log_file));
            }
            if (args.isSet("deviceinfo")) {
                System.out.println("GPSInfo:");
                String[] infos = gps_data_processor.getGPSInfo();
                for (int index = 0; index < infos.length; index++) {
                    System.out.println(infos[index]);
                }
            }
            if (args.isSet("screenshot")) {
                FileOutputStream out = new FileOutputStream((String) args.getValue("screenshot"));
                BufferedImage image = gps_data_processor.getScreenShot();
                ImageIO.write(image, "PNG", out);
            }
            boolean print_waypoints = args.isSet("downloadwaypoints");
            boolean print_routes = args.isSet("downloadroutes");
            boolean print_tracks = args.isSet("downloadtracks");
            if (print_waypoints || print_routes || print_tracks) {
                VelocityContext context = new VelocityContext();
                if (print_waypoints) {
                    List waypoints = gps_data_processor.getWaypoints();
                    if (waypoints != null) context.put("waypoints", waypoints); else print_waypoints = false;
                }
                if (print_tracks) {
                    List tracks = gps_data_processor.getTracks();
                    if (tracks != null) context.put("tracks", tracks); else print_tracks = false;
                }
                if (print_routes) {
                    List routes = gps_data_processor.getRoutes();
                    if (routes != null) context.put("routes", routes); else print_routes = false;
                }
                context.put("printwaypoints", new Boolean(print_waypoints));
                context.put("printtracks", new Boolean(print_tracks));
                context.put("printroutes", new Boolean(print_routes));
                Writer writer;
                Reader reader;
                if (args.isSet("template")) {
                    String template_file = (String) args.getValue("template");
                    reader = new FileReader(template_file);
                } else {
                    reader = new StringReader(DEFAULT_TEMPLATE);
                }
                if (args.isSet("outfile")) writer = new FileWriter((String) args.getValue("outfile")); else writer = new OutputStreamWriter(System.out);
                addDefaultValuesToContext(context);
                boolean result = printTemplate(context, reader, writer);
            }
            boolean read_waypoints = (args.isSet("uploadwaypoints") && args.isSet("infile"));
            boolean read_routes = (args.isSet("uploadroutes") && args.isSet("infile"));
            boolean read_tracks = (args.isSet("uploadtracks") && args.isSet("infile"));
            if (read_waypoints || read_routes || read_tracks) {
                ReadGPX reader = new ReadGPX();
                String in_file = (String) args.getValue("infile");
                reader.parseFile(in_file);
                if (read_waypoints) gps_data_processor.setWaypoints(reader.getWaypoints());
                if (read_routes) gps_data_processor.setRoutes(reader.getRoutes());
                if (read_tracks) gps_data_processor.setTracks(reader.getTracks());
            }
            if (args.isSet("printposonce")) {
                GPSPosition pos = gps_data_processor.getGPSPosition();
                System.out.println("Current Position: " + pos);
            }
            if (args.isSet("printpos") || args.isSet("p")) {
                gps_data_processor.addGPSDataChangeListener(GPSDataProcessor.LOCATION, this);
            }
            if (args.isSet("printalt")) {
                gps_data_processor.addGPSDataChangeListener(GPSDataProcessor.ALTITUDE, this);
            }
            if (args.isSet("printspeed")) {
                gps_data_processor.addGPSDataChangeListener(GPSDataProcessor.SPEED, this);
            }
            if (args.isSet("printheading")) {
                gps_data_processor.addGPSDataChangeListener(GPSDataProcessor.HEADING, this);
            }
            if (args.isSet("printsat")) {
                gps_data_processor.addGPSDataChangeListener(GPSDataProcessor.NUMBER_SATELLITES, this);
                gps_data_processor.addGPSDataChangeListener(GPSDataProcessor.SATELLITE_INFO, this);
            }
            if (args.isSet("printpos") || args.isSet("p") || args.isSet("printalt") || args.isSet("printsat") || args.isSet("printspeed") || args.isSet("printheading")) {
                gps_data_processor.startSendPositionPeriodically(1000L);
                try {
                    System.in.read();
                } catch (IOException ignore) {
                }
            }
            gps_data_processor.close();
        } catch (GPSException e) {
            e.printStackTrace();
        } catch (FileNotFoundException fnfe) {
            System.err.println("ERROR: File not found: " + fnfe.getMessage());
        } catch (IOException ioe) {
            System.err.println("ERROR: I/O Error: " + ioe.getMessage());
        }
    }
