    private void launch(String[] classNames) throws Exception {
        appletCount = classNames.length;
        StringBuffer html = new StringBuffer();
        for (int i = 0; i < classNames.length; i++) {
            html.append("<html><applet codebase=\"build/classes\" code=\"");
            html.append(classNames[i]);
            html.append("\" width=\"100\" height=\"100\"></applet>");
        }
        html.append("</html>");
        File dir = new File(System.getProperty("user.dir"));
        htmlFile = File.createTempFile(getName(), ".html", dir);
        htmlFile.deleteOnExit();
        FileOutputStream os = new FileOutputStream(htmlFile);
        os.write(html.toString().getBytes());
        os.close();
        final AppClassLoader cl = new AppClassLoader(".");
        oldsm = System.getSecurityManager();
        String tgname = getName() + " Thread Group for catching exit";
        final ThreadGroup group = new ThreadGroup(tgname) {

            public void uncaughtException(Thread t, Throwable thrown) {
                if (!(thrown instanceof ExitException) && !(thrown instanceof ThreadDeath)) Log.warn(thrown);
            }
        };
        Thread thread = new Thread(group, "AppletViewer Launcher") {

            public void run() {
                try {
                    Class cls = Class.forName("abbot.script.AppletSecurityManager", true, cl);
                    Constructor ctor = cls.getConstructor(new Class[] { SecurityManager.class });
                    SecurityManager asm = (SecurityManager) ctor.newInstance(new Object[] { new NoExitSecurityManager() {

                        protected void exitCalled(int status) {
                            ++exitCalled;
                        }
                    } });
                    cls = Class.forName("sun.applet.Main", true, cl);
                    System.setSecurityManager(asm);
                    cls.getMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { new String[] { htmlFile.getName() } });
                } catch (Exception e) {
                    Log.warn(e);
                }
            }
        };
        thread.setContextClassLoader(cl);
        thread.start();
        getWindowTracker();
        Timer timer = new Timer();
        while (appletStartCount < appletCount) {
            if (timer.elapsed() > 60000) {
                fail("AppletViewer failed to launch");
            }
            robot.sleep();
        }
    }
