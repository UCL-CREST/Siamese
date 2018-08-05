    @Override
    public void init() {
        super.init();
        try {
            Class<?> handlerClass = Class.forName("org.knowhowlab.osgi.jmx.beans.service.monitor.MonitorAdminEventHandler");
            Constructor<?> constructor = handlerClass.getConstructor(OsgiVisitor.class, LogVisitor.class, NotificationBroadcasterSupport.class, NotificationBroadcaster.class);
            Object handler = constructor.newInstance(visitor, logVisitor, nbs, this);
            Dictionary props = (Dictionary) handlerClass.getMethod("getHandlerProperties").invoke(handler);
            handlerRegistration = visitor.registerService("org.osgi.service.event.EventHandler", handler, props);
        } catch (Exception e) {
            logVisitor.warning("Unable to init EventHandler. MonitorAdmin events are ignored", e);
        }
    }
