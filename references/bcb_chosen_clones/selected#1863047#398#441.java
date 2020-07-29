    private LibraryAdapter createAdapter(String portID, int baudrate) throws KNXException {
        logger.info("try ME CDC support for serial ports (CommConnection)");
        try {
            return new CommConnectionAdapter(portID, baudrate);
        } catch (final KNXException e) {
            logger.warn("ME CDC access to serial port failed", e);
        }
        logger.info("try Calimero native support for serial ports (SerialCom)");
        SerialCom conn = null;
        try {
            conn = new SerialCom(portID);
            conn.setBaudRate(baudrate);
            calcTimeouts(conn.getBaudRate());
            conn.setTimeouts(new SerialCom.Timeouts(idleTimeout, 0, 0, 0, 0));
            conn.setParity(SerialCom.PARITY_EVEN);
            conn.setControl(SerialCom.STOPBITS, SerialCom.ONE_STOPBIT);
            conn.setControl(SerialCom.DATABITS, 8);
            conn.setControl(SerialCom.FLOWCTRL, SerialCom.FLOWCTRL_NONE);
            logger.info("setup serial port: baudrate " + conn.getBaudRate() + ", parity even, databits " + conn.getControl(SerialCom.DATABITS) + ", stopbits " + conn.getControl(SerialCom.STOPBITS) + ", timeouts " + conn.getTimeouts());
            return conn;
        } catch (final IOException e) {
            if (conn != null) try {
                conn.close();
            } catch (final IOException ignore) {
            }
            logger.warn("native access to serial port failed", e);
        }
        logger.info("try rxtx library support for serial ports");
        try {
            final Class c = Class.forName("tuwien.auto.calimero.serial.RxtxAdapter");
            return (LibraryAdapter) c.getConstructors()[0].newInstance(new Object[] { portID, new Integer(baudrate) });
        } catch (final ClassNotFoundException e) {
            logger.warn("rxtx library adapter not found");
        } catch (final SecurityException e) {
            logger.error("rxtx library adapter access denied", e);
        } catch (final InvocationTargetException e) {
            logger.error("initalizing rxtx serial port", e.getCause());
        } catch (final Exception e) {
            logger.warn("rxtx access to serial port failed", e);
        } catch (final NoClassDefFoundError e) {
            logger.error("no rxtx library classes found", e);
        }
        throw new KNXException("can not open serial port " + portID);
    }
