package com.ortosoft.ortodoxworship.bus;

import com.ortosoft.ortodoxworship.Model.Group;

/**
 * Created by dima on 24.05.2016.
 */
public interface IGroup {

    public void addGroup(Group group);
    public void updateGroup(Group group);
    public void deleteGroup(Group group);

}
