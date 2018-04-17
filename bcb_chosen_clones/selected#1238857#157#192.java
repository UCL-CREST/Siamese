    public void testRenderRules() {
        try {
            MappingManager manager = new MappingManager();
            OWLOntologyManager omanager = OWLManager.createOWLOntologyManager();
            OWLOntology srcOntology;
            OWLOntology targetOntology;
            manager.loadMapping(rulesDoc.toURL());
            srcOntology = omanager.loadOntologyFromPhysicalURI(srcURI);
            targetOntology = omanager.loadOntologyFromPhysicalURI(targetURI);
            manager.setSourceOntology(srcOntology);
            manager.setTargetOntology(targetOntology);
            Graph srcGraph = manager.getSourceGraph();
            Graph targetGraph = manager.getTargetGraph();
            System.out.println("Starting to render...");
            FlexGraphViewFactory factory = new FlexGraphViewFactory();
            factory.setColorScheme(ColorSchemes.BLUES);
            factory.visit(srcGraph);
            GraphView view = factory.getGraphView();
            GraphViewRenderer renderer = new FlexGraphViewRenderer();
            renderer.setGraphView(view);
            System.out.println("View updated with graph...");
            InputStream xmlStream = renderer.renderGraphView();
            StringWriter writer = new StringWriter();
            IOUtils.copy(xmlStream, writer);
            System.out.println("Finished writing");
            writer.close();
            System.out.println("Finished render... XML is:");
            System.out.println(writer.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
    }
