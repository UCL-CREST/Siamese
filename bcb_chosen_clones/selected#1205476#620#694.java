    public void registerProvider(Class provider, boolean isBuiltin) {
        if (MessageBodyReader.class.isAssignableFrom(provider)) {
            try {
                addMessageBodyReader(provider, isBuiltin);
            } catch (Exception e) {
                throw new RuntimeException("Unable to instantiate MessageBodyReader", e);
            }
        }
        if (MessageBodyWriter.class.isAssignableFrom(provider)) {
            try {
                addMessageBodyWriter(provider, isBuiltin);
            } catch (Exception e) {
                throw new RuntimeException("Unable to instantiate MessageBodyWriter", e);
            }
        }
        if (ExceptionMapper.class.isAssignableFrom(provider)) {
            try {
                addExceptionMapper(provider);
            } catch (Exception e) {
                throw new RuntimeException("Unable to instantiate ExceptionMapper", e);
            }
        }
        if (ClientExecutionInterceptor.class.isAssignableFrom(provider)) {
            clientExecutionInterceptorRegistry.register(provider);
        }
        if (PreProcessInterceptor.class.isAssignableFrom(provider)) {
            serverPreProcessInterceptorRegistry.register(provider);
        }
        if (PostProcessInterceptor.class.isAssignableFrom(provider)) {
            serverPostProcessInterceptorRegistry.register(provider);
        }
        if (MessageBodyWriterInterceptor.class.isAssignableFrom(provider)) {
            if (provider.isAnnotationPresent(ServerInterceptor.class)) {
                serverMessageBodyWriterInterceptorRegistry.register(provider);
            }
            if (provider.isAnnotationPresent(ClientInterceptor.class)) {
                clientMessageBodyWriterInterceptorRegistry.register(provider);
            }
            if (!provider.isAnnotationPresent(ServerInterceptor.class) && !provider.isAnnotationPresent(ClientInterceptor.class)) {
                throw new RuntimeException("Interceptor class must be annotated with @ServerInterceptor and/or @ClientInterceptor");
            }
        }
        if (MessageBodyReaderInterceptor.class.isAssignableFrom(provider)) {
            if (provider.isAnnotationPresent(ServerInterceptor.class)) {
                serverMessageBodyReaderInterceptorRegistry.register(provider);
            }
            if (provider.isAnnotationPresent(ClientInterceptor.class)) {
                clientMessageBodyReaderInterceptorRegistry.register(provider);
            }
            if (!provider.isAnnotationPresent(ServerInterceptor.class) && !provider.isAnnotationPresent(ClientInterceptor.class)) {
                throw new RuntimeException("Interceptor class must be annotated with @ServerInterceptor and/or @ClientInterceptor");
            }
        }
        if (ContextResolver.class.isAssignableFrom(provider)) {
            try {
                addContextResolver(provider, true);
            } catch (Exception e) {
                throw new RuntimeException("Unable to instantiate ContextResolver", e);
            }
        }
        if (StringConverter.class.isAssignableFrom(provider)) {
            addStringConverter(provider);
        }
        if (StringParameterUnmarshaller.class.isAssignableFrom(provider)) {
            addStringParameterUnmarshaller(provider);
        }
        if (InjectorFactory.class.isAssignableFrom(provider)) {
            try {
                Constructor constructor = provider.getConstructor(ResteasyProviderFactory.class);
                this.injectorFactory = (InjectorFactory) constructor.newInstance(this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
