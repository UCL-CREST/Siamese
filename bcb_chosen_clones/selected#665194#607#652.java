    public RobotList<Resource> sort_incr_Resource(RobotList<Resource> list, String field) {
        int length = list.size();
        Index_value[] resource_dist = new Index_value[length];
        if (field.equals("") || field.equals("location")) {
            Location cur_loc = this.getLocation();
            for (int i = 0; i < length; i++) {
                resource_dist[i] = new Index_value(i, distance(cur_loc, list.get(i).location));
            }
        } else if (field.equals("energy")) {
            for (int i = 0; i < length; i++) {
                resource_dist[i] = new Index_value(i, list.get(i).energy);
            }
        } else if (field.equals("ammostash")) {
            for (int i = 0; i < length; i++) {
                resource_dist[i] = new Index_value(i, list.get(i).ammostash);
            }
        } else if (field.equals("speed")) {
            for (int i = 0; i < length; i++) {
                resource_dist[i] = new Index_value(i, list.get(i).speed);
            }
        } else if (field.equals("health")) {
            for (int i = 0; i < length; i++) {
                resource_dist[i] = new Index_value(i, list.get(i).health);
            }
        } else {
            say("impossible to sort list - nothing modified");
            return list;
        }
        boolean permut;
        do {
            permut = false;
            for (int i = 0; i < length - 1; i++) {
                if (resource_dist[i].value > resource_dist[i + 1].value) {
                    Index_value a = resource_dist[i];
                    resource_dist[i] = resource_dist[i + 1];
                    resource_dist[i + 1] = a;
                    permut = true;
                }
            }
        } while (permut);
        RobotList<Resource> new_resource_list = new RobotList<Resource>(Resource.class);
        for (int i = 0; i < length; i++) {
            new_resource_list.addLast(list.get(resource_dist[i].index));
        }
        return new_resource_list;
    }
