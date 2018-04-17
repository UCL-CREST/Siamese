    public Boolean call() throws ComponentInvocationException {
        Constructor<? extends AbstractComponent> constructor;
        try {
            constructor = processClass.getConstructor(HashMap.class);
        } catch (SecurityException e1) {
            throw new ComponentInvocationException("The process to be executed could not be invoked.");
        } catch (NoSuchMethodException e1) {
            throw new ComponentInvocationException("The constructor of the process to be executed could not be found.");
        }
        for (int i = 0; i < this.invokeCount; i++) {
            IComponent proc;
            try {
                proc = constructor.newInstance(this.spaces);
            } catch (InstantiationException e) {
                throw new ComponentInvocationException("The process to be executed could not be instantiated.");
            } catch (IllegalAccessException e) {
                throw new ComponentInvocationException("The process to be executed is illegally accessed.");
            } catch (InvocationTargetException e) {
                throw new ComponentInvocationException("The process to be executed could not be invoked.");
            }
            proc.call();
        }
        return true;
    }
