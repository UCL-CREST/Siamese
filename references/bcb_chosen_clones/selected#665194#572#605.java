    public RobotList<Enemy> sort_decr_Enemy(RobotList<Enemy> list, String field) {
        int length = list.size();
        Index_value[] enemy_dist = new Index_value[length];
        if (field.equals("") || field.equals("location")) {
            Location cur_loc = this.getLocation();
            for (int i = 0; i < length; i++) {
                enemy_dist[i] = new Index_value(i, distance(cur_loc, list.get(i).location));
            }
        } else if (field.equals("health")) {
            for (int i = 0; i < length; i++) {
                enemy_dist[i] = new Index_value(i, list.get(i).health);
            }
        } else {
            say("impossible to sort list - nothing modified");
            return list;
        }
        boolean permut;
        do {
            permut = false;
            for (int i = 0; i < length - 1; i++) {
                if (enemy_dist[i].value < enemy_dist[i + 1].value) {
                    Index_value a = enemy_dist[i];
                    enemy_dist[i] = enemy_dist[i + 1];
                    enemy_dist[i + 1] = a;
                    permut = true;
                }
            }
        } while (permut);
        RobotList<Enemy> new_enemy_list = new RobotList<Enemy>(Enemy.class);
        for (int i = 0; i < length; i++) {
            new_enemy_list.addLast(list.get(enemy_dist[i].index));
        }
        return new_enemy_list;
    }
