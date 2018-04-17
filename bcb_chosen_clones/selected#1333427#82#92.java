    public String getUrl() {
        try {
            Class driver = this.getDriver();
            Method getUrl = this.getEngineMethod("getUrl");
            Object driverInstance = driver.getConstructor(new Class[] {}).newInstance(new Object[] {});
            return (String) getUrl.invoke(driverInstance, new Object[] {});
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
