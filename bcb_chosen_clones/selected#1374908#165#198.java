    @SuppressWarnings("deprecation")
    public void testMemberIterator() throws IOException {
        GZIPMembersInputStream gzin = new GZIPMembersInputStream(new ByteArrayInputStream(allfour_gz));
        Iterator<GZIPMembersInputStream> iter = gzin.memberIterator();
        assertTrue(iter.hasNext());
        GZIPMembersInputStream gzMember0 = iter.next();
        int count0 = IOUtils.copy(gzMember0, new NullOutputStream());
        assertEquals("wrong 1k member count", 1024, count0);
        assertEquals("wrong member number", 0, gzin.getMemberNumber());
        assertEquals("wrong member0 start", 0, gzin.getCurrentMemberStart());
        assertEquals("wrong member0 end", noise1k_gz.length, gzin.getCurrentMemberEnd());
        assertTrue(iter.hasNext());
        GZIPMembersInputStream gzMember1 = iter.next();
        int count1 = IOUtils.copy(gzMember1, new NullOutputStream());
        assertEquals("wrong 32k member count", (32 * 1024), count1);
        assertEquals("wrong member number", 1, gzin.getMemberNumber());
        assertEquals("wrong member1 start", noise1k_gz.length, gzin.getCurrentMemberStart());
        assertEquals("wrong member1 end", noise1k_gz.length + noise32k_gz.length, gzin.getCurrentMemberEnd());
        assertTrue(iter.hasNext());
        GZIPMembersInputStream gzMember2 = iter.next();
        int count2 = IOUtils.copy(gzMember2, new NullOutputStream());
        assertEquals("wrong 1-byte member count", 1, count2);
        assertEquals("wrong member number", 2, gzin.getMemberNumber());
        assertEquals("wrong member2 start", noise1k_gz.length + noise32k_gz.length, gzin.getCurrentMemberStart());
        assertEquals("wrong member2 end", noise1k_gz.length + noise32k_gz.length + a_gz.length, gzin.getCurrentMemberEnd());
        assertTrue(iter.hasNext());
        GZIPMembersInputStream gzMember3 = iter.next();
        int count3 = IOUtils.copy(gzMember3, new NullOutputStream());
        assertEquals("wrong 5-byte member count", 5, count3);
        assertEquals("wrong member number", 3, gzin.getMemberNumber());
        assertEquals("wrong member3 start", noise1k_gz.length + noise32k_gz.length + a_gz.length, gzin.getCurrentMemberStart());
        assertEquals("wrong member3 end", noise1k_gz.length + noise32k_gz.length + a_gz.length + hello_gz.length, gzin.getCurrentMemberEnd());
        assertFalse(iter.hasNext());
    }
