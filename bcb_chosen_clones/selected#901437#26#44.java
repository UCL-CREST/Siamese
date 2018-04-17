    public int findEvent(double miliseconds) {
        double seconds = miliseconds / 1000.0;
        if ((events == null) || events.size() < 1) return -1;
        int a = 0;
        int b = getNumberOfEvents();
        int index = (a + b) / 2;
        while (!(getEvent(index).contains(seconds))) {
            int newIndex = -1;
            if (getEvent(index).getStartTime() > seconds) {
                b = index;
            } else if (getEvent(index).getEndTime() < seconds) {
                a = index;
            } else return index;
            newIndex = (a + b) / 2;
            if (index == newIndex) return -1;
            index = newIndex;
        }
        return index;
    }
