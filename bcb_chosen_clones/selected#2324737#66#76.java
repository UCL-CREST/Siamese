    @Test
    public void testCascadeTraining() throws IOException {
        File temp = File.createTempFile("fannj_", ".tmp");
        temp.deleteOnExit();
        IOUtils.copy(this.getClass().getResourceAsStream("parity8.train"), new FileOutputStream(temp));
        Fann fann = new FannShortcut(8, 1);
        Trainer trainer = new Trainer(fann);
        float desiredError = .00f;
        float mse = trainer.cascadeTrain(temp.getPath(), 30, 1, desiredError);
        assertTrue("" + mse, mse <= desiredError);
    }
