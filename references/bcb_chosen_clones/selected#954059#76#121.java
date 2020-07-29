    public void run() {
        AlgorithmController controller = AlgorithmController.getInstance();
        Class algorithm = ((Class) controller.algorithms.get(algName));
        Constructor[] constructors = algorithm.getConstructors();
        Object[] parameters;
        int pCount = constructors[0].getParameterTypes().length;
        parameters = new Object[pCount];
        parameters[0] = g;
        endNodeSelection = false;
        TSProblemModel tModel = TSProblemModel.getInstance();
        tModel.reset();
        workspace.setListener(this);
        workspace.setEditable(true);
        workspace.requestNode(true);
        try {
            while (selectedNode == -1) Thread.sleep(500);
        } catch (InterruptedException exc) {
        }
        tModel.setSourceNodeIndex(selectedNode);
        ColorUtil.setSourceNodeColor(g, selectedNode);
        workspace.refresh();
        selectedNode = -1;
        while (!endNodeSelection) {
            workspace.requestNode(false);
            try {
                while (selectedNode == -1 && !endNodeSelection) Thread.sleep(500);
            } catch (InterruptedException exc) {
            }
            tModel.addClient(selectedNode);
            ColorUtil.setClientNodeColor(g, selectedNode);
            workspace.refresh();
            selectedNode = -1;
        }
        workspace.setEditable(false);
        try {
            Object instance = constructors[0].newInstance(parameters);
            fireAlgorithmStarted((Algorithm) instance);
            AlgorithmStarter starter = AlgorithmStarter.getInstance();
            starter.start((Algorithm) instance);
            fireAlgorithmFinished((Algorithm) instance);
            workspace.refresh();
        } catch (final Exception exc) {
            exc.printStackTrace();
            algorithmListener.exceptionRaised(exc);
        }
    }
