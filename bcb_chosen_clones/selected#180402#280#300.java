    public ServerLifeCycleListener getServerLifeCycleListener(final Map context) {
        try {
            final Constructor[] constructors = this.serverLifeCycleListenerClass.getConstructors();
            final ServerLifeCycleListener listener;
            if (constructors.length == 1) {
                listener = this.serverLifeCycleListenerClass.getConstructor().newInstance();
            } else {
                final Constructor<? extends ServerLifeCycleListener> constructor = this.serverLifeCycleListenerClass.getConstructor(Map.class);
                listener = constructor.newInstance(context);
            }
            return listener;
        } catch (final InstantiationException e) {
            throw propagate(e);
        } catch (final IllegalAccessException e) {
            throw propagate(e);
        } catch (final NoSuchMethodException e) {
            throw propagate(e);
        } catch (InvocationTargetException e) {
            throw propagate(e);
        }
    }
