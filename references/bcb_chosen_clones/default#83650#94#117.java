    public void run() {
        try {
            kh.setClient(myc);
            init();
            String st;
            while (true) {
                st = myc.in.readLine();
                char ch = st.charAt(0);
                if (ch == '�') {
                    StringTokenizer stk = new StringTokenizer(st, "�");
                    String fn = stk.nextToken();
                    if (fn.equals("end")) break;
                    if (fn.equals("users")) {
                        names.removeAllElements();
                        while (stk.hasMoreTokens()) names.add(0, stk.nextToken());
                    }
                    continue;
                }
                allarea.append(st + "\n");
                allarea.setCaretPosition((allarea.getText().length()));
            }
        } catch (Exception e) {
        }
    }
