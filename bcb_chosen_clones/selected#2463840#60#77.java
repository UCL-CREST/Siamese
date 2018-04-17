    private LoadingHandler<T> getLoadingHandler(File file) {
        Class<? extends LoadingHandler<T>> loadingHandlerClass = fileFilter.getLoadingHandlerClass(file);
        LoadingHandler<T> stl = null;
        try {
            if (useStream(file)) {
                stl = (StreamLoadingHandler<T>) loadingHandlerClass.getConstructors()[0].newInstance();
            } else {
                stl = (BulkLoadingHandler<T>) loadingHandlerClass.getConstructors()[0].newInstance();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return stl;
    }
