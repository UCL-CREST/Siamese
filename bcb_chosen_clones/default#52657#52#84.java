    private void getNewTask() {
        Vector probableTasks = new Vector();
        Vector weights = new Vector();
        Class[] parametersForFindingMethod = { Person.class, VirtualMars.class };
        Object[] parametersForInvokingMethod = { person, mars };
        for (int x = 0; x < generalTasks.length; x++) {
            try {
                Method probability = generalTasks[x].getMethod("getProbability", parametersForFindingMethod);
                int weight = ((Integer) probability.invoke(null, parametersForInvokingMethod)).intValue();
                if (weight > 0) {
                    probableTasks.addElement(generalTasks[x]);
                    weights.addElement(new Integer(weight));
                }
            } catch (Exception e) {
                System.out.println("TaskManager.getNewTask() (1): " + e.toString());
            }
        }
        int totalWeight = 0;
        for (int x = 0; x < weights.size(); x++) totalWeight += ((Integer) weights.elementAt(x)).intValue();
        int r = (int) Math.round(Math.random() * (double) totalWeight);
        int tempWeight = ((Integer) weights.elementAt(0)).intValue();
        int taskNum = 0;
        while (tempWeight < r) {
            taskNum++;
            tempWeight += ((Integer) weights.elementAt(taskNum)).intValue();
        }
        try {
            Constructor construct = ((Class) probableTasks.elementAt(taskNum)).getConstructor(parametersForFindingMethod);
            currentTask = (Task) construct.newInstance(parametersForInvokingMethod);
        } catch (Exception e) {
            System.out.println("TaskManager.getNewTask() (2): " + e.toString());
        }
    }
