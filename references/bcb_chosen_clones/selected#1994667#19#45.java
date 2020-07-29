    public static void main(String[] args) {
        Discoverer discoverer = new ClasspathDiscoverer();
        CLIAnnotationDiscovereryListener discoveryListener = new CLIAnnotationDiscovereryListener(new String[] { CLIEntry.class.getName() });
        discoverer.addAnnotationListener(discoveryListener);
        discoverer.discover();
        if (discoveryListener.getDiscoveredClasses().isEmpty()) {
            Console.severe("Startup failed: Could not find CLIEntry.");
            System.exit(1);
        }
        String cliEntryClassName = discoveryListener.getDiscoveredClasses().get(0);
        Console.superFine("Loading CLIEntry [" + cliEntryClassName + "].");
        try {
            Class<?> cliEntryClass = Class.forName(cliEntryClassName);
            if (!CommandLineApplication.class.isAssignableFrom(cliEntryClass)) {
                Console.severe("CLIEntry [" + cliEntryClassName + "] is not of type CommandLineApplication.");
                System.exit(1);
            }
            Constructor<?> constructor = cliEntryClass.getConstructor();
            @SuppressWarnings("unchecked") CommandLineApplication<? extends CLIContext> cla = (CommandLineApplication<? extends CLIContext>) constructor.newInstance();
            cla.start();
        } catch (ClassNotFoundException e) {
            Console.severe("Unable to find CLIEntry class [" + cliEntryClassName + "].");
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
