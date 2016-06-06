package com.ortosoft.ortodoxworship.bus;

import com.ortosoft.ortodoxworship.Model.Member;

/**
 * Created by dima on 05.06.2016 at 03: 03.
 * Событися для класса member
 */
@SuppressWarnings("unused")
public interface EventMember {
    void OnUpdatedMember(Member member);
    void OnDeleteMember(Member member);
}
