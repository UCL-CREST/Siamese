    public static void main(String[] args) throws Exception {
        System.out.print("Starting DBMS...");
        db = new DBDerby("c:\\tmp");
        db.initialize();
        System.out.println("   ok");
        System.out.print("Parsing XML & creating DOM...");
        ContainerHWC ctnr = new ContainerHWC();
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(uri);
        System.out.println("   ok");
        System.out.print("Initializing from DOM...");
        ctnr.initFromDOM(doc.getChildNodes().item(0));
        System.out.println("   ok");
        System.out.print("Assigning database...");
        ctnr.getMyNetwork().setDatabase(db);
        System.out.println("   ok");
        System.out.print("Getting new simulation number...");
        db.newSimulationNumber(ctnr.getMyNetwork(), "Second test for DB...");
        System.out.println("   ok");
        System.out.print("Validating the configuration...");
        if (ctnr.getMyNetwork().validate()) System.out.println("   ok"); else {
            System.out.println("   failed");
            return;
        }
        ctnr.getMyNetwork().setDatabase(null);
        System.out.print("Running simulation...");
        for (int ts = 1; ts < 116; ts++) {
            if (!ctnr.getMyNetwork().dataUpdate(ts)) {
                System.out.println("   failed");
                return;
            }
        }
        System.out.println("   ok");
        ctnr.getMyNetwork().setDatabase(db);
        System.out.print("Saving simulation...");
        db.saveSimulation(ctnr, null);
        System.out.println("   ok");
        System.out.print("Dumping XML...");
        PrintStream os = new PrintStream(new FileOutputStream(fname));
        ctnr.xmlDump(os);
        os.close();
        System.out.println("   ok");
    }
