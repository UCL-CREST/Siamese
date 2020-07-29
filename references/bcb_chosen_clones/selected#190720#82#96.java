    public void init(File file) {
        InputStream is = null;
        ByteArrayOutputStream os = null;
        try {
            is = new FileInputStream(file);
            os = new ByteArrayOutputStream();
            IOUtils.copy(is, os);
        } catch (Throwable e) {
            throw new VisualizerEngineException("Unexcpected exception while reading MDF file", e);
        }
        if (simulationEngine != null) simulationEngine.stopSimulation();
        simulationEngine = new TrafficAsynchSimulationEngine();
        simulationEngine.init(MDFReader.read(os.toByteArray()));
        simulationEngineThread = null;
    }
