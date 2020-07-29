    private HashMap<Integer, InstructionalTactic> collectTacticsFromGraph() {
        HashMap<Integer, InstructionalTactic> tacts = new HashMap<Integer, InstructionalTactic>();
        for (Class<? extends InstructionalTactic> tactCl : getSpec().getTacticClasses()) {
            Collection<VesselNode> tactNodes = null;
            try {
                tactNodes = GraphSearcher.findNodes(new SignatureMatcher(IMConstants.GROUP_SIGNATURE), getStrategyGroupNode().getGraph());
            } catch (Exception e) {
                logger.error("couldn't get group signature of " + tactCl, e);
            }
            if (tactNodes != null) {
                for (VesselNode node : tactNodes) {
                    InstructionalTactic newTactic;
                    try {
                        newTactic = tactCl.getConstructor(GroupNode.class, Integer.class).newInstance(node, workspaceId);
                        newTactic.setInstructionalStrategy(this);
                        tacts.put(newTactic.getId(), newTactic);
                    } catch (Exception e) {
                        logger.error("couldn't construct " + tactCl, e);
                    }
                }
            }
        }
        return tacts;
    }
