    protected boolean fillN(List<Position> sequence) {
        int length = sequence.size();
        n = new int[length][];
        for (int row = 0; row < length; row++) {
            n[row] = new int[length];
            for (int column = 0; column < length; column++) {
                n[row][column] = 0;
            }
        }
        int start = 0;
        int end;
        Position controlPosition, startPosition;
        Node firstNode, currentNode, currentNode2;
        Iterator<Node> itPaths;
        Iterator<Position> itForward = sequence.iterator();
        ListIterator<Position> itInner;
        ListIterator<Position> itInner2;
        while (itForward.hasNext()) {
            startPosition = itForward.next();
            itPaths = startPosition.nodes(true).iterator();
            while (itPaths.hasNext()) {
                end = start;
                firstNode = itPaths.next();
                currentNode = firstNode;
                itInner = sequence.listIterator(start);
                if (itInner.hasNext()) itInner.next();
                while (true) {
                    if (!itInner.hasNext()) break;
                    if (!currentNode.hasNextNode()) break;
                    end++;
                    controlPosition = itInner.next();
                    currentNode = currentNode.getNextNode();
                    if (controlPosition.matches(currentNode.getToken())) {
                        n[end][start]++;
                    } else {
                        break;
                    }
                }
            }
            start++;
        }
        itForward = sequence.iterator();
        boolean symmetric = true;
        while (itForward.hasNext()) if (!(itForward.next() instanceof MexGraph<?>.RegularPosition<?>)) symmetric = false;
        if (symmetric) {
            for (int row = 0; row < length; row++) {
                for (int column = row; column < length; column++) {
                    n[row][column] = n[column][row];
                }
            }
        } else {
            ListIterator<Position> itBackward = sequence.listIterator(sequence.size());
            while (itBackward.hasPrevious()) {
                start = itBackward.previousIndex();
                startPosition = itBackward.previous();
                itPaths = startPosition.nodes(false).iterator();
                while (itPaths.hasNext()) {
                    end = start;
                    firstNode = itPaths.next();
                    currentNode = firstNode;
                    itInner = sequence.listIterator(start + 1);
                    if (itInner.hasPrevious()) itInner.previous();
                    while (true) {
                        if (!itInner.hasPrevious()) break;
                        if (!currentNode.hasPreviousNode()) break;
                        end = itInner.previousIndex();
                        controlPosition = itInner.previous();
                        currentNode = currentNode.getPreviousNode();
                        if (controlPosition.matches(currentNode.getToken())) {
                            n[end][start]++;
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        Iterator<Position> iterator = sequence.iterator();
        for (int c = 0; iterator.hasNext(); c++) {
            controlPosition = iterator.next();
            n[c][c] = controlPosition.numberOfNodes();
        }
        iterator = sequence.iterator();
        Position position;
        boolean slotsFilled = true;
        while (iterator.hasNext()) {
            position = iterator.next();
            if (position instanceof MexGraph<?>.SlotPosition<?>) if (((SlotPosition) position).getTokens().size() < 1) slotsFilled = false;
        }
        return slotsFilled;
    }
