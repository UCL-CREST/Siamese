    public void createSessionBindingListenerBridge(final String name, final Object value) {
        try {
            if (this.sessionBindingListenerBridge == null) {
                final Class<?> clazz = CMainFilter.getInstance().getWebappLoader().loadClass("org.allcolor.alc.webapp.SessionBindingListenerBridge");
                this.sessionBindingListenerBridge = clazz.getConstructor(new Class[] { String.class, Object.class });
            }
            final Object binder = this.sessionBindingListenerBridge.newInstance(new Object[] { name, value });
            this._SetAttribute(name + ".session.binder.BRIDGE", binder);
        } catch (final Exception e) {
            ;
        }
    }
