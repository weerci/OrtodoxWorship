package com.ortosoft.ortodoxworship.bus;

import com.ortosoft.ortodoxworship.Model.Group;
import com.ortosoft.ortodoxworship.Model.Member;

import java.util.ArrayList;

/**
 * Created by dima on 24.05.2016.
 */
public class BusGroup implements IGroup {

    private static ArrayList<Member> _memberList = new ArrayList<>();

    @Override
    public void addGroup(Group group) {
        for (Member m: _memberList) {
            m.OnAddedGroup(group);
        }
    }

    @Override
    public void updateGroup(Group group) {
        for (Member m: _memberList) {
            m.OnUpdatedGroup(group);
        }

    }

    @Override
    public void deleteGroup(Group group) {
        for (Member m: _memberList) {
            m.OnDeleteGroup(group);
        }

    }

    public static void addToBus(Member member){
        _memberList.add(member);
    }

    public static void removeFromBus(Member member){
        _memberList.remove(member);
    }
}
