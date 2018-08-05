    @Test
    public void testFromFile() throws IOException {
        File temp = File.createTempFile("fannj_", ".tmp");
        temp.deleteOnExit();
        IOUtils.copy(this.getClass().getResourceAsStream("xor_float.net"), new FileOutputStream(temp));
        Fann fann = new Fann(temp.getPath());
        assertEquals(2, fann.getNumInputNeurons());
        assertEquals(1, fann.getNumOutputNeurons());
        assertEquals(-1f, fann.run(new float[] { -1, -1 })[0], .2f);
        assertEquals(1f, fann.run(new float[] { -1, 1 })[0], .2f);
        assertEquals(1f, fann.run(new float[] { 1, -1 })[0], .2f);
        assertEquals(-1f, fann.run(new float[] { 1, 1 })[0], .2f);
        fann.close();
    }
