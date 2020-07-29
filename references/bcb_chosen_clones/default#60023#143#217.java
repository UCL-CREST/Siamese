    private int CalculateTreeWidth(int myGraph[][], int SolutionVector[], int zaehler) {
        int[][] DecompositionedMatrix;
        int[] ConnectedNodes;
        int[] NextSolutionVector;
        int TreeWidth;
        int NumVerticesOfMyGraph;
        int DeeperTreeWidth;
        int LineIndex;
        NumVerticesOfMyGraph = myGraph.length;
        DecompositionedMatrix = new int[NumVerticesOfMyGraph - 1][NumVerticesOfMyGraph - 1];
        ConnectedNodes = new int[NumVerticesOfMyGraph];
        TreeWidth = 0;
        DeeperTreeWidth = 0;
        LineIndex = 0;
        NextSolutionVector = new int[SolutionVector.length - 1];
        for (int i = 1; i < NumVerticesOfMyGraph; i++) {
            if (SolutionVector[0] == myGraph[i][0]) {
                LineIndex = i;
            }
        }
        for (int j = 1; j < NumVerticesOfMyGraph; j++) {
            if (myGraph[LineIndex][j] == 1) {
                ConnectedNodes[TreeWidth] = myGraph[0][j];
                TreeWidth++;
            }
        }
        for (int i = 0; i < NumVerticesOfMyGraph; i++) {
            if (i < LineIndex) {
                for (int j = 0; j < NumVerticesOfMyGraph; j++) {
                    if (j < LineIndex) {
                        DecompositionedMatrix[i][j] = myGraph[i][j];
                    } else if (j > LineIndex) {
                        DecompositionedMatrix[i][j - 1] = myGraph[i][j];
                    }
                }
            } else if (i > LineIndex) {
                for (int j = 0; j < NumVerticesOfMyGraph; j++) {
                    if (j < LineIndex) {
                        DecompositionedMatrix[i - 1][j] = myGraph[i][j];
                    } else if (j > LineIndex) {
                        DecompositionedMatrix[i - 1][j - 1] = myGraph[i][j];
                    }
                }
            }
        }
        for (int k = 0; k < TreeWidth; k++) {
            for (int i = 0; i < DecompositionedMatrix.length; i++) {
                if (DecompositionedMatrix[i][0] == ConnectedNodes[k]) {
                    for (int l = 0; l < TreeWidth; l++) {
                        for (int j = 0; j < DecompositionedMatrix.length; j++) {
                            if (DecompositionedMatrix[0][j] == ConnectedNodes[l]) {
                                DecompositionedMatrix[i][j] = 1;
                            }
                        }
                    }
                }
            }
        }
        for (int i = 1; i < DecompositionedMatrix.length; i++) {
            DecompositionedMatrix[i][i] = 0;
            for (int j = 1; j < DecompositionedMatrix.length; j++) {
                DecompositionedMatrix[i][j] = DecompositionedMatrix[j][i];
            }
        }
        for (int l = 1; l < SolutionVector.length; l++) {
            NextSolutionVector[l - 1] = SolutionVector[l];
        }
        if (NextSolutionVector.length > 1) {
            DeeperTreeWidth = CalculateTreeWidth(DecompositionedMatrix, NextSolutionVector, zaehler + 1);
            if (TreeWidth < DeeperTreeWidth) {
                TreeWidth = DeeperTreeWidth;
            }
        }
        return TreeWidth;
    }
