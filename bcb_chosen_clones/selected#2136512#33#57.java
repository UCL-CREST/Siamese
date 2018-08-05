    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (ReloadingContext.getInstance().isDebug()) {
            DispatchFilter.updateRequestDispatcher();
            if (getClass().getClassLoader() == DispatchFilter.lastReloadingClassLoader) {
                handleDebugModeDoFilter(request, response, chain);
            } else {
                Object object;
                try {
                    object = DispatchFilter.lastReloadingClassLoader.loadClass(DebugResourceFilter.class.getName()).getConstructor().newInstance();
                } catch (Exception e) {
                    logger.error("Can't create instance of DebugResourceFilter.", e);
                    throw new NocturneException("Can't create instance of DebugResourceFilter.", e);
                }
                try {
                    ReflectionUtil.invoke(object, "handleDebugModeDoFilter", request, response, chain);
                } catch (ReflectionException e) {
                    logger.error("Can't run DebugResourceFilter.", e);
                    throw new NocturneException("Can't run DebugResourceFilter.", e);
                }
            }
        } else {
            chain.doFilter(request, response);
        }
    }
