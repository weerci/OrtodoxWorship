package com.ortosoft.ortodoxworship.bus;

import android.support.v4.util.LongSparseArray;

import com.ortosoft.ortodoxworship.Model.Group;
import com.ortosoft.ortodoxworship.Model.Member;


/**
 * Created by dima on 05.06.2016.
 */
public class BusMember {

    private LongSparseArray<Group> _groupMap = new LongSparseArray<>();
    public LongSparseArray<Group> get_groupMap() {
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
        for (int i = 0; i < _groupMap.size(); i++) {
            _groupMap.valueAt(i).OnUpdatedMember(member);
        }

    }
    public void EventDeleteMember(Member member) {
        for (int i = 0; i < _groupMap.size(); i++) {
            _groupMap.valueAt(i).OnDeleteMember(member);
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
