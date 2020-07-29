    private GoogleSiteMapParser getConfigurationSupport(String urlBase) throws Exception {
        if (configurationSupportClass == null) {
            return new GoogleSiteMapParser(urlBase);
        } else {
            try {
                Class supportClass = Class.forName(configurationSupportClass);
                if (!GoogleSiteMapParser.class.isAssignableFrom(supportClass)) {
                    throw new ServletException(supportClass.getName() + " is not assignable from " + GoogleSiteMapParser.class.getName());
                }
                return (GoogleSiteMapParser) supportClass.getConstructor(new Class[] { String.class }).newInstance(new String[] { urlBase });
            } catch (ServletException e) {
                throw e;
            } catch (Exception e) {
                throw new ServletException("Could not instantiate class " + configurationSupportClass);
            }
        }
    }
