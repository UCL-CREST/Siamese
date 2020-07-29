    public static void main(String[] args) {
        RBE rbe = new RBE();
        EBTestFactory ebtf = new EBTestFactory();
        int i, a, num;
        int maxState = 0;
        Vector ebs = new Vector(0);
        System.out.println("Remote Browser Emulator for TPC-W.");
        System.out.println("  ECE 902  Fall '99");
        System.out.println("  Version 1.5");
        ArgDB db = new ArgDB();
        startTime = new Date();
        EBFactoryArg ebfArg = new EBFactoryArg("-EB", "EB Factory", "% Factory class used to create EBs.  " + "<class> <#> <factory args...>.", rbe, ebs, db);
        PrintStreamArg oFile = new PrintStreamArg("-OUT", "Output file", "% Name of matlab .m output file for results.", db);
        DateArg st = new DateArg("-ST", "Starting time for ramp-up", "% Time (such as Nov 2, 1999 11:30:00 AM CST) " + "at which to start ramp-up." + "  Useful for synchronizing multiple RBEs.", System.currentTimeMillis() + 2000L, db);
        IntArg ru = new IntArg("-RU", "Ramp-up time", "% Seconds used to warm-up the simulator.", 10 * 60, db);
        IntArg mi = new IntArg("-MI", "Measurement interval", "% Seconds used for measuring SUT performance.", 30 * 60, db);
        IntArg rd = new IntArg("-RD", "Ramp-down time", "% Seconds of steady-state operation following " + "measurment interval.", 5 * 60, db);
        DoubleArg slow = new DoubleArg("-SLOW", "Slow-down factor", "% 1000 means one thousand real seconds equals one " + "simulated second.  " + "Accepts factional values and E notation.", 1.0, db);
        DoubleArg tt_scale = new DoubleArg("-TT", "Think time multiplication.", "% Used to increase (>1.0) or decrease (<1.0) think time.  " + "In addition to slow-down factor.", 1.0, db);
        BooleanArg key = new BooleanArg("-KEY", "Interactive control.", "% Require user to hit RETURN before every interaction.  Overrides think time.", false, db);
        BooleanArg getImage = new BooleanArg("-GETIM", "Request images.", "% True will cause RBE to request images.  False suppresses image requests.", true, db);
        IntArg img = new IntArg("-CON", "Image connections", "% Maximum number of images downloaded at once.", 4, db);
        IntArg cust = new IntArg("-CUST", "Number of customers", "% Number of customers in the database.  " + "Used to generated random CIDs.", 1000, db);
        IntArg custa = new IntArg("-CUSTA", "CID NURand A", "% Used to generate random CIDs.  " + "See TPC-W Spec. Clause 2.3.2.  " + "-1 means use TPC-W spec. value.", -1, db);
        IntArg item = new IntArg("-ITEM", "Number of items", "% Number of items in the database. " + "Used to generate random searches.", 10000, db);
        IntArg itema = new IntArg("-ITEMA", "Item NURand A", "% Used to generate random searches.  " + "See TPC-W Spec. Clause 2.10.5.1.  " + "-1 means use TPC-W spec. value.", -1, db);
        IntArg debug = new IntArg("-DEBUG", "Debug message.", "% Increase this to see more debug messages ~1 to 10.", 0, db);
        IntArg maxErr = new IntArg("-MAXERROR", "Maximum errors allowed.", "% RBE will terminate after this many errors.  0 implies no limit.", 1, db);
        StringArg wwwArg = new StringArg("-WWW", "Base URL", "% The root URL for the TPC-W pages.", RBE.www1, db);
        BooleanArg monArg = new BooleanArg("-MONITOR", "Do utilization monitoring", "% TRUE=do monitoring, FALSE=Don't do monitoring", false, db);
        BooleanArg incrArg = new BooleanArg("-INCREMENTAL", "Start EBs Incrementally", "% TRUE=do them in increments, FALSE=Do them all at once", false, db);
        BooleanArg print = new BooleanArg("-PRINT", "Out to console COntrolor.", "% use this to decide whether to print the cancel informations!.", false, db);
        if (args.length == 0) {
            Usage(args, db);
            return;
        }
        try {
            db.parse(args);
        } catch (Arg.Exception ae) {
            System.out.println("Error:");
            System.out.println(ae);
            Usage(args, db);
            return;
        }
        Usage(args, db);
        rbe.maxImageRd = img.num;
        rbe.numCustomer = cust.num;
        rbe.cidA = custa.num;
        rbe.numItem = item.num;
        rbe.numItemA = itema.num;
        RBE.www1 = wwwArg.s;
        RBE.getImage = getImage.flag;
        RBE.monitor = monArg.flag;
        RBE.incremental = incrArg.flag;
        RBE.setURLs();
        EB.DEBUG = debug.num;
        EBStats.maxError = maxErr.num;
        RBE.PrintControl = print.flag;
        if (rbe.numCustomer < 1) {
            System.out.println("Number of customers (" + rbe.numCustomer + ")  must be >= 1.");
            return;
        }
        if (rbe.numCustomer > stdCIDA[stdCIDA.length - 1][1]) {
            System.out.println("Number of customers (" + rbe.numCustomer + ")  must be <= " + stdCIDA[stdCIDA.length - 1][1] + ".");
            return;
        }
        if (rbe.cidA == -1) {
            for (i = 0; i < stdCIDA.length; i++) {
                if ((rbe.numCustomer >= stdCIDA[i][0]) && (rbe.numCustomer <= stdCIDA[i][1])) {
                    rbe.cidA = stdCIDA[i][2];
                    System.out.println("Choose " + rbe.cidA + " for -CUSTA.");
                    break;
                }
            }
        }
        for (i = 0; i < stdNumItemA.length; i++) {
            if (rbe.numItem == stdNumItemA[i][0]) break;
        }
        if (i == stdNumItemA.length) {
            System.out.println("Number of items (" + rbe.numItem + ") must be one of ");
            for (i = 0; i < stdNumItemA.length; i++) {
                System.out.println("    " + stdNumItemA[i][0]);
            }
            return;
        }
        if (rbe.numItemA == -1) {
            rbe.numItemA = stdNumItemA[i][1];
            System.out.println("Choose " + rbe.numItemA + " for -ITEMA.");
        }
        if (rbe.maxImageRd < 1) {
            System.out.println("-CON must be >= 1.");
            return;
        }
        long start = st.d.getTime();
        long addRU = start - System.currentTimeMillis();
        if (addRU < 0L) {
            System.out.println("Warning: start time " + (((double) addRU) / 1000.0) + " seconds before current time.\n" + "Resetting to current time.");
            start = System.currentTimeMillis();
        }
        rbe.slowDown = slow.num;
        rbe.speedUp = 1 / rbe.slowDown;
        rbe.stats = new EBStats(rbe, 60000, 50, 75000, 100, ebfArg.maxState, start, 1000L * ru.num, 1000L * mi.num, 1000L * rd.num);
        String pidStr = null;
        if (monitor) {
            try {
                int j;
                char c[] = new char[10];
                Runtime r = Runtime.getRuntime();
                Process proc = r.exec("/usr/local/bin/monitor -log /bigvol2/monitor_traces/java.mon -interval 10 -sample 10 -L -Toplog -F");
                InputStreamReader reader = new InputStreamReader(proc.getInputStream());
                reader.skip(40);
                reader.read(c, 0, 10);
                for (j = 0; c[j] != '\n'; j++) ;
                pidStr = new String(c, 0, j);
                System.out.println("Pid = " + pidStr);
            } catch (java.lang.Exception ex) {
                System.out.println("Unable to monitor process");
            }
        }
        for (i = 0; i < ebs.size(); i++) {
            EB e = (EB) ebs.elementAt(i);
            e.initialize();
            e.tt_scale = tt_scale.num;
            e.waitKey = key.flag;
            e.setName("TPC-W Emulated Broswer " + (i + 1));
            e.setDaemon(true);
        }
        System.out.println("Starting " + ebs.size() + " EBs.");
        for (i = 0; i < ebs.size(); i++) {
            EB e = (EB) ebs.elementAt(i);
            try {
                if (RBE.incremental && ((i + 1) % 10 == 0)) {
                    System.out.println(i + " EBs Alive!");
                    Thread.currentThread().sleep(10000L);
                }
            } catch (java.lang.Exception ex) {
                System.out.println("Unable to sleep");
            }
            e.start();
        }
        System.out.println("All of the EBs are alive!");
        rbe.stats.waitForRampDown();
        if (monitor) {
            try {
                Runtime r = Runtime.getRuntime();
                Process proc = r.exec("/bin/kill " + pidStr);
            } catch (java.lang.Exception ex) {
                System.out.println("Unable to destroy monitor process");
            }
        }
        System.out.println("Terminating EBs...");
        EB.terminate = true;
        try {
            Thread.currentThread().sleep(10000L);
        } catch (InterruptedException ie) {
            System.out.println("Unable to sleep!");
        }
        for (i = 0; i < ebs.size(); i++) {
            EB e = (EB) ebs.elementAt(i);
            System.out.println(e + ": alive: " + e.isAlive());
        }
        for (i = 0; i < ebs.size(); i++) {
            EB e = (EB) ebs.elementAt(i);
            System.out.println("main thread: About to interrupt: " + i);
            e.interrupt();
        }
        System.out.println("EBs finished.");
        if (!oFile.set()) {
            oFile.s = System.out;
        }
        oFile.s.println("% Start time: " + startTime);
        oFile.s.println("% System under test: " + www);
        Date endTime = new Date();
        oFile.s.println("% End time: " + endTime);
        oFile.s.println("% Transaction Mix: " + ebfArg.className);
        db.print(oFile.s);
        rbe.stats.print(oFile.s);
        oFile.s.close();
        System.out.println("Really finishing RBE!.");
        System.out.println("During test,had Finished " + CompleteSession + "'s Sessions ");
        System.out.println("During test,there are  " + errorSession + "'s  Sessions canceled! ");
    }
