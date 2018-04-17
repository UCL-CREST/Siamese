    private RpcService getService(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(true);
        String key = request.getPathInfo();
        RpcService service = (RpcService) session.getAttribute(key);
        if (service == null) {
            if (!services.containsKey(key)) throw new Exception("Invalid service key: " + key);
            Constructor<? extends RpcService> constructor = services.get(key).getConstructor(String.class, Config.class);
            service = constructor.newInstance(request.getRemoteUser(), config);
            session.setAttribute(key, service);
        }
        return service;
    }
