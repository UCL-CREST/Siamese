    public void smartCreateBranch(String key, Object objAttach) throws SchemeObjectException {
        this.checkSchemeObject();
        String tcmd = key;
        Pattern px = null;
        Matcher mx = null;
        px = Pattern.compile("\\s", Pattern.CASE_INSENSITIVE);
        mx = px.matcher(tcmd);
        String results[] = px.split(tcmd);
        boolean _found = false;
        int str = -1;
        int end = -1;
        while (mx.find()) {
            str = mx.start();
            end = mx.end();
            _found = true;
            break;
        }
        String cmd = results[0].trim();
        String args = key.substring(end, key.length()).trim();
        String cmds[] = this.getCommandList();
        try {
            if (cmd.equalsIgnoreCase(cmds[0])) {
                PrintBranchAction p = new PrintBranchAction(0, 0, 0);
                p.setMessage(args);
                p.setKey(key);
                actionTree.addNode(p);
            } else if (cmd.equalsIgnoreCase(cmds[1])) {
                AttachTreeAction p = new AttachTreeAction(0, 0, 0);
                p.attachObject(objAttach);
                p.setKey(key);
                actionTree.addNode(p);
            } else if (cmd.equalsIgnoreCase(cmds[2])) {
                SendMessageBranch p = new SendMessageBranch(0, 0, 0);
                p.setBotProcessThread(this.getProcessThreadNode());
                p.setMessage(args);
                p.setKey(key);
                actionTree.addNode(p);
            } else if (cmd.equalsIgnoreCase(cmds[3])) {
                InternalMessageBranch p = new InternalMessageBranch(0, 0, 0);
                p.setBotProcessThread(this.getProcessThreadNode());
                p.setMessage(args);
                p.setInternalType("irc-send");
                p.setKey(key);
                actionTree.addNode(p);
            } else if (cmd.equalsIgnoreCase(cmds[4])) {
                InternalMessageBranch p = new InternalMessageBranch(0, 0, 0);
                p.setBotProcessThread(this.getProcessThreadNode());
                p.setMessage(args);
                p.setInternalType("client-send");
                p.setKey(key);
                actionTree.addNode(p);
            } else if (cmd.equalsIgnoreCase(cmds[5])) {
                InternalMessageBranch p = new InternalMessageBranch(0, 0, 0);
                p.setBotProcessThread(this.getProcessThreadNode());
                p.setMessage(args);
                p.setInternalType("think-send");
                p.setKey(key);
                actionTree.addNode(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
