    public int fibonacci(int num) {
        if (num == 0) {
            return 0;
        } else if (num == 1) {
            return 1;
        } else {
            processor.getAlgData().setRecursiveCalls(processor.getAlgData().getRecursiveCalls() + 2);
            return fibonacci(num - 1) + fibonacci(num - 2);
        }
    }
