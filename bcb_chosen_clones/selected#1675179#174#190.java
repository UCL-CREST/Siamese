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
