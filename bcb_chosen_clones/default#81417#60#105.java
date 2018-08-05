        public void run() {
            String applicationName = appname;
            String appID = appid;
            String params = parameters;
            NetworkClassLoader cloader = loader;
            Object mainClass = null;
            try {
                System.out.println("Before loading __Main class\r\n");
                Class cl = cloader.loadClass("__Main");
                System.out.println("After loading __Main class\r\n");
                SystemDesktop desktopframe = new SystemDesktop();
                Constructor con = cl.getConstructor(new Class[] { si.getClass(), applicationName.getClass(), appID.getClass(), params.getClass(), helper.getClass(), desktopframe.getClass(), model.getClass() });
                mainClass = con.newInstance(new Object[] { si, applicationName, appID, params, helper, desktopframe, model });
                boolean eventsRequested = false;
                try {
                    Method method = mainClass.getClass().getMethod("frameworkEvents", null);
                    Object result = method.invoke(mainClass, null);
                    eventsRequested = ((Boolean) result).booleanValue();
                } catch (java.lang.NoSuchMethodException e) {
                    System.out.println("Warning: Method frameworkEvents not found.\r\n");
                }
                try {
                    Method method = mainClass.getClass().getMethod("initApplication", null);
                    method.invoke(mainClass, null);
                } catch (java.lang.NoSuchMethodException e) {
                    System.out.println("Warning: Method initApplication not found.\r\n");
                }
                for (Enumeration e = applications.elements(); e.hasMoreElements(); ) {
                    Object app = e.nextElement();
                    Entry entry = (Entry) app;
                    if (entry.frameworkEvents) {
                        Object[] args = new Object[1];
                        args[0] = new String(applicationName);
                        InvokeMethod(entry.object, "applicationLoadingFinished", args);
                    }
                }
                Entry entry = new Entry(mainClass, eventsRequested);
                String id = applicationName + " " + appID;
                System.out.println("#### New entry added to applications-list: " + id + "\r\n");
                applications.put(id, entry);
            } catch (Throwable throwable) {
                System.out.println("AsyncLoader exception " + throwable + "\r\n");
                throwable.printStackTrace();
            }
            ;
        }
