    private void loadTaskList() {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(this.getProperty("path.data") + File.separator + "Tasks.xml"));
            NodeList tasksNodes = doc.getElementsByTagName("task");
            tasks = new HashMap<String, TaskCommand>();
            for (int x = 0; x < tasksNodes.getLength(); x++) {
                Node item = tasksNodes.item(x);
                TaskCommand taskCommand = new TaskCommand(item);
                tasks.put(taskCommand.getName(), taskCommand);
            }
            System.out.println("Tasks.xml found and loaded (" + tasksNodes.getLength() + ")");
        } catch (Exception e) {
            tasks = new HashMap<String, TaskCommand>();
            System.out.println("Error loading Tasks.xml, starting with no tasks.");
        }
    }
