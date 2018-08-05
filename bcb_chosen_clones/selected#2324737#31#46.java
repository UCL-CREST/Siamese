    @Test
    public void testTrainingQuickprop() throws IOException {
        File temp = File.createTempFile("fannj_", ".tmp");
        temp.deleteOnExit();
        IOUtils.copy(this.getClass().getResourceAsStream("xor.data"), new FileOutputStream(temp));
        List<Layer> layers = new ArrayList<Layer>();
        layers.add(Layer.create(2));
        layers.add(Layer.create(3, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        layers.add(Layer.create(1, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        Fann fann = new Fann(layers);
        Trainer trainer = new Trainer(fann);
        trainer.setTrainingAlgorithm(TrainingAlgorithm.FANN_TRAIN_QUICKPROP);
        float desiredError = .001f;
        float mse = trainer.train(temp.getPath(), 500000, 1000, desiredError);
        assertTrue("" + mse, mse <= desiredError);
    }
