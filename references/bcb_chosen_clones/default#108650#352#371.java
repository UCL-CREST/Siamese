    public void importTaskList(String data, boolean append) throws Exception {
        HashMap<String, TaskCommand> importedTasks = new HashMap<String, TaskCommand>();
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        ByteArrayInputStream reader = new ByteArrayInputStream(data.toString().getBytes());
        Document doc = docBuilder.parse(reader);
        NodeList tasksNodes = doc.getElementsByTagName("task");
        for (int x = 0; x < tasksNodes.getLength(); x++) {
            Node item = tasksNodes.item(x);
            TaskCommand taskCommand = new TaskCommand(item);
            importedTasks.put(taskCommand.getName(), taskCommand);
        }
        if (append) {
            if (tasks == null) tasks = new HashMap<String, TaskCommand>();
            tasks.putAll(importedTasks);
        } else {
            tasks = importedTasks;
        }
        saveTaskList(null);
    }
