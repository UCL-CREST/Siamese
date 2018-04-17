    public Start parse() throws ParserException, LexerException, IOException {
        push(0, null, false);
        List ign = null;
        while (true) {
            while (index(lexer.peek()) == -1) {
                if (ign == null) {
                    ign = new TypedLinkedList(NodeCast.instance);
                }
                ign.add(lexer.next());
            }
            if (ign != null) {
                ignoredTokens.setIn(lexer.peek(), ign);
                ign = null;
            }
            last_pos = lexer.peek().getPos();
            last_line = lexer.peek().getLine();
            last_token = lexer.peek();
            int index = index(lexer.peek());
            action[0] = actionTable[state()][0][1];
            action[1] = actionTable[state()][0][2];
            int low = 1;
            int high = actionTable[state()].length - 1;
            while (low <= high) {
                int middle = (low + high) / 2;
                if (index < actionTable[state()][middle][0]) {
                    high = middle - 1;
                } else if (index > actionTable[state()][middle][0]) {
                    low = middle + 1;
                } else {
                    action[0] = actionTable[state()][middle][1];
                    action[1] = actionTable[state()][middle][2];
                    break;
                }
            }
            switch(action[0]) {
                case SHIFT:
                    push(action[1], lexer.next(), true);
                    last_shift = action[1];
                    break;
                case REDUCE:
                    switch(action[1]) {
                        case 0:
                            {
                                Node node = new0();
                                push(goTo(0), node, true);
                            }
                            break;
                        case 1:
                            {
                                Node node = new1();
                                push(goTo(1), node, true);
                            }
                            break;
                        case 2:
                            {
                                Node node = new2();
                                push(goTo(2), node, true);
                            }
                            break;
                        case 3:
                            {
                                Node node = new3();
                                push(goTo(2), node, true);
                            }
                            break;
                        case 4:
                            {
                                Node node = new4();
                                push(goTo(3), node, true);
                            }
                            break;
                        case 5:
                            {
                                Node node = new5();
                                push(goTo(3), node, true);
                            }
                            break;
                        case 6:
                            {
                                Node node = new6();
                                push(goTo(3), node, true);
                            }
                            break;
                        case 7:
                            {
                                Node node = new7();
                                push(goTo(3), node, true);
                            }
                            break;
                        case 8:
                            {
                                Node node = new8();
                                push(goTo(4), node, true);
                            }
                            break;
                        case 9:
                            {
                                Node node = new9();
                                push(goTo(4), node, true);
                            }
                            break;
                        case 10:
                            {
                                Node node = new10();
                                push(goTo(4), node, true);
                            }
                            break;
                        case 11:
                            {
                                Node node = new11();
                                push(goTo(5), node, true);
                            }
                            break;
                        case 12:
                            {
                                Node node = new12();
                                push(goTo(5), node, true);
                            }
                            break;
                        case 13:
                            {
                                Node node = new13();
                                push(goTo(5), node, true);
                            }
                            break;
                        case 14:
                            {
                                Node node = new14();
                                push(goTo(6), node, true);
                            }
                            break;
                        case 15:
                            {
                                Node node = new15();
                                push(goTo(6), node, true);
                            }
                            break;
                        case 16:
                            {
                                Node node = new16();
                                push(goTo(6), node, true);
                            }
                            break;
                        case 17:
                            {
                                Node node = new17();
                                push(goTo(6), node, true);
                            }
                            break;
                        case 18:
                            {
                                Node node = new18();
                                push(goTo(6), node, true);
                            }
                            break;
                        case 19:
                            {
                                Node node = new19();
                                push(goTo(6), node, true);
                            }
                            break;
                        case 20:
                            {
                                Node node = new20();
                                push(goTo(6), node, true);
                            }
                            break;
                        case 21:
                            {
                                Node node = new21();
                                push(goTo(6), node, true);
                            }
                            break;
                        case 22:
                            {
                                Node node = new22();
                                push(goTo(6), node, true);
                            }
                            break;
                        case 23:
                            {
                                Node node = new23();
                                push(goTo(6), node, true);
                            }
                            break;
                        case 24:
                            {
                                Node node = new24();
                                push(goTo(6), node, true);
                            }
                            break;
                        case 25:
                            {
                                Node node = new25();
                                push(goTo(6), node, true);
                            }
                            break;
                        case 26:
                            {
                                Node node = new26();
                                push(goTo(6), node, true);
                            }
                            break;
                        case 27:
                            {
                                Node node = new27();
                                push(goTo(6), node, true);
                            }
                            break;
                        case 28:
                            {
                                Node node = new28();
                                push(goTo(7), node, true);
                            }
                            break;
                        case 29:
                            {
                                Node node = new29();
                                push(goTo(7), node, true);
                            }
                            break;
                        case 30:
                            {
                                Node node = new30();
                                push(goTo(7), node, true);
                            }
                            break;
                        case 31:
                            {
                                Node node = new31();
                                push(goTo(8), node, true);
                            }
                            break;
                        case 32:
                            {
                                Node node = new32();
                                push(goTo(8), node, true);
                            }
                            break;
                        case 33:
                            {
                                Node node = new33();
                                push(goTo(8), node, true);
                            }
                            break;
                        case 34:
                            {
                                Node node = new34();
                                push(goTo(8), node, true);
                            }
                            break;
                        case 35:
                            {
                                Node node = new35();
                                push(goTo(8), node, true);
                            }
                            break;
                        case 36:
                            {
                                Node node = new36();
                                push(goTo(9), node, true);
                            }
                            break;
                        case 37:
                            {
                                Node node = new37();
                                push(goTo(9), node, true);
                            }
                            break;
                        case 38:
                            {
                                Node node = new38();
                                push(goTo(9), node, true);
                            }
                            break;
                        case 39:
                            {
                                Node node = new39();
                                push(goTo(10), node, true);
                            }
                            break;
                        case 40:
                            {
                                Node node = new40();
                                push(goTo(10), node, true);
                            }
                            break;
                        case 41:
                            {
                                Node node = new41();
                                push(goTo(10), node, true);
                            }
                            break;
                        case 42:
                            {
                                Node node = new42();
                                push(goTo(10), node, true);
                            }
                            break;
                        case 43:
                            {
                                Node node = new43();
                                push(goTo(10), node, true);
                            }
                            break;
                        case 44:
                            {
                                Node node = new44();
                                push(goTo(11), node, true);
                            }
                            break;
                        case 45:
                            {
                                Node node = new45();
                                push(goTo(11), node, true);
                            }
                            break;
                        case 46:
                            {
                                Node node = new46();
                                push(goTo(11), node, true);
                            }
                            break;
                        case 47:
                            {
                                Node node = new47();
                                push(goTo(12), node, true);
                            }
                            break;
                        case 48:
                            {
                                Node node = new48();
                                push(goTo(12), node, true);
                            }
                            break;
                        case 49:
                            {
                                Node node = new49();
                                push(goTo(13), node, true);
                            }
                            break;
                        case 50:
                            {
                                Node node = new50();
                                push(goTo(13), node, true);
                            }
                            break;
                        case 51:
                            {
                                Node node = new51();
                                push(goTo(14), node, true);
                            }
                            break;
                        case 52:
                            {
                                Node node = new52();
                                push(goTo(14), node, true);
                            }
                            break;
                        case 53:
                            {
                                Node node = new53();
                                push(goTo(15), node, true);
                            }
                            break;
                        case 54:
                            {
                                Node node = new54();
                                push(goTo(16), node, true);
                            }
                            break;
                        case 55:
                            {
                                Node node = new55();
                                push(goTo(17), node, true);
                            }
                            break;
                        case 56:
                            {
                                Node node = new56();
                                push(goTo(17), node, true);
                            }
                            break;
                        case 57:
                            {
                                Node node = new57();
                                push(goTo(17), node, true);
                            }
                            break;
                        case 58:
                            {
                                Node node = new58();
                                push(goTo(17), node, true);
                            }
                            break;
                        case 59:
                            {
                                Node node = new59();
                                push(goTo(17), node, true);
                            }
                            break;
                        case 60:
                            {
                                Node node = new60();
                                push(goTo(17), node, true);
                            }
                            break;
                        case 61:
                            {
                                Node node = new61();
                                push(goTo(17), node, true);
                            }
                            break;
                        case 62:
                            {
                                Node node = new62();
                                push(goTo(17), node, true);
                            }
                            break;
                    }
                    break;
                case ACCEPT:
                    {
                        EOF node2 = (EOF) lexer.next();
                        PEqExpression node1 = (PEqExpression) pop();
                        Start node = new Start(node1, node2);
                        return node;
                    }
                case ERROR:
                    throw new ParserException(last_token, "[" + last_line + "," + last_pos + "] " + errorMessages[errors[action[1]]]);
            }
        }
    }
