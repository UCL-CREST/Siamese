    private void run(String[] args) throws Throwable {
        ArgParser parser = new ArgParser("Run an experiment");
        parser.addOptions(this, true);
        args = parser.matchAllArgs(args, 0, ArgParserOption.EXIT_ON_ERROR, ArgParserOption.STOP_FIRST_UNMATCHED);
        if (log4jFile != null) {
            logger.info("Using another log4j configuration: %s", log4jFile);
            PropertyConfigurator.configure(log4jFile.getAbsolutePath());
        }
        final TreeMap<TaskName, Class<Task>> tasks = GenericHelper.newTreeMap();
        final Enumeration<URL> e = About.class.getClassLoader().getResources(EXPERIMENT_PACKAGES);
        while (e.hasMoreElements()) {
            final URL url = e.nextElement();
            logger.debug("Got URL %s", url);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = in.readLine()) != null) {
                String packageName = line;
                getTasks(url, tasks, packageName);
            }
        }
        getTasks(null, tasks, getClass().getPackage().getName());
        if (tasks.isEmpty()) {
            logger.fatal("I did not find any valid experiment (service bpiwowar.experiments.ExperimentListProvider)");
            System.exit(1);
        }
        if (args.length == 0 || args[0].equals("list")) {
            System.out.format("Available experiments:%n");
            TreeMapArray<PackageName, String> map = TreeMapArray.newInstance();
            for (Entry<TaskName, Class<Task>> entry : tasks.entrySet()) {
                TaskName task = entry.getKey();
                if (showClassNames) map.add(task.packageName, String.format("%s (%s)", task.name, entry.getValue().toString())); else map.add(task.packageName, task.name);
            }
            Stack<PackageName> ancestors = new Stack<PackageName>();
            for (Entry<PackageName, ArrayList<String>> entry : map.entrySet()) {
                final PackageName key = entry.getKey();
                while (!ancestors.isEmpty() && key.commonPrefixLength(ancestors.peek()) != ancestors.peek().getLength()) ancestors.pop();
                int nbAncestors = ancestors.size();
                int c = nbAncestors > 0 ? ancestors.peek().getLength() : 0;
                StringBuilder s = new StringBuilder();
                for (int i = 0; i < c; i++) s.append("|");
                for (int i = c; i < key.getLength(); i++) {
                    s.append("|");
                    ancestors.add(new PackageName(key, i + 1));
                    System.out.format("%s%n", s);
                    System.out.format("%s+ [%s]%n", s, ancestors.peek());
                    nbAncestors++;
                }
                String prefix = s.toString();
                for (String task : entry.getValue()) System.out.format("%s|- %s%n", prefix, task);
                ancestors.add(key);
            }
            return;
        } else if (args[0].equals(SEARCH_COMMAND)) {
            final class Options {

                @OrderedArgument(required = true)
                String search;
            }
            Options options = new Options();
            ArgParser ap = new ArgParser(SEARCH_COMMAND);
            ap.addOptions(options);
            ap.matchAllArgs(args, 1);
            logger.info("Searching for %s", options.search);
            for (Entry<TaskName, Class<Task>> entry : tasks.entrySet()) {
                TaskName taskname = entry.getKey();
                if (taskname.name.contains(options.search)) {
                    System.err.format("[*] %s - %s%n   %s%n", taskname, entry.getValue(), entry.getValue().getAnnotation(TaskDescription.class).description());
                }
            }
            return;
        }
        String taskName = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);
        ArrayList<Class<Task>> matching = GenericHelper.newArrayList();
        for (Entry<TaskName, Class<Task>> entry : tasks.entrySet()) {
            if (entry.getKey().name.equals(taskName)) matching.add(entry.getValue());
        }
        if (matching.isEmpty()) {
            System.err.println("No task match " + taskName);
            System.exit(1);
        }
        if (matching.size() > 1) {
            System.err.println("Too many tasks match " + taskName);
            System.exit(1);
        }
        Class<Task> taskClass = matching.get(0);
        logger.info("Running experiment " + taskClass.getCanonicalName());
        Task task = taskClass.newInstance();
        int errorCode = 0;
        try {
            task.init(args);
            if (xstreamOutput != null) {
                OutputStream out;
                if (xstreamOutput.toString().equals("-")) out = System.out; else out = new FileOutputStream(xstreamOutput);
                logger.info("Serializing the object into %s", xstreamOutput);
                new XStream().toXML(task, out);
                out.close();
            } else {
                errorCode = task.run();
            }
            logger.info("Finished task");
        } catch (Throwable t) {
            if (t instanceof InvocationTargetException && t.getCause() != null) {
                t = t.getCause();
            }
            logger.error("Exception thrown while executing the action:%n%s%n", t);
            errorCode = 2;
        }
        System.exit(errorCode);
    }
