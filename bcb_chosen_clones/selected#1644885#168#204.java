    private static void configureNamedPlugin(String iname) throws ClassNotFoundException {
        int found = 0;
        if (!namedPluginClasses.containsKey(iname)) {
            String namedVal = ConfigurationManager.getProperty(NAMED_PREFIX + iname);
            if (namedVal != null) {
                namedVal = namedVal.trim();
                log.debug("Got Named configuration for interface=" + iname + ", config=" + namedVal);
                Pattern classnameEqual = Pattern.compile("([\\w\\p{Sc}\\.]+)\\s*\\=");
                int prevEnd = -1;
                String prevClassName = null;
                Matcher classMatcher = classnameEqual.matcher(namedVal);
                while (classMatcher.find()) {
                    if (prevClassName != null) found += installNamedConfigs(iname, prevClassName, namedVal.substring(prevEnd, classMatcher.start()).trim().split("\\s*,\\s*"));
                    prevClassName = classMatcher.group(1);
                    prevEnd = classMatcher.end();
                }
                if (prevClassName != null) found += installNamedConfigs(iname, prevClassName, namedVal.substring(prevEnd).trim().split("\\s*,\\s*"));
            }
            String selfNamedVal = ConfigurationManager.getProperty(SELFNAMED_PREFIX + iname);
            if (selfNamedVal != null) {
                String classnames[] = selfNamedVal.trim().split("\\s*,\\s*");
                for (int i = 0; i < classnames.length; ++i) {
                    try {
                        Class pluginClass = Class.forName(classnames[i]);
                        String names[] = (String[]) pluginClass.getMethod("getPluginNames", null).invoke(null, null);
                        if (names == null || names.length == 0) log.error("Self-named plugin class \"" + classnames[i] + "\" returned null or empty name list!"); else found += installNamedConfigs(iname, classnames[i], names);
                    } catch (NoSuchMethodException e) {
                        log.error("Implementation Class \"" + classnames[i] + "\" is not a subclass of SelfNamedPlugin, it has no getPluginNames() method.");
                    } catch (Exception e) {
                        log.error("While configuring self-named plugin: " + e.toString());
                    }
                }
            }
            namedPluginClasses.put(iname, "org.dspace.core.marker");
            if (found == 0) log.error("No named plugins found for interface=" + iname);
        }
    }
