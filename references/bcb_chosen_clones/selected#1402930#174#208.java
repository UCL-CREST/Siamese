    private AbstractViewBean getViewBean(Class beanClazz, Object objectToWrap, String link, String contextPath, int maxChunk, int selectedChunk) {
        if (beanClazz == null) {
            return null;
        }
        if (objectToWrap == null) {
            logger.info("ViewBeanFactory: null object to be tab viewed! ViewBean Class " + beanClazz);
            return null;
        }
        try {
            Class classToWrap = null;
            if (objectToWrap instanceof AnnotatedObject) {
                classToWrap = AnnotatedObject.class;
            } else {
                classToWrap = Collection.class;
            }
            logger.info("ClassToWrap affected to: " + classToWrap);
            logger.info("Ask constructor to: " + beanClazz.getName());
            logger.info("Param1: " + Class.class.getName() + " value: " + objectToWrap.getClass().getName());
            logger.info("Param2: " + String.class.getName() + " value: " + link);
            logger.info("Param3: " + String.class.getName() + " value: " + contextPath);
            logger.info("Param4: " + Integer.class.getName() + " value: " + maxChunk);
            logger.info("Param5: " + Integer.class.getName() + " value: " + selectedChunk);
            Constructor constructor = beanClazz.getConstructor(new Class[] { classToWrap, String.class, String.class, Integer.class, Integer.class });
            return (AbstractViewBean) constructor.newInstance(new Object[] { objectToWrap, link, contextPath, new Integer(maxChunk), new Integer(selectedChunk) });
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
