package com.ortosoft.ortodoxworship.bus;

import com.ortosoft.ortodoxworship.Model.Group;
import com.ortosoft.ortodoxworship.Model.Member;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dima on 24.05.2016.
 */
public class BusGroup  {

    private HashMap<Long, Member> _memberList = new HashMap<>();
    public HashMap<Long, Member> get_memberList() {
        return _memberList;
    }

    private static BusGroup _busGroup;

    public static BusGroup Item(){
        if (_busGroup == null) {
            _busGroup = new BusGroup();
        }
        return _busGroup;
    }

    // region Для публикатора
    public void EventAddGroup(Group group) {
        for (Member m: _memberList.values()) {
            m.OnAddedGroup(group);
        }
    }
    public void EventUpdateGroup(Group group) {
        for (Member m: _memberList.values()) {
            m.OnUpdatedGroup(group);
        }

    }
    public void EventDeleteGroup(Group group) {
        for (Member m: _memberList.values()) {
            m.OnDeleteGroup(group);
        }
    }
    // endregion

    // region Для слушателя
    public void AddToBus(Member member){
        _memberList.put(member.get_id(), member);
    }
    public void RemoveFromBus(Member member){
        _memberList.remove(member.get_id());
    }
    // endregion

}
