    public void bubbleSort(int[] arr) {
        BasicProcessor.getInstance().getStartBlock();
        BasicProcessor.getInstance().getVarDeclaration();
        boolean swapped = true;
        BasicProcessor.getInstance().getVarDeclaration();
        int j = 0;
        BasicProcessor.getInstance().getVarDeclaration();
        int tmp;
        {
            BasicProcessor.getInstance().getWhileStatement();
            while (swapped) {
                BasicProcessor.getInstance().getStartBlock();
                swapped = false;
                j++;
                {
                    BasicProcessor.getInstance().getForStatement();
                    for (int i = 0; i < arr.length - j; i++) {
                        BasicProcessor.getInstance().getStartBlock();
                        {
                            BasicProcessor.getInstance().getIfStatement();
                            if (arr[i] > arr[i + 1]) {
                                BasicProcessor.getInstance().getStartBlock();
                                tmp = arr[i];
                                arr[i] = arr[i + 1];
                                arr[i + 1] = tmp;
                                swapped = true;
                                BasicProcessor.getInstance().getEndBlock();
                            }
                        }
                        BasicProcessor.getInstance().getEndBlock();
                    }
                }
                BasicProcessor.getInstance().getEndBlock();
            }
        }
        BasicProcessor.getInstance().getEndBlock();
    }
