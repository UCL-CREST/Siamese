    public void testBenchConstructorReflectionCall() throws Exception {
        final Constructor ctor = Bar.class.getConstructor(null);
        new Benchmark("Nuts: BarConstructor.newInstance()", LOOP) {

            public void run() throws Exception {
                ctor.newInstance(null);
            }
        }.start(true);
    }
