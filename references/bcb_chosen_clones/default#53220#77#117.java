    private TrackGenerator createModuleInvocation(String str) {
        String[] segments;
        segments = str.split("[(,)] *");
        try {
            Object[] objs = new Object[2];
            objs[0] = _overlord;
            objs[1] = segments;
            return (TrackGenerator) Class.forName(segments[0]).getConstructor(Class.forName("Megatron"), Class.forName("[Ljava.lang.String;")).newInstance(objs);
        } catch (ClassNotFoundException e) {
            (Megatron.getLog()).log("XMLScheduleLoader: module " + segments[0] + " cannot be found (check paths, including CLASSPATH) (" + e + ")\n");
        } catch (IllegalAccessException e) {
            (Megatron.getLog()).log("XMLScheduleLoader: Module " + segments[0] + " wtf? (" + e + ")\n");
        } catch (NoSuchMethodException e) {
            (Megatron.getLog()).log("XMLScheduleLoader: Module " + segments[0] + " appears to have a private constructor, try to getInstance...");
            try {
                Object[] objs = new Object[2];
                objs[0] = _overlord;
                objs[1] = segments;
                return (TrackGenerator) Class.forName(segments[0]).getMethod("getInstance", new Class[] { Megatron.class, String[].class }).invoke(null, objs);
            } catch (ClassNotFoundException f) {
                (Megatron.getLog()).log("XMLScheduleLoader: this should never happen");
            } catch (NoSuchMethodException f) {
                (Megatron.getLog()).log("XMLScheduleLoader: Could not access constructor or invoke getInstance on " + segments[0] + ".  Giving up.\n");
            } catch (SecurityException f) {
                (Megatron.getLog()).log("XMLScheduleLoader: SECURITY ALERT HAS FUCKED YOU!");
            } catch (IllegalAccessException f) {
                f.printStackTrace();
            } catch (IllegalArgumentException f) {
                (Megatron.getLog()).error("XMLScheduleLoader: you sent the params wrong to getInstance, koder");
            } catch (java.lang.reflect.InvocationTargetException f) {
                (Megatron.getLog()).log("XMLScheduleLoader: getInstance() of the " + segments[0] + " vommitted.  Abandoning all hope");
            } catch (Exception f) {
                f.printStackTrace();
                (Megatron.getLog()).log("XMLScheduleLoader: something went wrong" + f.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            (Megatron.getLog()).error("XMLScheduleLoader: A queer error has been encountered\n");
        }
        return null;
    }
