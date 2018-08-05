    static void start(String[] args) throws Exception {
        String socket = findSocket();
        String os = null;
        String separator = "/-+.,;: ";
        try {
            String val = System.getProperty("os.name").toLowerCase();
            StringTokenizer t = new StringTokenizer(val, separator);
            os = t.nextToken();
        } catch (Throwable t) {
        }
        if (os == null) os = "unknown";
        File ext = null;
        try {
            ext = (args.length == 0) ? new File(new File(System.getProperty("java.class.path")).getParentFile().getAbsoluteFile(), "ext") : new File(args[0], "ext");
            ext = ext.getAbsoluteFile();
        } catch (Throwable t) {
            ext = (args.length == 0) ? new File("ext") : new File(args[0], "ext");
            ext = ext.getAbsoluteFile();
        }
        if (!ext.isDirectory()) ext.mkdirs();
        base = ext.getParentFile();
        File java = new File(base, "java");
        if (!java.isDirectory()) java.mkdirs();
        ClassLoader loader = TestInstallation.class.getClassLoader();
        InputStream in = loader.getResourceAsStream("WEB-INF/lib/JavaBridge.jar");
        extractFile(in, new File(ext, "JavaBridge.jar").getAbsoluteFile());
        in.close();
        in = loader.getResourceAsStream("WEB-INF/lib/php-script.jar");
        extractFile(in, new File(ext, "php-script.jar").getAbsoluteFile());
        in.close();
        in = loader.getResourceAsStream("WEB-INF/lib/php-servlet.jar");
        extractFile(in, new File(ext, "php-servlet.jar").getAbsoluteFile());
        in.close();
        in = loader.getResourceAsStream("WEB-INF/lib/script-api.jar");
        extractFile(in, new File(ext, "script-api.jar").getAbsoluteFile());
        in.close();
        in = loader.getResourceAsStream("test.php");
        extractFile(in, new File(base, "test.php").getAbsoluteFile());
        in.close();
        (new Thread(new TestInstallation())).start();
        int count = 20;
        while (count-- > 0) {
            Thread.sleep(500);
            try {
                Socket s = new Socket("127.0.0.1", Integer.parseInt(socket));
                if (s != null) s.close();
                break;
            } catch (IOException e) {
            }
        }
        if (count == 0) throw new IOException("Could not start test servlet engine");
        URL url = new URL("http://127.0.0.1:" + socket + "/JavaBridge/java/Java.inc");
        URLConnection conn = url.openConnection();
        conn.connect();
        in = conn.getInputStream();
        extractFile(in, new File(java, "Java.inc").getAbsoluteFile());
        in.close();
        FileOutputStream o = new FileOutputStream(new File(base, "RESULT.html"));
        String php = "php-cgi";
        for (int i = 0; i < DEFAULT_CGI_LOCATIONS.length; i++) {
            File location = new File(DEFAULT_CGI_LOCATIONS[i]);
            if (location.exists()) {
                php = location.getAbsolutePath();
                break;
            }
        }
        String[] cmd = new String[] { php, "-n", "-d", "allow_url_include=On", String.valueOf(new File(base, "test.php")) };
        System.err.println("Invoking php: " + Arrays.asList(cmd));
        HashMap env = (HashMap) processEnvironment.clone();
        env.put("SERVER_PORT", socket);
        env.put("X_JAVABRIDGE_OVERRIDE_HOSTS", "h:127.0.0.1:" + socket + "//JavaBridge/test.phpjavabridge");
        Process p;
        try {
            p = Runtime.getRuntime().exec(cmd, hashToStringArray(env));
        } catch (java.io.IOException ex) {
            throw new RuntimeException("Could not run PHP (" + Arrays.asList(cmd) + "), please check if php-cgi is in the path.", ex);
        }
        (new Thread(new TestInstallation(p))).start();
        InputStream i = p.getInputStream();
        int c;
        while ((c = i.read()) != -1) o.write(c);
        i.close();
        p.getOutputStream().close();
        o.close();
        System.out.flush();
        System.err.flush();
        System.out.println("\nNow check the " + new File(base, "RESULT.html."));
        System.out.println("Read the INSTALL.J2EE and/or INSTALL.J2SE documents.");
        try {
            SwingUtilities.invokeAndWait(new SimpleBrowser(socket));
        } catch (Throwable err) {
            System.exit(0);
        }
    }
