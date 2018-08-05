            @SuppressWarnings("unchecked")
            @Override
            public void onClick() {
                Constructor<? extends TysanPage> modelParamConstructor = null;
                Constructor<? extends TysanPage> userParamConstructor = null;
                Constructor<? extends TysanPage> noParamConstructor = null;
                U modelObject = super.getModelObject();
                for (Constructor<?> cns : pageClass.getConstructors()) {
                    if (cns.getParameterTypes().length == 0) {
                        if (!cns.isAnnotationPresent(NoAuto.class)) {
                            noParamConstructor = (Constructor<? extends TysanPage>) cns;
                        }
                    }
                    if (cns.getParameterTypes().length == 1 && cns.getParameterTypes()[0] == User.class) {
                        if (!cns.isAnnotationPresent(NoAuto.class)) {
                            userParamConstructor = (Constructor<? extends TysanPage>) cns;
                        }
                    }
                    if (cns.getParameterTypes().length == 1 && modelObject != null && cns.getParameterTypes()[0].isAssignableFrom(modelObject.getClass())) {
                        if (!cns.isAnnotationPresent(NoAuto.class)) {
                            modelParamConstructor = (Constructor<? extends TysanPage>) cns;
                        }
                    }
                }
                try {
                    if (userParamConstructor != null) {
                        setResponsePage(userParamConstructor.newInstance(getUser()));
                    } else if (noParamConstructor != null) {
                        setResponsePage(noParamConstructor.newInstance());
                    } else if (modelParamConstructor != null) {
                        setResponsePage(modelParamConstructor.newInstance(modelObject));
                    }
                } catch (IllegalArgumentException e) {
                    logger.error(e.getMessage(), e);
                } catch (InstantiationException e) {
                    logger.error(e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    logger.error(e.getMessage(), e);
                } catch (InvocationTargetException e) {
                    logger.error(e.getMessage(), e);
                }
            }
