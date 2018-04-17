    @Test
    public void testWriteModel() {
        Model model = new Model();
        model.setName("MY_MODEL1");
        Stereotype st1 = new Stereotype();
        st1.setName("Pirulito1");
        PackageObject p1 = new PackageObject("p1");
        ClassType type1 = new ClassType("Class1");
        type1.setStereotype(st1);
        type1.addMethod(new Method("doSomething"));
        p1.add(type1);
        ClassType type2 = new ClassType("Class2");
        Method m2 = new Method("doSomethingElse");
        m2.setType(type1);
        type2.addMethod(m2);
        p1.add(type2);
        Generalization g = new Generalization();
        g.setSource(type1);
        g.setTarget(type1);
        p1.add(g);
        model.add(p1);
        ModelWriter writer = new ModelWriter();
        try {
            File modelFile = new File("target", "test.model");
            writer.write(model, modelFile);
            File xmlFile = new File("target", "test.xml");
            xmlFile.createNewFile();
            IOUtils.copy(new GZIPInputStream(new FileInputStream(modelFile)), new FileOutputStream(xmlFile));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            Assert.fail(e.getMessage());
        }
    }
