    private ArrayList<Move> shuffle(ArrayList<Move> list) {
        Random random = new Random();
        for (int index = (list.size() - 1); index > 0; index--) {
            int other = random.nextInt(index + 1);
            Move temp = list.get(other);
            list.set(other, list.get(index));
            list.set(index, temp);
        }
        return list;
    }
