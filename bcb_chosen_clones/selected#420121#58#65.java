    public void fireUserEvent(Object publisher, FireAfterTransaction events) throws Throwable {
        for (int i = 0; i < events.events().length; i++) {
            Class<? extends EventObject> event = events.events()[i];
            if (ApplicationEvent.class.isAssignableFrom(event)) {
                ctx.publishEvent((ApplicationEvent) event.getConstructor(Object.class).newInstance(publisher));
            }
        }
    }
