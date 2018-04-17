    public static synchronized void loadConfig(String configFile) {
        if (properties != null) {
            return;
        }
        URL url = null;
        InputStream is = null;
        try {
            String configProperty = null;
            try {
                configProperty = System.getProperty("dspace.configuration");
            } catch (SecurityException se) {
                log.warn("Unable to access system properties, ignoring.", se);
            }
            if (loadedFile != null) {
                log.info("Reloading current config file: " + loadedFile.getAbsolutePath());
                url = loadedFile.toURI().toURL();
            } else if (configFile != null) {
                log.info("Loading provided config file: " + configFile);
                loadedFile = new File(configFile);
                url = loadedFile.toURI().toURL();
            } else if (configProperty != null) {
                log.info("Loading system provided config property (-Ddspace.configuration): " + configProperty);
                loadedFile = new File(configProperty);
                url = loadedFile.toURI().toURL();
            } else {
                url = ConfigurationManager.class.getResource("/dspace.cfg");
                if (url != null) {
                    log.info("Loading from classloader: " + url);
                    loadedFile = new File(url.getPath());
                }
            }
            if (url == null) {
                log.fatal("Cannot find dspace.cfg");
                throw new IllegalStateException("Cannot find dspace.cfg");
            } else {
                properties = new Properties();
                moduleProps = new HashMap<String, Properties>();
                is = url.openStream();
                properties.load(is);
                for (Enumeration<?> pe = properties.propertyNames(); pe.hasMoreElements(); ) {
                    String key = (String) pe.nextElement();
                    String value = interpolate(key, properties.getProperty(key), 1);
                    if (value != null) {
                        properties.setProperty(key, value);
                    }
                }
            }
        } catch (IOException e) {
            log.fatal("Can't load configuration: " + url, e);
            throw new IllegalStateException("Cannot load configuration: " + url, e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ioe) {
                }
            }
        }
        File licenseFile = new File(getProperty("dspace.dir") + File.separator + "config" + File.separator + "default.license");
        FileInputStream fir = null;
        InputStreamReader ir = null;
        BufferedReader br = null;
        try {
            fir = new FileInputStream(licenseFile);
            ir = new InputStreamReader(fir, "UTF-8");
            br = new BufferedReader(ir);
            String lineIn;
            license = "";
            while ((lineIn = br.readLine()) != null) {
                license = license + lineIn + '\n';
            }
            br.close();
        } catch (IOException e) {
            log.fatal("Can't load license: " + licenseFile.toString(), e);
            throw new IllegalStateException("Cannot load license: " + licenseFile.toString(), e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                }
            }
            if (ir != null) {
                try {
                    ir.close();
                } catch (IOException ioe) {
                }
            }
            if (fir != null) {
                try {
                    fir.close();
                } catch (IOException ioe) {
                }
            }
        }
    }
