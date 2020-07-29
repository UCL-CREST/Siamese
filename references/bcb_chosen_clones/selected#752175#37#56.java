    private void addInterceptorStack(Class classInterceptor, Intercepts ann) {
        try {
            if (!Interceptors.class.isAssignableFrom(classInterceptor)) throw new BrutosException("the class does not implement interceptors: " + classInterceptor.getName());
            Constructor c = classInterceptor.getConstructor();
            Interceptors i = (Interceptors) c.newInstance();
            Class[] interceptors = i.getInterceptors();
            InterceptorStackBuilder isb = interceptorManager.addInterceptorStack(ann.name().length() == 0 ? classInterceptor.getName() : ann.name(), ann.isDefault());
            addParams(isb, ann.params());
            for (Class interceptor : interceptors) {
                if (!interceptor.isAnnotationPresent(Intercepts.class)) throw new BrutosException("is not a interceptor: " + interceptor.getName());
                Intercepts in = (Intercepts) interceptor.getAnnotation(Intercepts.class);
                String name = in.name().length() == 0 ? interceptor.getName() : in.name();
                isb.addInterceptor(name);
            }
        } catch (BrutosException e) {
            throw e;
        } catch (Exception e) {
            throw new BrutosException(e);
        }
    }
