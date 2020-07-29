    public final void setPolicy(final String PolicyName) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoClassDefFoundError {
        if (memoryModel_ == null) {
            throw new InstantiationException("E' necessario chiamare createMemoryModel() prima di usare questo metodo");
        }
        Constructor<Object> c;
        if (((this.memoryType_ == MemoryController.MemoryType.PAGED) && !(PolicyName.equals(this.replacementPolicyName_))) || ((this.memoryType_ == MemoryController.MemoryType.SEGMENTED) && !(PolicyName.equals(this.allocationPolicyName_)))) {
            if (this.memoryType_ == MemoryController.MemoryType.PAGED) {
                this.replacementPolicyName_ = PolicyName;
                try {
                    Class cerca = simpleClassLoader.findClass(PolicyName, this.memoryType_);
                    c = (Constructor<Object>) cerca.getConstructors()[0];
                    this.replacementPolicy_ = (ReplacementPolicy) c.newInstance();
                } catch (NoClassDefFoundError e) {
                    throw e;
                }
            } else {
                this.allocationPolicyName_ = PolicyName;
                Class cerca = simpleClassLoader.findClass(PolicyName, this.memoryType_);
                c = (Constructor<Object>) cerca.getConstructors()[0];
                this.allocationPolicy_ = (AllocationPolicy) c.newInstance(memoryModel_);
            }
            if (this.properlyConfigured_ == true) {
                System.out.println("QUA DENTRO CI 6??");
                this.isModified_ = true;
            }
            this.properlyConfigured_ = false;
            this.insertedMethodParameter = new Object[this.getPolicySetMethodName().length];
        }
    }
