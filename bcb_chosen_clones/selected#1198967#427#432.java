    private static void testMathOp(Class<? extends Node> opType, Object correct, Node number1, Node number2) throws Exception {
        Node op = opType.getConstructor(Node.class, Node.class).newInstance(number1, number2);
        Map<String, Object> ntfn = new HashMap<String, Object>();
        ntfn.put("string", "string");
        assertEquals(correct, op.evaluate(ntfn));
    }
