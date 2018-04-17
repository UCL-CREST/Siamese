    public static Object clone(Object o_, boolean raiseException_) {
        if (o_ == null) return null;
        if (o_ instanceof String) return o_;
        try {
            if (o_ instanceof drcl.ObjectCloneable) return ((drcl.ObjectCloneable) o_).clone();
            if (o_.getClass().isArray()) {
                int length_ = Array.getLength(o_);
                Class componentType_ = o_.getClass().getComponentType();
                Object that_ = Array.newInstance(componentType_, length_);
                if (componentType_.isPrimitive()) System.arraycopy(o_, 0, that_, 0, length_); else {
                    for (int i = 0; i < length_; i++) Array.set(that_, i, clone(Array.get(o_, i), raiseException_));
                }
                return that_;
            }
            Method m_ = o_.getClass().getMethod("clone", null);
            return m_.invoke(o_, null);
        } catch (Exception e_) {
            if (raiseException_) {
                Thread t_ = Thread.currentThread();
                t_.getThreadGroup().uncaughtException(t_, e_);
            }
            return null;
        }
    }
