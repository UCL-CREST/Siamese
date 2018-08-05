    public void stop(String applicationName, String appID) {
        System.out.println("STOP APPLICATION: " + applicationName + " " + appID + "\r\n");
        Entry entry = (Entry) applications.get(applicationName + " " + appID);
        Object application = entry.object;
        applications.remove(applicationName + " " + appID);
        try {
            Method method = application.getClass().getMethod("stopApplication", null);
            method.invoke(application, null);
        } catch (Exception exc) {
            System.out.println("exception: " + exc + "\r\n");
        }
        ;
    }
