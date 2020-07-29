    public <T extends Command> T newCommand(final Class<T> type) {
        try {
            return (T) type.getConstructor(new Class<?>[] { CommandHelper.class }).newInstance(helper);
        } catch (final InstantiationException ix) {
            throw new RuntimeException(ix);
        } catch (final IllegalAccessException iax) {
            throw new RuntimeException(iax);
        } catch (final InvocationTargetException itx) {
            throw new RuntimeException(itx.getTargetException());
        } catch (final NoSuchMethodException nsmx) {
            throw new RuntimeException(nsmx);
        }
    }
