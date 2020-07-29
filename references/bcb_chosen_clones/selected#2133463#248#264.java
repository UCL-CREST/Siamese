    private SipStack createStack(Properties properties) throws PeerUnavailableException {
        try {
            Class[] paramTypes = new Class[1];
            paramTypes[0] = Class.forName("java.util.Properties");
            Constructor sipStackConstructor = Class.forName(getPathName() + ".javax.sip.SipStackImpl").getConstructor(paramTypes);
            Object[] conArgs = new Object[1];
            conArgs[0] = properties;
            SipStack sipStack = (SipStack) sipStackConstructor.newInstance(conArgs);
            sipStackList.add(sipStack);
            String name = properties.getProperty("javax.sip.STACK_NAME");
            this.sipStackByName.put(name, sipStack);
            return sipStack;
        } catch (Exception e) {
            String errmsg = "The Peer SIP Stack: " + getPathName() + ".javax.sip.SipStackImpl" + " could not be instantiated. Ensure the Path Name has been set.";
            throw new PeerUnavailableException(errmsg, e);
        }
    }
