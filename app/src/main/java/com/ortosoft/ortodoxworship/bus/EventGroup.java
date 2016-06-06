package com.ortosoft.ortodoxworship.bus;

import com.ortosoft.ortodoxworship.Model.Group;

/**
 * Created by dima on 25.05.2016 at 03: 03.
 * События для класса Group
 */
@SuppressWarnings("unused")
public interface EventGroup {

    void OnUpdatedGroup(Group group);
    void OnDeleteGroup(Group group);

}
