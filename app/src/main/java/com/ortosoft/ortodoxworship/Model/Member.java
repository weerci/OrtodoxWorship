package com.ortosoft.ortodoxworship.Model;

import com.ortosoft.ortodoxworship.common.State;

/**
 * Created by admin on 16.05.2016.
 */
public class Member
{
    private String _name;
    public String get_name() {
        return _name;
    }
    public void set_name(String _name) {
        this._name = _name;
    }

    private String _description;
    public String get_description() {
        return _description;
    }
    public void set_description(String _description) {
        this._description = _description;
    }

    private State.IsBaptized _isBaptized = State.IsBaptized.unknown;
    public State.IsBaptized get_isBaptized() {
        return _isBaptized;
    }
    public void set_isBaptized(State.IsBaptized _isBaptized) {
        this._isBaptized = _isBaptized;
    }

    private State.IsDead _isIsDead = State.IsDead.unknown;
    public State.IsDead get_isIsDead() {
        return _isIsDead;
    }
    public void set_isIsDead(State.IsDead _isIsDead) {
        this._isIsDead = _isIsDead;
    }

    // region Constructors
    public Member(String name, String description, State.IsBaptized isBaptized, State.IsDead isDead) {
        _name = name;
        _description = description;
        _isBaptized = isBaptized;
        _isIsDead = isDead;
    }
    public Member(String name, String description) {
        _name = name;
        _description = description;
    }
    // endregion
}
