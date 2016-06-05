package com.ortosoft.ortodoxworship.bus;

import com.ortosoft.ortodoxworship.Model.Group;
import com.ortosoft.ortodoxworship.Model.Member;

import java.util.HashMap;

/**
 * Created by dima on 05.06.2016.
 */
public class BusMember {

    private HashMap<Long, Group> _groupMap = new HashMap<>();
    public HashMap<Long, Group> get_groupMap() {
        return _groupMap;
    }

    private static BusMember _busMember;

    public static BusMember Item(){
        if (_busMember == null) {
            _busMember = new BusMember();
        }
        return _busMember;
    }

    // region Для публикатора
    public void EventUpdateMember(Member member) {
        for (Group g: _groupMap.values()) {
            g.OnUpdatedMember(member);
        }

    }
    public void EventDeleteMember(Member member) {
        for (Group g: _groupMap.values()) {
            g.OnDeleteMember(member);
        }
    }
    // endregion

    // region Для слушателя
    public void AddToBus(Group group){
        _groupMap.put(group.get_id(), group);
    }
    public void RemoveFromBus(Group group){
        _groupMap.remove(group.get_id());
    }
    // endregion

}
