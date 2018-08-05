    protected Token getToken() throws IOException, LexerException {
        int dfa_state = 0;
        int start_pos = pos;
        int start_line = line;
        int accept_state = -1;
        int accept_token = -1;
        int accept_length = -1;
        int accept_pos = -1;
        int accept_line = -1;
        int[][][] gotoTable = this.gotoTable[state.id()];
        int[] accept = this.accept[state.id()];
        text.setLength(0);
        while (true) {
            int c = getChar();
            if (c != -1) {
                switch(c) {
                    case 10:
                        if (cr) {
                            cr = false;
                        } else {
                            line++;
                            pos = 0;
                        }
                        break;
                    case 13:
                        line++;
                        pos = 0;
                        cr = true;
                        break;
                    default:
                        pos++;
                        cr = false;
                        break;
                }
                ;
                text.append((char) c);
                do {
                    int oldState = (dfa_state < -1) ? (-2 - dfa_state) : dfa_state;
                    dfa_state = -1;
                    int[][] tmp1 = gotoTable[oldState];
                    int low = 0;
                    int high = tmp1.length - 1;
                    while (low <= high) {
                        int middle = (low + high) / 2;
                        int[] tmp2 = tmp1[middle];
                        if (c < tmp2[0]) {
                            high = middle - 1;
                        } else if (c > tmp2[1]) {
                            low = middle + 1;
                        } else {
                            dfa_state = tmp2[2];
                            break;
                        }
                    }
                } while (dfa_state < -1);
            } else {
                dfa_state = -1;
            }
            if (dfa_state >= 0) {
                if (accept[dfa_state] != -1) {
                    accept_state = dfa_state;
                    accept_token = accept[dfa_state];
                    accept_length = text.length();
                    accept_pos = pos;
                    accept_line = line;
                }
            } else {
                if (accept_state != -1) {
                    switch(accept_token) {
                        case 0:
                            {
                                Token token = new0(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.TICK_STATE;
                                        break;
                                    case 2:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 1:
                            {
                                Token token = new1(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 2:
                            {
                                Token token = new2(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 3:
                            {
                                Token token = new3(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                    case 1:
                                        state = State.OCL_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 4:
                            {
                                Token token = new4(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                    case 1:
                                        state = State.OCL_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 5:
                            {
                                Token token = new5(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 6:
                            {
                                Token token = new6(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 7:
                            {
                                Token token = new7(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 8:
                            {
                                Token token = new8(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 9:
                            {
                                Token token = new9(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 10:
                            {
                                Token token = new10(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 11:
                            {
                                Token token = new11(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 12:
                            {
                                Token token = new12(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 13:
                            {
                                Token token = new13(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 14:
                            {
                                Token token = new14(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 15:
                            {
                                Token token = new15(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 16:
                            {
                                Token token = new16(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 17:
                            {
                                Token token = new17(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 18:
                            {
                                Token token = new18(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 19:
                            {
                                Token token = new19(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 20:
                            {
                                Token token = new20(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 21:
                            {
                                Token token = new21(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 22:
                            {
                                Token token = new22(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 23:
                            {
                                Token token = new23(start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.OCL_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 24:
                            {
                                Token token = new24(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 25:
                            {
                                Token token = new25(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 26:
                            {
                                Token token = new26(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 27:
                            {
                                Token token = new27(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 28:
                            {
                                Token token = new28(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 29:
                            {
                                Token token = new29(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 0:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 30:
                            {
                                Token token = new30(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 1:
                                        state = State.INIT_STATE;
                                        break;
                                }
                                return token;
                            }
                        case 31:
                            {
                                Token token = new31(getText(accept_length), start_line + 1, start_pos + 1);
                                pushBack(accept_length);
                                pos = accept_pos;
                                line = accept_line;
                                switch(state.id()) {
                                    case 2:
                                        state = State.TICK_STATE;
                                        break;
                                }
                                return token;
                            }
                    }
                } else {
                    if (text.length() > 0) {
                        throw new LexerException("[" + (start_line + 1) + "," + (start_pos + 1) + "]" + " Unknown token: " + text);
                    } else {
                        EOF token = new EOF(start_line + 1, start_pos + 1);
                        return token;
                    }
                }
            }
        }
    }
