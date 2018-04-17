    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Syntax: BehavioralLauncher <appclass> [plus args]");
            System.exit(1);
        }
        SimulationThread behClkThread = null;
        DVApplication app = null;
        try {
            Class appCls = null;
            try {
                appCls = Class.forName(args[0]);
            } catch (UnsupportedClassVersionError unsupportedClassVersionError) {
                System.err.println("Could not load class" + args[0]);
                throw unsupportedClassVersionError;
            }
            assert (DVApplication.class.isAssignableFrom(appCls));
            final BehavioralSimulation sim = new BehavioralSimulation(Arrays.asList(args));
            final PRNGFactory rngFactory = PRNGFactoryFactory.getDefaultFactory();
            final SimulationManager simManager = new SimulationManager("BehavioralSimulation", rngFactory, rngFactory.newInstance(0));
            final OVAEngine ovaEngine = null;
            final DVSimulation dvSim = new DVSimulation(sim, simManager, ovaEngine);
            final String clockName = "DefaultClock";
            sim.createRegister(clockName, 1);
            behClkThread = dvSim.fork(clockName, new BehavioralClockGenerator(dvSim, clockName, 100));
            final Constructor<?> appCtor = appCls.getConstructor(DVSimulation.class);
            app = (DVApplication) appCtor.newInstance(dvSim);
            app.start();
            sim.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (behClkThread != null) {
                behClkThread.terminate();
            }
            if (app != null) {
                app.finish();
            }
        }
    }
