    public Node eval(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(4);
        String xnode = startAt.getSubNode(3, Node.VTYPE_STRINGS).getString();
        Node ynode = Node.createEmptyList();
        String regex = startAt.getSubNode(1, Node.VTYPE_STRINGS).getString();
        startAt.requirePCode(2, PCoder.PC_IN);
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(xnode);
            while (matcher.find()) {
                ynode.addElement(new Node(xnode.substring(matcher.start(), matcher.end())));
            }
        } catch (Exception ex) {
            if (Interpreter.isDebugMode()) {
                ex.printStackTrace();
            }
            throw new InterpreterException(StdErrors.extend(StdErrors.Regex_error, regex));
        }
        return ynode;
    }
