    public void solve2(pusherboyHostHandler h) throws Exception {
        long startTime = System.currentTimeMillis();
        searchProc = Runtime.getRuntime().exec("cmd /c E:\\workspace\\pushboy\\src\\pusherboy_v1_1.exe " + "0 1 " + boardX + " " + blocksNum + " " + boardString);
        synchronized (searchProc) {
            isSearchProcRun = true;
            searchProc.waitFor();
            isSearchProcRun = false;
            if (stopTag) return;
            BufferedReader ir = new BufferedReader(new InputStreamReader(searchProc.getInputStream()));
            String rs = ir.readLine();
            if (rs.compareTo("") > 0) {
                solution = rs;
                h.notifyOthersToStop();
                System.out.println("host solved:" + rs);
            }
            ir.close();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("slove2 timeused: " + (endTime - startTime));
    }
