    private void initUrlMap(final ServletContext ctx, final Properties config) {
        final String urlMapClassname = config.getProperty(CFG_URLMAP_CLASSNAME);
        if (urlMapClassname == null) throw new RuntimeException();
        try {
            @SuppressWarnings("unchecked") Class<? extends UrlMap> mapClass = (Class<? extends UrlMap>) Class.forName(urlMapClassname);
            Constructor<? extends UrlMap> ctor = mapClass.getConstructor(Properties.class);
            UrlMap map = ctor.newInstance(config);
            ctx.setAttribute(CTX_MAP, map);
        } catch (Exception e) {
            Logger.getLogger(getLogName(ctx)).log(Level.SEVERE, "Exception instantiating UrlMap class {0} in context {1}: {2}", new Object[] { urlMapClassname, ctx.getServletContextName(), e });
            throw new RuntimeException(e);
        }
    }
