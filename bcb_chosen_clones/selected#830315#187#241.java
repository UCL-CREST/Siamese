    public void handleRequest(XletState desiredState) {
        XletState targetState = currentState;
        synchronized (stateGuard) {
            try {
                if (desiredState == XletState.LOADED) {
                    if (currentState != XletState.UNLOADED) return;
                    targetState = XletState.LOADED;
                    Constructor m = xletClass.getConstructor(new Class[0]);
                    xlet = (Xlet) m.newInstance(new Object[0]);
                } else if (desiredState == DesiredXletState.INITIALIZE) {
                    if (currentState != XletState.LOADED) return;
                    targetState = XletState.PAUSED;
                    try {
                        xlet.initXlet(context);
                    } catch (XletStateChangeException xsce) {
                        targetState = XletState.DESTROYED;
                        xlet.destroyXlet(true);
                    }
                } else if (desiredState == XletState.ACTIVE) {
                    if (currentState != XletState.PAUSED) return;
                    targetState = XletState.ACTIVE;
                    try {
                        xlet.startXlet();
                    } catch (XletStateChangeException xsce) {
                        targetState = currentState;
                    }
                } else if (desiredState == XletState.PAUSED) {
                    if (currentState != XletState.ACTIVE) return;
                    targetState = XletState.PAUSED;
                    xlet.pauseXlet();
                } else if (desiredState == DesiredXletState.CONDITIONAL_DESTROY) {
                    if (currentState == XletState.DESTROYED) return;
                    targetState = XletState.DESTROYED;
                    try {
                        xlet.destroyXlet(false);
                    } catch (XletStateChangeException xsce) {
                        targetState = currentState;
                    }
                } else if (desiredState == XletState.DESTROYED) {
                    targetState = XletState.DESTROYED;
                    if (currentState == XletState.DESTROYED) return;
                    try {
                        xlet.destroyXlet(true);
                    } catch (XletStateChangeException xsce) {
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (targetState != XletState.DESTROYED) {
                    handleRequest(XletState.DESTROYED);
                }
            }
            setState(targetState);
        }
    }
