    public AudioDevice createAudioDevice() {
        if (physicalEnvironment == null) {
            System.err.println("Java 3D: createAudioDevice: physicalEnvironment is null");
            return null;
        }
        try {
            String audioDeviceClassName = (String) java.security.AccessController.doPrivileged(new java.security.PrivilegedAction() {

                public Object run() {
                    return System.getProperty("Ding3d.audiodevice");
                }
            });
            if (audioDeviceClassName == null) {
                throw new UnsupportedOperationException("No AudioDevice specified");
            }
            Class audioDeviceClass = null;
            try {
                audioDeviceClass = Class.forName(audioDeviceClassName);
            } catch (ClassNotFoundException ex) {
            }
            if (audioDeviceClass == null) {
                ClassLoader audioDeviceClassLoader = (ClassLoader) java.security.AccessController.doPrivileged(new java.security.PrivilegedAction() {

                    public Object run() {
                        return ClassLoader.getSystemClassLoader();
                    }
                });
                if (audioDeviceClassLoader == null) {
                    throw new IllegalStateException("System ClassLoader is null");
                }
                audioDeviceClass = Class.forName(audioDeviceClassName, true, audioDeviceClassLoader);
            }
            Class physEnvClass = PhysicalEnvironment.class;
            Constructor audioDeviceConstructor = audioDeviceClass.getConstructor(new Class[] { physEnvClass });
            PhysicalEnvironment[] args = new PhysicalEnvironment[] { physicalEnvironment };
            AudioEngine3DL2 mixer = (AudioEngine3DL2) audioDeviceConstructor.newInstance((Object[]) args);
            mixer.initialize();
            return mixer;
        } catch (Throwable e) {
            e.printStackTrace();
            physicalEnvironment.setAudioDevice(null);
            System.err.println("Java 3D: audio is disabled");
            return null;
        }
    }
