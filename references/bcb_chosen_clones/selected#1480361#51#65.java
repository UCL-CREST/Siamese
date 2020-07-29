    private String getOntologyURI(String name, String ontology) {
        String onto_url;
        try {
            ClassLoader cl = this.getClass().getClassLoader();
            String class_name = this.getClass().getPackage().getName() + "." + ontology + "Handler";
            Class<?> ontology_handler_class = (Class<?>) cl.loadClass(class_name);
            Constructor<?> constructor = ontology_handler_class.getConstructor(new Class[0]);
            OntologyHandler handler = (OntologyHandler) constructor.newInstance();
            onto_url = handler.getURL(name);
        } catch (Throwable t) {
            t.printStackTrace(System.err);
            onto_url = "unknown";
        }
        return onto_url;
    }
