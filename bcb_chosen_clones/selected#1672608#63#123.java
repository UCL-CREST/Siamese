    public void init() {
        File[] configsDirectories = { XPontusConstantsIF.XPONTUS_PLUGINS_DATA_DIR, XPontusConstantsIF.XPONTUS_PREFERENCES_DIR, XPontusConstantsIF.XPONTUS_DATABASE_CONFIG_DIR, XPontusConstantsIF.XPONTUS_PLUGINS_DIR, XPontusConstantsIF.XPONTUS_CACHE_DIR };
        for (int i = 0; i < configsDirectories.length; i++) {
            if (!configsDirectories[i].exists()) {
                configsDirectories[i].mkdirs();
            }
        }
        String[] locations = { "/net/sf/xpontus/configuration/editorPanel.properties", "/net/sf/xpontus/configuration/general.properties", "/net/sf/xpontus/configuration/mimetypes.properties" };
        try {
            for (String loc : locations) {
                String outName = FilenameUtils.getName(loc);
                File output = new File(XPontusConstantsIF.XPONTUS_PREFERENCES_DIR, outName);
                if (!output.exists()) {
                    if (loc.equals(locations[0])) {
                        Properties hackProps = new Properties();
                        InputStream is = getClass().getResourceAsStream(loc);
                        hackProps.load(is);
                        Font hackFont = UIManager.getFont("EditorPane.font");
                        StrBuilder strFont = new StrBuilder();
                        strFont.append(hackFont.getFamily() + "," + hackFont.getStyle() + "," + hackFont.getSize());
                        hackProps.put("EditorPane.Font", strFont.toString());
                        OutputStream out = new FileOutputStream(output);
                        hackProps.store(out, null);
                        out.close();
                        is.close();
                    } else {
                        InputStream is = getClass().getResourceAsStream(loc);
                        OutputStream out = new FileOutputStream(output);
                        IOUtils.copy(is, out);
                        out.close();
                        is.close();
                    }
                }
                if (!outName.equals("mimetypes.properties")) {
                    Properties m_properties = PropertiesConfigurationLoader.load(output);
                    Iterator it = m_properties.keySet().iterator();
                    while (it.hasNext()) {
                        Object m_key = it.next();
                        Object m_value = m_properties.get(m_key);
                        XPontusConfig.put(m_key, m_value);
                    }
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        Properties m_props = PropertiesConfigurationLoader.load(XPontusConfigurationConstantsIF.EDITOR_PREFERENCES_FILE);
        String[] f = m_props.get("EditorPane.Font").toString().split(",");
        String family = f[0].trim();
        String style1 = f[1].trim();
        int style = Integer.parseInt(style1);
        int size = Integer.parseInt(f[2].trim());
        Font m_font = new Font(family, style, size);
        XPontusConfig.put("EditorPane.Font", m_font);
        Map map = new HashMap();
        map.put(ROLE, this);
        PropertiesHolder.registerProperty(XPontusSettings.KEY, map);
        DockableContainerFactory.setFactory(new XPontusDockableContainerFactory());
        FileHistoryList.init();
        initDefaultSettings();
    }
