    private AbstractViewBean getViewBean(Class beanClazz, Object objectToWrap, String link, String contextPath) {
        if (beanClazz == null) {
            return null;
        }
        if (objectToWrap == null) {
            logger.info("ViewBeanFactory: view requested for null object! ViewBean Class " + beanClazz);
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
            logger.info("Param1: " + classToWrap.getName() + " value: " + objectToWrap);
            logger.info("Param2: " + String.class.getName() + " value: " + link);
            logger.info("Param3: " + String.class.getName() + " value: " + contextPath);
            Constructor constructor = beanClazz.getConstructor(new Class[] { classToWrap, String.class, String.class });
            return (AbstractViewBean) constructor.newInstance(new Object[] { objectToWrap, link, contextPath });
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
