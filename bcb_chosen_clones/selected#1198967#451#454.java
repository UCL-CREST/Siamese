    private static void testPred(Class<? extends StringCompareNode> type, String arg1, String arg2, Boolean answer) throws Exception {
        Node node = type.getConstructor(Node.class, Const.class).newInstance(new Const(arg1), new Const(arg2));
        assertEquals(answer, node.evaluate(EMPTY_NOTIFICATION));
    }
