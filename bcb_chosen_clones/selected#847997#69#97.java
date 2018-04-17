    @Test
    public void behaveTest() {
        InputStream is = this.getClass().getResourceAsStream("safetyCaseTest.mdf");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            IOUtils.copy(is, out);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read mdf", e);
        }
        TrafficSimulationEngine engine = new TrafficSimulationEngine();
        TrafficModelDefinition def = MDFReader.read(out.toByteArray());
        engine.init(def);
        Map<Integer, Set<Integer>> linkSegments = new HashMap<Integer, Set<Integer>>();
        Set<Integer> segments = new HashSet<Integer>();
        segments.add(0);
        linkSegments.put(0, segments);
        FrameProperties frameProperties = new FrameProperties(linkSegments, new HashSet<Integer>());
        engine.setFrameProperties(frameProperties);
        RegularVehicle vehicle = (RegularVehicle) engine.getDynamicObjects().iterator().next();
        CompositeDriver driver = (CompositeDriver) vehicle.getDriver();
        driver.drive(0.1f);
        SafetyCase safety = new SafetyCase(driver);
        RectangleCCRange ccRange = (RectangleCCRange) safety.behave(0.1f);
        HandRange turnRange = ccRange.getTurnRange();
        HandRange probeRange = new HandRange();
        probeRange.remove(Hand.Left);
        assertTrue(turnRange.equals(probeRange));
        assertTrue(ccRange.getPriority() == Priority.SafetyCase);
    }
