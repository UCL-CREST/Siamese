    @Test
    public void testConfigurartion() {
        try {
            Enumeration<URL> assemblersToRegister = this.getClass().getClassLoader().getResources("META-INF/PrintAssemblerFactory.properties");
            log.debug("PrintAssemblerFactory " + SimplePrintJobTest.class.getClassLoader().getResource("META-INF/PrintAssemblerFactory.properties"));
            log.debug("ehcache " + SimplePrintJobTest.class.getClassLoader().getResource("ehcache.xml"));
            log.debug("log4j " + this.getClass().getClassLoader().getResource("/log4j.xml"));
            if (log.isDebugEnabled()) {
                while (assemblersToRegister.hasMoreElements()) {
                    URL url = (URL) assemblersToRegister.nextElement();
                    InputStream in = url.openStream();
                    BufferedReader buff = new BufferedReader(new InputStreamReader(in));
                    String line = buff.readLine();
                    while (line != null) {
                        log.debug(line);
                        line = buff.readLine();
                    }
                    buff.close();
                    in.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
