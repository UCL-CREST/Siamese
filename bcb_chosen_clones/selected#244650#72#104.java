    @SuppressWarnings("unchecked")
    public static MessageConverter getMessageConverter(MessageContext ctx) throws MessageException {
        Service service = (Service) ctx.get("adapterService");
        Boolean input = (Boolean) ctx.get("input");
        Binding fromBinding = service.getPort().getBinding();
        Binding toBinding = service.getAdapter().getService().getPort().getBinding();
        String from = fromBinding.getProtocol();
        String to = toBinding.getProtocol();
        String key = from + "" + to;
        Class<? extends MessageConverter> converterclass = null;
        if (input) {
            converterclass = inputConverterRegistry.get(key);
        } else {
            converterclass = outputConverterRegistry.get(key);
        }
        if (converterclass == null) {
            throw new MessageException("1004");
        }
        try {
            Class[] parameterTypes = { MessageContext.class };
            Constructor<? extends MessageConverter> constructor = converterclass.getConstructor(parameterTypes);
            return constructor.newInstance(ctx);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof MessageException) {
                throw (MessageException) e.getTargetException();
            }
            logger.error(e.getMessage(), e);
            throw new MessageException("1004");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MessageException("1004");
        }
    }
