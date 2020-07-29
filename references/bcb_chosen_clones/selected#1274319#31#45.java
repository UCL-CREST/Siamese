    public RPCCallable getInstanceOfRpcCallableClass(Object[] aListOfParameter, Class[] aListOfClass) {
        RPCCallable lNewInstance = null;
        Class lClass = this.getClass();
        try {
            if (aListOfClass == null) {
                lNewInstance = (RPCCallable) lClass.getConstructor().newInstance();
            } else {
                lNewInstance = (RPCCallable) lClass.getConstructor(aListOfClass).newInstance(aListOfParameter);
            }
        } catch (Exception e) {
            mLog.error("Can't build an instance of the RPCCallable class" + lClass.getName() + ". " + "classes: " + aListOfClass + " - parameters: " + aListOfParameter + "." + e.getMessage());
            return null;
        }
        return lNewInstance;
    }
