package com.ortosoft.ortodoxworship.bus;

import android.support.v4.util.LongSparseArray;

import com.ortosoft.ortodoxworship.Model.Group;
import com.ortosoft.ortodoxworship.Model.Member;


/**
 * Created by ${USER} on ${DATE} at 03 at 03: 05.
 * Шина событий для групп, сообщает пользователям member об изменении и удалении группы
 */
public class BusGroup  {

    private final LongSparseArray<Member> _memberList = new LongSparseArray<>();
    public LongSparseArray<Member> get_memberList() {
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
    public void EventUpdateGroup(Group group) {
        for (int i = 0; i < _memberList.size(); i++) {
            _memberList.valueAt(i).OnUpdatedGroup(group);
        }
    }
    public void EventDeleteGroup(Group group) {
        for (int i = 0; i < _memberList.size(); i++) {
            _memberList.valueAt(i).OnDeleteGroup(group);
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
