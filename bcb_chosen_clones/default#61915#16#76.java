    public static void main(String[] args) {
        if (System.getProperty("TTOOL_HOME") == null) {
            System.setProperty("TTOOL_HOME", System.getProperty("user.dir"));
        }
        Vector<URL> v = new Vector<URL>();
        try {
            load_Folder(new File(System.getProperty("TTOOL_HOME") + "/lib"), v);
            URL[] urls = new URL[v.size()];
            v.toArray(urls);
            URLClassLoader urlClassLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
            Thread.currentThread().setContextClassLoader(urlClassLoader);
            if (args.length == 0) {
                Thread th = new Thread((Runnable) urlClassLoader.loadClass("com.ravi.util.MainThread").newInstance());
                th.setContextClassLoader(urlClassLoader);
                th.start();
            } else if (args[0].equalsIgnoreCase("run")) {
                Class cls = urlClassLoader.loadClass("com.ravi.actions.RunTestScriptCommand");
                Method begin = cls.getDeclaredMethod("main", String[].class);
                String[] argg = new String[args.length - 1];
                System.arraycopy(args, 1, argg, 0, args.length - 1);
                begin.invoke(null, new Object[] { argg });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (args.length == 0) {
                    ServerSocket ss = new ServerSocket(12345);
                    while (keeprunning && args.length == 0) {
                        Socket s = ss.accept();
                        BufferedReader is = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        String cmd = is.readLine();
                        ObjectOutputStream oo = new ObjectOutputStream(s.getOutputStream());
                        System.out.println("Receive command from port 12345 :" + cmd);
                        if (cmd.equalsIgnoreCase("reboot")) {
                            System.out.println("exit with code 99 from " + s.getRemoteSocketAddress());
                            oo.writeBytes("exit with code 99");
                            System.exit(99);
                        } else if (cmd.equalsIgnoreCase("update")) {
                            System.out.println("exit with code 98 from " + s.getRemoteSocketAddress());
                            oo.writeBytes("exit with code 98");
                            System.exit(98);
                        } else {
                            String rsp = "";
                            if (cmd.equalsIgnoreCase("help")) {
                                rsp = "\ncmd: reboot will exit with code 99" + "\ncmd: update will exit with code 98";
                            } else {
                                rsp = "\nnot supported cmd:" + cmd;
                            }
                            System.out.println(rsp);
                            oo.writeBytes(rsp);
                            oo.close();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(101);
            }
        }
    }
