    public static void main(String[] args) {
        int trafficPort = DEFAULT_TRAFFIC_PORT;
        if (args.length < 2) {
            String narrator = "Syntax is 'SingleServerWrapper ServerClass ServerBindingName [-p Port] [Params]' where ";
            narrator += "ServerClass is the class name of your interactive task server class to ";
            narrator += "be wrapping around (must have no argument constructor).";
            narrator += "\nServerBindingName is the RMI binding name you wish ";
            narrator += "to use, which is the name clients will refer to this server by.";
            narrator += "\nPort is the port where all RMI traffic should go through.";
            narrator += "\nParams are extra command line arguments to supply to the ServerClass constructor.";
            narrator += "\nEg. java -Djava.rmi.server.codebase=http://139.184.166.27:2011/ -Djava.rmi.server.hostname=139.184.166.27 -Djava.security.policy=java.policy distrit.server.SingleServerWrapper package.MyITSServer KingKong bog jog -p 1098";
            narrator += "\nThe example will construct server class package.MyITSServer with an ArrayList containing Strings bog and jog.  It will then bind it as KingKong and use port 1098 for all RMI traffic.";
            System.out.println("" + narrator);
            System.exit(0);
        }
        try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            Class inITSClass = classLoader.loadClass(args[0]);
            ArrayList extraArgs = new ArrayList();
            for (int al = 2; al < args.length; al++) {
                if (args[al].equals("-p")) {
                    trafficPort = Integer.parseInt(args[++al]);
                } else {
                    extraArgs.add(args[al]);
                }
            }
            InteractiveTaskServer inITS;
            if (extraArgs.size() == 0) {
                inITS = (InteractiveTaskServer) inITSClass.newInstance();
            } else {
                Class[] parameterTypes = { extraArgs.getClass() };
                Constructor inITSConstructor = inITSClass.getConstructor(parameterTypes);
                Object[] parameters = { extraArgs };
                inITS = (InteractiveTaskServer) inITSConstructor.newInstance(parameters);
            }
            System.out.println("Succesfully loaded InteractiveTaskServer " + inITS);
            try {
                SingleServerWrapper ssw = new SingleServerWrapper(inITS, args[1], trafficPort);
                ssw.bindServer();
            } catch (RemoteException rem) {
                handleException(rem);
            } catch (java.net.MalformedURLException e) {
                handleException(e);
            } catch (Exception e) {
                System.out.println("HANDLING GENERIC EXCEPTION!!!! WHAT IS IT????");
                handleException(e);
            }
        } catch (ClassNotFoundException cnfe) {
            handleException(cnfe);
        } catch (InstantiationException ine) {
            handleException(ine);
        } catch (IllegalAccessException iae) {
            handleException(iae);
        } catch (NoSuchMethodException e) {
            handleException(e);
        } catch (java.lang.reflect.InvocationTargetException e) {
            handleException(e);
        }
    }
