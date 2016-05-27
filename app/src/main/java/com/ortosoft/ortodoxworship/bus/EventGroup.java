package com.ortosoft.ortodoxworship.bus;

import com.ortosoft.ortodoxworship.Model.Group;

/**
 * Created by dima on 25.05.2016.
 */
public interface EventGroup {

    void OnAddedGroup(Group group);
    void OnUpdatedGroup(Group group);
    void OnDeleteGroup(Group group);

}
