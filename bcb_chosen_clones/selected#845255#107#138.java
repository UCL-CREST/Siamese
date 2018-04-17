    static AbstractWrapper requestFor(String name, Class wclazz, LoggerImpl ref) {
        AbstractWrapper wrapper;
        AbstractWrapper cached;
        Iterator i;
        if (name == null || wclazz == null || ref == null) {
            throw new LoggingException("Internal error");
        }
        wrapper = null;
        synchronized (cache_) {
            for (i = cache_.iterator(); i.hasNext(); ) {
                cached = (AbstractWrapper) i.next();
                if (wclazz.isInstance(cached) && cached.name_.equals(name)) {
                    wrapper = cached;
                    break;
                }
            }
            if (wrapper == null) {
                try {
                    wrapper = (AbstractWrapper) wclazz.getConstructor(new Class[] { String.class, LoggerImpl.class }).newInstance(new Object[] { name, ref });
                } catch (Throwable t) {
                    if (t instanceof InvocationTargetException) {
                        throw new LoggingException("Failed creating " + wclazz + ": " + ((InvocationTargetException) t).getTargetException().getMessage());
                    }
                    throw new LoggingException("Internal error while creating " + wclazz + ": " + t.getMessage());
                }
                cache_.add(wrapper);
            } else {
                wrapper.cnt_ += 1;
            }
        }
        return wrapper;
    }
