    private Service getServiceInstance(String className) {
        try {
            Class clazz = Class.forName(className);
            return (Service) clazz.getConstructor(new Class[] {}).newInstance(new Object[] {});
        } catch (ClassNotFoundException e) {
            this.logger.error(className, e);
            return null;
        } catch (SecurityException e) {
            this.logger.error(className, e);
            return null;
        } catch (NoSuchMethodException e) {
            this.logger.error(className, e);
            return null;
        } catch (IllegalArgumentException e) {
            this.logger.error(className, e);
            return null;
        } catch (InstantiationException e) {
            this.logger.error(className, e);
            return null;
        } catch (IllegalAccessException e) {
            this.logger.error(className, e);
            return null;
        } catch (InvocationTargetException e) {
            this.logger.error(className, e);
            return null;
        }
    }
