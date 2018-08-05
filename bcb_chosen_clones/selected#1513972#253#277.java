        private SOAPFaultException createException(Method method, SOAPFault fault) {
            Class[] exTypes = method.getExceptionTypes();
            for (int i = 0; i < exTypes.length; i++) {
                Class c = exTypes[i];
                if (SOAPFaultException.class.isAssignableFrom(c)) {
                    Constructor[] ctors = c.getConstructors();
                    for (int j = 0; j < ctors.length; j++) {
                        Constructor ctor = ctors[j];
                        Class[] ctorParams = ctor.getParameterTypes();
                        if (ctorParams.length == 1) {
                            Class param = ctorParams[0];
                            if (SOAPFault.class.isAssignableFrom(param)) {
                                try {
                                    SOAPFaultException ex = (SOAPFaultException) ctor.newInstance(new Object[] { fault });
                                    return ex;
                                } catch (Throwable e) {
                                    return null;
                                }
                            }
                        }
                    }
                }
            }
            return null;
        }
