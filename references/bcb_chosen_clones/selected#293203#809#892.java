    private PlanStrategy loadStrategy(final String name, final StrategyConfigGroup.StrategySettings settings) {
        Network network = this.network;
        PersonalizableTravelCost travelCostCalc = this.travelCostCalculatorFactory.createTravelCostCalculator(this.travelTimeCalculator, this.config.charyparNagelScoring());
        PersonalizableTravelTime travelTimeCalc = this.travelTimeCalculator;
        Config config = this.config;
        PlanStrategy strategy = null;
        if (name.equals("KeepLastSelected")) {
            strategy = new PlanStrategy(new KeepSelected());
        } else if (name.equals("ReRoute") || name.equals("threaded.ReRoute")) {
            strategy = new PlanStrategy(new RandomPlanSelector());
            strategy.addStrategyModule(new ReRoute(this));
        } else if (name.equals("ReRoute_Dijkstra")) {
            strategy = new PlanStrategy(new RandomPlanSelector());
            strategy.addStrategyModule(new ReRouteDijkstra(config, network, travelCostCalc, travelTimeCalc));
        } else if (name.equals("ReRoute_Landmarks")) {
            strategy = new PlanStrategy(new RandomPlanSelector());
            strategy.addStrategyModule(new ReRouteLandmarks(config, network, travelCostCalc, travelTimeCalc, new FreespeedTravelTimeCost(config.charyparNagelScoring())));
        } else if (name.equals("TimeAllocationMutator") || name.equals("threaded.TimeAllocationMutator")) {
            strategy = new PlanStrategy(new RandomPlanSelector());
            TimeAllocationMutator tam = new TimeAllocationMutator(config);
            strategy.addStrategyModule(tam);
        } else if (name.equals("TimeAllocationMutator7200_ReRouteLandmarks")) {
            strategy = new PlanStrategy(new RandomPlanSelector());
            strategy.addStrategyModule(new TimeAllocationMutator(config, 7200));
            strategy.addStrategyModule(new ReRouteLandmarks(config, network, travelCostCalc, travelTimeCalc, new FreespeedTravelTimeCost(config.charyparNagelScoring())));
        } else if (name.equals("ExternalModule")) {
            externalCounter++;
            strategy = new PlanStrategy(new RandomPlanSelector());
            String exePath = settings.getExePath();
            ExternalModule em = new ExternalModule(exePath, "ext" + externalCounter, controlerIO, getScenario(), 1);
            em.setIterationNumber(getIterationNumber());
            strategy.addStrategyModule(em);
        } else if (name.equals("BestScore")) {
            strategy = new PlanStrategy(new BestPlanSelector());
        } else if (name.equals("SelectExpBeta")) {
            strategy = new PlanStrategy(new ExpBetaPlanSelector(config.charyparNagelScoring()));
        } else if (name.equals("ChangeExpBeta")) {
            strategy = new PlanStrategy(new ExpBetaPlanChanger(config.charyparNagelScoring().getBrainExpBeta()));
        } else if (name.equals("SelectRandom")) {
            strategy = new PlanStrategy(new RandomPlanSelector());
        } else if (name.equals("ChangeLegMode")) {
            strategy = new PlanStrategy(new RandomPlanSelector());
            strategy.addStrategyModule(new ChangeLegMode(config));
            strategy.addStrategyModule(new ReRoute(this));
        } else {
            if (name.startsWith("org.matsim")) {
                log.error("Strategies in the org.matsim package must not be loaded by name!");
            } else {
                try {
                    Class<? extends PlanStrategy> klas = (Class<? extends PlanStrategy>) Class.forName(name);
                    Class[] args = new Class[1];
                    args[0] = Scenario.class;
                    Constructor<? extends PlanStrategy> c = null;
                    try {
                        c = klas.getConstructor(args);
                        strategy = c.newInstance(getScenario());
                    } catch (NoSuchMethodException e) {
                        log.warn("Cannot find Constructor in PlanStrategy " + name + " with single argument of type Scenario. " + "This is not fatal, trying to find other constructor, however a constructor expecting Scenario as " + "single argument is recommented!");
                    }
                    if (c == null) {
                        args[0] = MZControler.class;
                        c = klas.getConstructor(args);
                        strategy = c.newInstance(this);
                    }
                    log.info("Loaded PlanStrategy from class " + name);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return strategy;
    }
