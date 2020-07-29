    protected void registerClasses() throws PrintException {
        if (!init) {
            try {
                Enumeration<URL> somethingToRegister = this.getClass().getClassLoader().getResources("META-INF/" + getClass().getSimpleName() + ".properties");
                while (somethingToRegister.hasMoreElements()) {
                    URL url = (URL) somethingToRegister.nextElement();
                    InputStream in = url.openStream();
                    BufferedReader buff = new BufferedReader(new InputStreamReader(in));
                    String line = buff.readLine();
                    while (line != null) {
                        log.debug(line);
                        try {
                            Class cls = Class.forName(line);
                            cls.newInstance();
                            log.debug("class " + line + " registered " + url);
                        } catch (ClassNotFoundException e) {
                            log.error("class " + line + " not found " + url, e);
                        } catch (InstantiationException e) {
                            log.error("class " + line + " not found " + url, e);
                        } catch (IllegalAccessException e) {
                            log.error("class " + line + " not found " + url, e);
                        }
                        line = buff.readLine();
                    }
                    buff.close();
                    in.close();
                }
            } catch (IOException e) {
                throw new PrintException(e.getMessage(), e);
            }
            init = true;
        }
    }
