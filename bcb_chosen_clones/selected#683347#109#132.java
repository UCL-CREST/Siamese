    public boObjectState getStateManager(boObject object) {
        if (!classnotfound) {
            try {
                boDefHandler bodef = this.getBoDefHandler();
                String version = "v" + bodef.getBoVersion().replace('.', '_');
                String name = version + "." + bodef.getName() + "StateManager";
                Class xcls = Class.forName(name, false, boApplication.currentContext().getApplication().getClassLoader());
                Constructor con = xcls.getConstructor(new Class[] { boObject.class });
                Object xret = con.newInstance(new Object[] { object });
                return (boObjectState) xret;
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getClass().getName() + "\n" + e.getMessage());
            } catch (InstantiationException e) {
                throw new RuntimeException(e.getClass().getName() + "\n" + e.getMessage());
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e.getClass().getName() + "\n" + e.getMessage());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getClass().getName() + "\n" + e.getMessage());
            } catch (ClassNotFoundException e) {
                classnotfound = true;
            }
        }
        return null;
    }
