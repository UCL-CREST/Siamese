    @Override
    public void execute() throws ProcessorExecutionException {
        try {
            if (getSource().getPaths() == null || getSource().getPaths().size() == 0 || getDestination().getPaths() == null || getDestination().getPaths().size() == 0) {
                throw new ProcessorExecutionException("No input and/or output paths specified.");
            }
            String temp_dir_prefix = getDestination().getPath().getParent().toString() + "/bcc_" + getDestination().getPath().getName() + "_";
            SequenceTempDirMgr dirMgr = new SequenceTempDirMgr(temp_dir_prefix, context);
            dirMgr.setSeqNum(0);
            Path tmpDir;
            System.out.println("++++++>" + dirMgr.getSeqNum() + ": Transform input to AdjSetVertex");
            Transformer transformer = new OutAdjVertex2AdjSetVertexTransformer();
            transformer.setConf(context);
            transformer.setSrcPath(getSource().getPath());
            tmpDir = dirMgr.getTempDir();
            transformer.setDestPath(tmpDir);
            transformer.setMapperNum(getMapperNum());
            transformer.setReducerNum(getReducerNum());
            transformer.execute();
            System.out.println("++++++>" + dirMgr.getSeqNum() + ": Transform input to LabeledAdjSetVertex");
            Vertex2LabeledTransformer l_transformer = new Vertex2LabeledTransformer();
            l_transformer.setConf(context);
            l_transformer.setSrcPath(tmpDir);
            tmpDir = dirMgr.getTempDir();
            l_transformer.setDestPath(tmpDir);
            l_transformer.setMapperNum(getMapperNum());
            l_transformer.setReducerNum(getReducerNum());
            l_transformer.setOutputValueClass(LabeledAdjSetVertex.class);
            l_transformer.execute();
            Graph src;
            Graph dest;
            Path path_to_remember = tmpDir;
            System.out.println("++++++>" + dirMgr.getSeqNum() + ": SpanningTreeRootChoose");
            src = new Graph(Graph.defaultGraph());
            src.setPath(tmpDir);
            dest = new Graph(Graph.defaultGraph());
            tmpDir = dirMgr.getTempDir();
            dest.setPath(tmpDir);
            GraphAlgorithm choose_root = new SpanningTreeRootChoose();
            choose_root.setConf(context);
            choose_root.setSource(src);
            choose_root.setDestination(dest);
            choose_root.setMapperNum(getMapperNum());
            choose_root.setReducerNum(getReducerNum());
            choose_root.execute();
            Path the_file = new Path(tmpDir.toString() + "/part-00000");
            FileSystem client = FileSystem.get(context);
            if (!client.exists(the_file)) {
                throw new ProcessorExecutionException("Did not find the chosen vertex in " + the_file.toString());
            }
            FSDataInputStream input_stream = client.open(the_file);
            ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
            IOUtils.copyBytes(input_stream, output_stream, context, false);
            String the_line = output_stream.toString();
            String root_vertex_id = the_line.substring(SpanningTreeRootChoose.SPANNING_TREE_ROOT.length()).trim();
            input_stream.close();
            output_stream.close();
            System.out.println("++++++> Chosen the root of spanning tree = " + root_vertex_id);
            while (true) {
                System.out.println("++++++>" + dirMgr.getSeqNum() + " Generate the spanning tree rooted at : (" + root_vertex_id + ") from " + tmpDir);
                src = new Graph(Graph.defaultGraph());
                src.setPath(path_to_remember);
                tmpDir = dirMgr.getTempDir();
                dest = new Graph(Graph.defaultGraph());
                dest.setPath(tmpDir);
                path_to_remember = tmpDir;
                GraphAlgorithm spanning = new SpanningTreeGenerate();
                spanning.setConf(context);
                spanning.setSource(src);
                spanning.setDestination(dest);
                spanning.setMapperNum(getMapperNum());
                spanning.setReducerNum(getReducerNum());
                spanning.setParameter(ConstantLabels.ROOT_ID, root_vertex_id);
                spanning.execute();
                System.out.println("++++++>" + dirMgr.getSeqNum() + " Test spanning convergence");
                src = new Graph(Graph.defaultGraph());
                src.setPath(tmpDir);
                tmpDir = dirMgr.getTempDir();
                dest = new Graph(Graph.defaultGraph());
                dest.setPath(tmpDir);
                GraphAlgorithm conv_tester = new SpanningConvergenceTest();
                conv_tester.setConf(context);
                conv_tester.setSource(src);
                conv_tester.setDestination(dest);
                conv_tester.setMapperNum(getMapperNum());
                conv_tester.setReducerNum(getReducerNum());
                conv_tester.execute();
                long vertexes_out_of_tree = MRConsoleReader.getMapOutputRecordNum(conv_tester.getFinalStatus());
                System.out.println("++++++> number of vertexes out of the spanning tree = " + vertexes_out_of_tree);
                if (vertexes_out_of_tree == 0) {
                    break;
                }
            }
            System.out.println("++++++> From spanning tree to sets of edges");
            src = new Graph(Graph.defaultGraph());
            src.setPath(path_to_remember);
            tmpDir = dirMgr.getTempDir();
            dest = new Graph(Graph.defaultGraph());
            dest.setPath(tmpDir);
            GraphAlgorithm tree2set = new Tree2EdgeSet();
            tree2set.setConf(context);
            tree2set.setSource(src);
            tree2set.setDestination(dest);
            tree2set.setMapperNum(getMapperNum());
            tree2set.setReducerNum(getReducerNum());
            tree2set.execute();
            long map_input_records_num = -1;
            long map_output_records_num = -2;
            Stack<Path> expanding_stack = new Stack<Path>();
            do {
                System.out.println("++++++>" + dirMgr.getSeqNum() + ": EdgeSetMinorJoin");
                GraphAlgorithm minorjoin = new EdgeSetMinorJoin();
                minorjoin.setConf(context);
                src = new Graph(Graph.defaultGraph());
                src.setPath(tmpDir);
                dest = new Graph(Graph.defaultGraph());
                tmpDir = dirMgr.getTempDir();
                dest.setPath(tmpDir);
                minorjoin.setSource(src);
                minorjoin.setDestination(dest);
                minorjoin.setMapperNum(getMapperNum());
                minorjoin.setReducerNum(getReducerNum());
                minorjoin.execute();
                expanding_stack.push(tmpDir);
                System.out.println("++++++>" + dirMgr.getSeqNum() + ": EdgeSetJoin");
                GraphAlgorithm join = new EdgeSetJoin();
                join.setConf(context);
                src = new Graph(Graph.defaultGraph());
                src.setPath(tmpDir);
                dest = new Graph(Graph.defaultGraph());
                tmpDir = dirMgr.getTempDir();
                dest.setPath(tmpDir);
                join.setSource(src);
                join.setDestination(dest);
                join.setMapperNum(getMapperNum());
                join.setReducerNum(getReducerNum());
                join.execute();
                map_input_records_num = MRConsoleReader.getMapInputRecordNum(join.getFinalStatus());
                map_output_records_num = MRConsoleReader.getMapOutputRecordNum(join.getFinalStatus());
                System.out.println("++++++> map in/out : " + map_input_records_num + "/" + map_output_records_num);
            } while (map_input_records_num != map_output_records_num);
            while (expanding_stack.size() > 0) {
                System.out.println("++++++>" + dirMgr.getSeqNum() + ": EdgeSetExpand");
                GraphAlgorithm expand = new EdgeSetExpand();
                expand.setConf(context);
                src = new Graph(Graph.defaultGraph());
                src.addPath(expanding_stack.pop());
                src.addPath(tmpDir);
                dest = new Graph(Graph.defaultGraph());
                tmpDir = dirMgr.getTempDir();
                dest.setPath(tmpDir);
                expand.setSource(src);
                expand.setDestination(dest);
                expand.setMapperNum(getMapperNum());
                expand.setReducerNum(getReducerNum());
                expand.execute();
                System.out.println("++++++>" + dirMgr.getSeqNum() + ": EdgeSetMinorExpand");
                GraphAlgorithm minorexpand = new EdgeSetMinorExpand();
                minorexpand.setConf(context);
                src = new Graph(Graph.defaultGraph());
                src.setPath(tmpDir);
                dest = new Graph(Graph.defaultGraph());
                tmpDir = dirMgr.getTempDir();
                dest.setPath(tmpDir);
                minorexpand.setSource(src);
                minorexpand.setDestination(dest);
                minorexpand.setMapperNum(getMapperNum());
                minorexpand.setReducerNum(getReducerNum());
                minorexpand.execute();
            }
            System.out.println("++++++>" + dirMgr.getSeqNum() + ": EdgeSetSummarize");
            GraphAlgorithm summarize = new EdgeSetSummarize();
            summarize.setConf(context);
            src = new Graph(Graph.defaultGraph());
            src.setPath(tmpDir);
            dest = new Graph(Graph.defaultGraph());
            dest.setPath(getDestination().getPath());
            summarize.setSource(src);
            summarize.setDestination(dest);
            summarize.setMapperNum(getMapperNum());
            summarize.setReducerNum(getReducerNum());
            summarize.execute();
            dirMgr.deleteAll();
        } catch (IOException e) {
            throw new ProcessorExecutionException(e);
        } catch (IllegalAccessException e) {
            throw new ProcessorExecutionException(e);
        }
    }
