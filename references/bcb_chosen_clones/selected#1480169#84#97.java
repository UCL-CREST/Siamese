    private Action createAsmAction(ActionMapping actionMapping) {
        ASMActionCreater asmActionCreater = new ASMActionCreater();
        Class<Action> clazz = asmActionCreater.createASMAction(actionMapping);
        try {
            Object obj = clazz.getConstructor().newInstance();
            Action action = (Action) obj;
            Field field = action.getClass().getDeclaredField("action");
            field.setAccessible(true);
            field.set(action, actionMapping.getAction());
            return action;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
