    public FileStore provides(@NotNull final String pBaseUri, @NotNull final RuntimeContext<FileStore> pContext) throws ProviderException {
        final URI baseURI = createURI(pBaseUri);
        final FileStoreType fileStoreType = FileStoreTypeEnum.match(baseURI);
        if (fileStoreType != null) {
            final Set<Plugin<FileStore>> pluginImpls = PluginFactory.getImplementationRegistry().findByInterface(FileStore.class);
            for (final Plugin<FileStore> pi : pluginImpls) {
                final Class<? extends FileStore> impl = pi.getAnnotatedClass();
                try {
                    final Constructor<? extends FileStore> constructor = impl.getConstructor(String.class, pContext.getClass());
                    final FileStore fileStore = constructor.newInstance(pBaseUri, pContext);
                    try {
                        if (fileStore.isUriManageable(pBaseUri.toString())) {
                            return fileStore;
                        }
                    } catch (final Throwable th) {
                    }
                    FileStoreFactory.getRegistry().get(fileStore.getBaseUri()).remove(fileStore);
                } catch (final NoSuchMethodException e) {
                    throw new ProviderException("context.provider.error.NoSuchConstructorException", impl.getName(), "RuntimeContext<FileStore> context");
                } catch (final InstantiationException e) {
                    throw new ProviderException("context.provider.error.InstantiationException", impl.getName(), e.getMessage());
                } catch (final IllegalAccessException e) {
                    throw new ProviderException("context.provider.error.IllegalAccessException", impl.getName(), "RuntimeContext<FileStore> context");
                } catch (final InvocationTargetException e) {
                    if (e.getCause() instanceof ResourceException) {
                        throw new ProviderException(e.getCause());
                    } else {
                        throw new ProviderException("context.provider.error.InvocationTargetException", e.getCause(), impl.getName(), "RuntimeContext<FileStore> context", e.getCause().getClass().getName(), e.getCause().getMessage());
                    }
                }
            }
            throw new ProviderException(new ResourceException("store.uri.custom.notmanaged", baseURI.getScheme(), pBaseUri.toString()));
        }
        throw new ProviderException(new ResourceException("store.uri.notmanaged", baseURI.getScheme(), pBaseUri.toString()));
    }
