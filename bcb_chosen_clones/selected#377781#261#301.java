    private String choosePivotVertex() throws ProcessorExecutionException {
        String result = null;
        Graph src;
        Graph dest;
        Path tmpDir;
        System.out.println("##########>" + _dirMgr.getSeqNum() + " Choose the pivot vertex");
        src = new Graph(Graph.defaultGraph());
        src.setPath(_curr_path);
        dest = new Graph(Graph.defaultGraph());
        try {
            tmpDir = _dirMgr.getTempDir();
        } catch (IOException e) {
            throw new ProcessorExecutionException(e);
        }
        dest.setPath(tmpDir);
        GraphAlgorithm choose_pivot = new PivotChoose();
        choose_pivot.setConf(context);
        choose_pivot.setSource(src);
        choose_pivot.setDestination(dest);
        choose_pivot.setMapperNum(getMapperNum());
        choose_pivot.setReducerNum(getReducerNum());
        choose_pivot.execute();
        try {
            Path the_file = new Path(tmpDir.toString() + "/part-00000");
            FileSystem client = FileSystem.get(context);
            if (!client.exists(the_file)) {
                throw new ProcessorExecutionException("Did not find the chosen vertex in " + the_file.toString());
            }
            FSDataInputStream input_stream = client.open(the_file);
            ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
            IOUtils.copyBytes(input_stream, output_stream, context, false);
            String the_line = output_stream.toString();
            result = the_line.substring(PivotChoose.KEY_PIVOT.length()).trim();
            input_stream.close();
            output_stream.close();
            System.out.println("##########> Chosen pivot id = " + result);
        } catch (IOException e) {
            throw new ProcessorExecutionException(e);
        }
        return result;
    }
