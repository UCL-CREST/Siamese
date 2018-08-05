    public static void main(String args[]) {
        String midletClass = null;
        ;
        File appletInputFile = null;
        File deviceInputFile = null;
        File midletInputFile = null;
        File htmlOutputFile = null;
        File appletOutputFile = null;
        File deviceOutputFile = null;
        File midletOutputFile = null;
        List params = new ArrayList();
        for (int i = 0; i < args.length; i++) {
            params.add(args[i]);
        }
        Iterator argsIterator = params.iterator();
        while (argsIterator.hasNext()) {
            String arg = (String) argsIterator.next();
            argsIterator.remove();
            if ((arg.equals("--help")) || (arg.equals("-help"))) {
                System.out.println(usage());
                System.exit(0);
            } else if (arg.equals("--midletClass")) {
                midletClass = (String) argsIterator.next();
                argsIterator.remove();
            } else if (arg.equals("--appletInput")) {
                appletInputFile = new File((String) argsIterator.next());
                argsIterator.remove();
            } else if (arg.equals("--deviceInput")) {
                deviceInputFile = new File((String) argsIterator.next());
                argsIterator.remove();
            } else if (arg.equals("--midletInput")) {
                midletInputFile = new File((String) argsIterator.next());
                argsIterator.remove();
            } else if (arg.equals("--htmlOutput")) {
                htmlOutputFile = new File((String) argsIterator.next());
                argsIterator.remove();
            } else if (arg.equals("--appletOutput")) {
                appletOutputFile = new File((String) argsIterator.next());
                argsIterator.remove();
            } else if (arg.equals("--deviceOutput")) {
                deviceOutputFile = new File((String) argsIterator.next());
                argsIterator.remove();
            } else if (arg.equals("--midletOutput")) {
                midletOutputFile = new File((String) argsIterator.next());
                argsIterator.remove();
            }
        }
        if (midletClass == null || appletInputFile == null || deviceInputFile == null || midletInputFile == null || htmlOutputFile == null || appletOutputFile == null || deviceOutputFile == null || midletOutputFile == null) {
            System.out.println(usage());
            System.exit(0);
        }
        try {
            DeviceImpl device = null;
            String descriptorLocation = null;
            JarFile jar = new JarFile(deviceInputFile);
            for (Enumeration en = jar.entries(); en.hasMoreElements(); ) {
                String entry = ((JarEntry) en.nextElement()).getName();
                if ((entry.toLowerCase().endsWith(".xml") || entry.toLowerCase().endsWith("device.txt")) && !entry.toLowerCase().startsWith("meta-inf")) {
                    descriptorLocation = entry;
                    break;
                }
            }
            if (descriptorLocation != null) {
                EmulatorContext context = new EmulatorContext() {

                    private DisplayComponent displayComponent = new NoUiDisplayComponent();

                    private InputMethod inputMethod = new J2SEInputMethod();

                    private DeviceDisplay deviceDisplay = new J2SEDeviceDisplay(this);

                    private FontManager fontManager = new J2SEFontManager();

                    private DeviceComponent deviceComponent = new SwingDeviceComponent(true);

                    public DisplayComponent getDisplayComponent() {
                        return displayComponent;
                    }

                    public InputMethod getDeviceInputMethod() {
                        return inputMethod;
                    }

                    public DeviceDisplay getDeviceDisplay() {
                        return deviceDisplay;
                    }

                    public FontManager getDeviceFontManager() {
                        return fontManager;
                    }

                    public InputStream getResourceAsStream(String name) {
                        return MIDletBridge.getCurrentMIDlet().getClass().getResourceAsStream(name);
                    }

                    public DeviceComponent getDeviceComponent() {
                        return deviceComponent;
                    }
                };
                URL[] urls = new URL[1];
                urls[0] = deviceInputFile.toURI().toURL();
                ClassLoader classLoader = new ExtensionsClassLoader(urls, urls.getClass().getClassLoader());
                device = DeviceImpl.create(context, classLoader, descriptorLocation, J2SEDevice.class);
            }
            if (device == null) {
                System.out.println("Error parsing device package: " + descriptorLocation);
                System.exit(0);
            }
            createHtml(htmlOutputFile, device, midletClass, midletOutputFile, appletOutputFile, deviceOutputFile);
            createMidlet(midletInputFile.toURI().toURL(), midletOutputFile);
            IOUtils.copyFile(appletInputFile, appletOutputFile);
            IOUtils.copyFile(deviceInputFile, deviceOutputFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.exit(0);
    }
