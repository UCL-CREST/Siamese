        @Override
        public PluginInfo introspect(Class<? extends OndexPlugin> pluginClass) throws PluginIntrospectionException {
            BeanInfo bi;
            try {
                bi = Introspector.getBeanInfo(pluginClass);
            } catch (IntrospectionException e) {
                throw new PluginIntrospectionException("Could not get bean info for plugin class: " + pluginClass, e);
            }
            Plugin p = pluginClass.getAnnotation(Plugin.class);
            String name = p.name().equals("") ? bi.getBeanDescriptor().getName() : p.name();
            String description = p.description();
            String version = p.version();
            PluginType type = p.type();
            List<ArgumentDescriptor> arguments = new ArrayList<ArgumentDescriptor>();
            List<ProvidedDescriptor> provided = new ArrayList<ProvidedDescriptor>();
            List<ConsumedDescriptor> consumed = new ArrayList<ConsumedDescriptor>();
            for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                Method setter = pd.getWriteMethod();
                if (setter != null) {
                    PluginArgument arg = setter.getAnnotation(PluginArgument.class);
                    if (arg != null) {
                        List<Validator> validators = new ArrayList<Validator>();
                        for (Annotation a : setter.getAnnotations()) {
                            UseValidator uv = a.getClass().getAnnotation(UseValidator.class);
                            if (uv != null) {
                                try {
                                    validators.add(uv.value().getConstructor(a.getClass()).newInstance(a));
                                } catch (NoSuchMethodException e) {
                                    throw new PluginIntrospectionException("Could not process validator: " + uv + " for " + a + " on " + setter.getName() + " for plugin " + name, e);
                                } catch (InvocationTargetException e) {
                                    throw new PluginIntrospectionException("Could not process validator: " + uv + " for " + a + " on " + setter.getName() + " for plugin " + name, e);
                                } catch (InstantiationException e) {
                                    throw new PluginIntrospectionException("Could not process validator: " + uv + " for " + a + " on " + setter.getName() + " for plugin " + name, e);
                                } catch (IllegalAccessException e) {
                                    throw new PluginIntrospectionException("Could not process validator: " + uv + " for " + a + " on " + setter.getName() + " for plugin " + name, e);
                                }
                            }
                        }
                        if (!validators.isEmpty() && pd.getReadMethod() == null) {
                            throw new PluginIntrospectionException("Unable to configure plugin " + name + " as property " + pd.getName() + " is annotated with a valiator, but has no getter method");
                        }
                        arguments.add(new ArgumentDescriptor(arg.name().equals("") ? pd.getName() : arg.name(), arg.description(), arg.optional(), arg.defaultValue(), validators, pd));
                    }
                    ProvidedByWorkflow pbw = setter.getAnnotation(ProvidedByWorkflow.class);
                    if (pbw != null) {
                        provided.add(new ProvidedDescriptor(pbw.name().equals("") ? pd.getName() : pbw.name(), pbw.description(), pd));
                    }
                }
                Method getter = pd.getReadMethod();
                if (getter != null) {
                    ConsumedByWorkflow cbw = getter.getAnnotation(ConsumedByWorkflow.class);
                    if (cbw != null) {
                        consumed.add(new ConsumedDescriptor(cbw.name().equals("") ? pd.getName() : cbw.name(), cbw.description(), pd));
                    }
                }
            }
            return new PluginInfo(name, description, version, type, bi.getBeanDescriptor(), arguments, provided, consumed);
        }
