    @SuppressWarnings("unchecked")
    public LogicOrComposedValidator(MessageProducer messageProducer, boolean isFormatted, Class<Validator<T>>... validatorClasses) {
        this.messageProducer = messageProducer;
        this.validators = new Validator[validatorClasses.length];
        int i = 0;
        for (Class<Validator<T>> clazz : validatorClasses) {
            Constructor<Validator<T>> constructor;
            try {
                constructor = clazz.getConstructor(MessageProducer.class, boolean.class);
                constructor.setAccessible(true);
                validators[i++] = constructor.newInstance(messageProducer, isFormatted);
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
