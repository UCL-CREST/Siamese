    public void solve2(IoSession s, String puzzle) throws Exception {
        long startTime = System.currentTimeMillis();
        reflash();
        session = s;
        parsePuzzleInfo(puzzle);
        int offset = startPos;
        searchProc = Runtime.getRuntime().exec("cmd /c E:\\workspace\\pushboy\\src\\pusherboy_v1_1.exe " + offset + " -1 " + boardX + " " + blocksNum + " " + boardString);
        isSearchProcRun = true;
        if (stopTag) return;
        BufferedReader ir = new BufferedReader(new InputStreamReader(searchProc.getInputStream()));
        String rs = ir.readLine();
        if (rs.compareTo("") > 0) {
            session.write("solution:" + rs);
            System.out.println("collaborator solved,solution:" + rs);
            session.write("stoped");
        }
        ir.close();
        long endTime = System.currentTimeMillis();
        System.out.println("slove2 timeused: " + (endTime - startTime));
    }
