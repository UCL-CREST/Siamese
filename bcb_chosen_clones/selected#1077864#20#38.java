    public static State getNodeState(Node node) {
        try {
            if (node != null) {
                String simpleName = node.getClass().getSimpleName();
                if (simpleName.indexOf("Node") != -1) {
                    simpleName = simpleName.substring(0, simpleName.indexOf("Node"));
                }
                Class<?> stateClass = Class.forName("cn.myapps.core.workflow.engine.state." + simpleName + "State");
                Constructor<?> constructor = stateClass.getConstructor(Node.class);
                State state = (State) constructor.newInstance(node);
                return state;
            } else {
                return new NullState(null);
            }
        } catch (Exception e) {
            LOG.warn("getNodeState", e);
            return new NullState(node);
        }
    }
