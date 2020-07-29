    public void submitAndExtract(Queue<Board2> boards, String destination, String filename, int maxsteps, String rec) {
        PutMethod httpput = new PutMethod("https://mig-1.imada.sdu.dk/" + destination + filename);
        ZipOutputStream out = null;
        ObjectOutputStream objout = null;
        try {
            int count = 0;
            int filecount = 0;
            for (Board2 board : boards) {
                if ((count % 1000) == 0) {
                    filecount++;
                    if (count != 0) {
                        out.close();
                    }
                    out = new ZipOutputStream(new FileOutputStream("NQ-" + board.size + "-" + maxsteps + "-" + filecount + ".zip"));
                }
                out.putNextEntry(new ZipEntry("board" + board.size + "-" + maxsteps + "-" + rec + "-" + count + ".obj"));
                objout = new ObjectOutputStream(out);
                objout.writeObject(board);
                out.closeEntry();
                MigJob job = new MigJob("NQueenJob boards/board" + board.size + "-" + maxsteps + "-" + rec + "-" + count + ".obj", "board" + count + ".mrsl");
                out.putNextEntry(new ZipEntry("board" + board.size + "-" + count + ".mrsl"));
                out.write(job.toString().getBytes());
                out.closeEntry();
                count++;
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpput.releaseConnection();
        }
    }
