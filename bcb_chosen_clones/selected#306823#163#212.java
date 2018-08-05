    private static MapEntry<String, Properties> loadFpmConf() throws ConfigurationReadException {
        MapEntry<String, Properties> ret = null;
        Scanner sc = new Scanner(CONF_PATHS).useDelimiter(SEP_P);
        String prev = "";
        while (sc.hasNext() && !hasLoaded) {
            Properties fpmConf = null;
            boolean relative = false;
            String path = sc.next();
            if (path.startsWith(PREV_P)) {
                path = path.replace(PREV_P, prev.substring(0, prev.length() - 1));
            } else if (path.startsWith(REL_P)) {
                path = path.replace(REL_P + FS, "");
                relative = true;
            } else if (path.contains(HOME_P)) {
                path = path.replace(HOME_P, USER_HOME);
            }
            prev = path;
            path = path.concat(MAIN_CONF_FILE);
            try {
                InputStream is = null;
                if (relative) {
                    is = ClassLoader.getSystemResourceAsStream(path);
                    path = getSystemConfDir();
                    Strings.getOne().createPath(path);
                    path += MAIN_CONF_FILE;
                    FileOutputStream os = new FileOutputStream(path);
                    IOUtils.copy(is, os);
                    os.flush();
                    os.close();
                    os = null;
                } else {
                    is = new FileInputStream(path);
                }
                fpmConf = new Properties();
                fpmConf.load(is);
                if (fpmConf.isEmpty()) {
                    throw new ConfigurationReadException();
                }
                ret = new MapEntry<String, Properties>(path, fpmConf);
                hasLoaded = true;
            } catch (FileNotFoundException e) {
                fpmConf = null;
                singleton = null;
                hasLoaded = false;
            } catch (IOException e) {
                throw new ConfigurationReadException();
            }
        }
        return ret;
    }
