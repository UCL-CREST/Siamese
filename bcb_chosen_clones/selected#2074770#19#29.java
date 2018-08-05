    @Override
    public double getDistance(JPacket packet1, JPacket packet2) {
        int packet1Id = packet1.getId();
        int packet2Id = packet2.getId();
        if (distances[packet1Id][packet2Id] == -1) {
            setDistance(packet1, packet2);
            distances[packet2Id][packet1Id] = distances[packet1Id][packet2Id];
        } else {
        }
        return distances[packet1Id][packet2Id];
    }
