    public CGCanvasTool loadTool(String toolName) throws Exception {
        CGCanvasTool tool = null;
        String toolClassName = getString(toolName + ".class");
        Class toolClass = Class.forName(toolClassName);
        Constructor toolConstructor = toolClass.getConstructor(new Class[] { CGPalette.class });
        Object[] args = new Object[] { this };
        tool = (CGCanvasTool) toolConstructor.newInstance(args);
        String groupName = tool.getGroup();
        if (groupName != null) {
            ButtonGroup group = null;
            if (groups.containsKey(groupName)) {
                group = (ButtonGroup) groups.get(groupName);
            } else {
                group = new ButtonGroup();
                groups.put(groupName, group);
            }
            group.add(tool);
        }
        return tool;
    }
