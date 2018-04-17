    public int init_topology(String topo_file) {
        int ret = 0;
        _arrivals_are_random = 0;
        _departures_are_random = 0;
        _seed_is_cable = true;
        _tracker_type = 0;
        for (int i = 0; i < DEGREES_OF_CLOSENESS; i++) {
            g_closeness[i] = 0;
        }
        switch(_tracker_type) {
            case NORMAL_TRACKER:
                break;
            case SUB_TRACKER:
                _num_external_peers = 0;
                break;
            case SUB_LIMIT_TRACKER:
                _num_external_peers = 0;
                break;
            case UNIV_TRACKER:
                _num_external_peers = 0;
                break;
        }
        _tracker = new BT_Tracker();
        univ_sub = new BT_Subtracker();
        univ_sub.set_tracker(_tracker);
        _num_univ_nodes = UNIV_NODE_NUM;
        if (_num_univ_nodes > 0) {
            for (int i = 0; i < _num_univ_nodes; i++) {
                Univ_Node u_n = new Univ_Node(i, univ_sub);
                BT_Peer peer = new BT_Peer(i, BT_Peer.UNIV_NODE, 1, -1);
                peer.un = u_n;
                _univ_node.addLast(u_n);
            }
        }
        _num_as = AS_NUM;
        for (int i = 0; i < _num_as; i++) {
            As new_as = new As(i);
            int gateway_node_idx = _num_headend_nodes;
            new_as.low_client_id = _num_univ_nodes + _num_cable_leaf_nodes;
            new_as.num_nodes = _as_num_node;
            _num_headend_nodes += new_as.num_nodes;
            for (int j = gateway_node_idx; j < _num_headend_nodes; j++) {
                Headend_Node h_n = new Headend_Node(j);
                h_n.low_client_id = _num_univ_nodes + _num_cable_leaf_nodes;
                _num_cable_leaf_nodes += _as_office_fanout;
                h_n.hi_client_id = h_n.low_client_id + _as_office_fanout - 1;
                h_n.my_as = new_as;
                new_as.as_handend_node.addLast(h_n);
                _headend_node.addLast(h_n);
            }
            new_as.hi_client_id = _num_univ_nodes + _num_cable_leaf_nodes - 1;
            new_as.subtracker.set_tracker(_tracker);
            _as.addLast(new_as);
        }
        for (int i = 0; i < _num_cable_leaf_nodes; i++) {
            int id = i + _num_univ_nodes;
            Cable_Leaf_Node cln = new Cable_Leaf_Node(id);
            As as = Id_id_as(id);
            BT_Peer peer = new BT_Peer(id, BT_Peer.CABLE_NODE, 1, as.as_id);
            peer.cln = cln;
            _cable_leaf_node.addLast(cln);
        }
        init_all_nodes();
        num_clents = _num_univ_nodes + _num_cable_leaf_nodes;
        if (BT_Peer.instances.size() != num_clents) {
            System.out.println("_peers.size() != num_clents");
            System.exit(1);
        }
        _delay_matrix = new int[num_clents][num_clents];
        for (Iterator<Integer> it = BT_Peer.instances.keySet().iterator(); it.hasNext(); ) {
            int src_id = (Integer) it.next();
            BT_Peer src = BT_Peer.instance(src_id);
            for (Iterator<Integer> j = BT_Peer.instances.keySet().iterator(); j.hasNext(); ) {
                int dest_id = (Integer) j.next();
                if (src_id == dest_id) {
                    _delay_matrix[src_id][dest_id] = 0;
                    continue;
                }
                BT_Peer dest = BT_Peer.instance(dest_id);
                LinkedList<Link> all_link = link_traveler(src, dest);
                for (Iterator<Link> i = all_link.iterator(); i.hasNext(); ) {
                    Link l_tmp = (Link) i.next();
                    _delay_matrix[src_id][dest_id] += l_tmp.delay_ms;
                }
            }
        }
        for (Iterator<Integer> it = BT_Peer.instances.keySet().iterator(); it.hasNext(); ) {
            int peer_id = (Integer) it.next();
            BT_Peer bp = BT_Peer.instance(peer_id);
            bp.get_landmark();
        }
        _connection_map = new Conn[num_clents][num_clents];
        for (int i = 0; i < num_clents; i++) {
            for (int j = 0; j < num_clents; j++) {
                _connection_map[i][j] = null;
            }
        }
        _closeness_matrix = new int[num_clents][num_clents];
        for (int i = 0; i < num_clents; i++) {
            for (int j = 0; j < num_clents; j++) {
                _closeness_matrix[i][j] = CLOSE_NULL;
            }
        }
        for (Iterator<Integer> src_it = BT_Peer.instances.keySet().iterator(); src_it.hasNext(); ) {
            int src_id = (Integer) src_it.next();
            for (Iterator<Integer> dest_it = BT_Peer.instances.keySet().iterator(); dest_it.hasNext(); ) {
                int dest_id = (Integer) dest_it.next();
                _closeness_matrix[src_id][dest_id] = get_cloessness(src_id, dest_id);
                _closeness_matrix[dest_id][src_id] = _closeness_matrix[src_id][dest_id];
            }
        }
        _link_vec.clear();
        _xfer_vec.clear();
        return ret;
    }
