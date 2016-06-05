package com.ortosoft.ortodoxworship.bus;

import com.ortosoft.ortodoxworship.Model.Member;

/**
 * Created by dima on 05.06.2016.
 */
public interface EventMember {
    void OnUpdatedMember(Member member);
    void OnDeleteMember(Member member);
}
