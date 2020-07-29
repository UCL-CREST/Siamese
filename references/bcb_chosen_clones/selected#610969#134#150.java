    public void copyLogic() {
        if (getState() == States.Idle) {
            setState(States.Synchronizing);
            try {
                FileChannel sourceChannel = new FileInputStream(new File(_properties.getProperty("binPath") + name + ".class")).getChannel();
                FileChannel destinationChannel = new FileOutputStream(new File(_properties.getProperty("agentFileLocation") + name + ".class")).getChannel();
                sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
                sourceChannel.close();
                destinationChannel.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            setState(States.Idle);
        }
    }
