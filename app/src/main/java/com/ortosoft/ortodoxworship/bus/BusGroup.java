package com.ortosoft.ortodoxworship.bus;

import com.ortosoft.ortodoxworship.Model.Group;
import com.ortosoft.ortodoxworship.Model.Member;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dima on 24.05.2016.
 */
public class BusGroup  {

    private static HashMap<Long, Member> _memberList = new HashMap<>();

    // region Для публикатора
    public static void addGroup(Group group) {
        for (Member m: _memberList.values()) {
            m.OnAddedGroup(group);
        }
    }

    public static void updateGroup(Group group) {
        for (Member m: _memberList.values()) {
            m.OnUpdatedGroup(group);
        }

    }

    public static void deleteGroup(Group group) {
        for (Member m: _memberList.values()) {
            m.OnDeleteGroup(group);
        }
    }
    // endregion


    // region Для слушателя
    public static void addToBus(Member member){
        _memberList.put(member.get_id(), member);
    }
    public static void removeFromBus(Member member){
        _memberList.remove(member.get_id());
    }
    // endregion
}
