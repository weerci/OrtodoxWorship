package com.ortosoft.ortodoxworship.common;

import com.ortosoft.ortodoxworship.Model.Prayer;
import com.ortosoft.ortodoxworship.Model.Worship;

import java.util.HashMap;

/**
 * Created by dima on 06.06.2016.
 */
public class LanguageHash {

    private long _id;

    public LanguageHash(long _id) {
        this._id = _id;
    }

    private HashMap<Worship.Language, HashMap<Long, Prayer>> _prayersHash = new HashMap();
    public HashMap<Long, Prayer> Prayers(Worship.Language language){
        return loadPrayers(language);
    }

    //region Helper
    private HashMap<Long, Prayer> loadPrayers(Worship.Language language){
        switch (language){
            case cks:
                return load_cks(_id);
            case eng:
                return load_eng(_id);
            case greek:
                return load_greek(_id);
            case latin:
                return load_latin(_id);
            default:
                return load_rus(_id);
        }
    }

    private HashMap<Long, Prayer> load_cks(long id){
        HashMap<Long, Prayer> _prayers = _prayersHash.get(id);
        if (_prayers == null) {

        };
    }

    private HashMap<Long, Prayer> load_eng(long id){
        return null;
    }

    private HashMap<Long, Prayer> load_greek(long id){
        return null;
    }

    private HashMap<Long, Prayer> load_latin(long id){
        return null;
    }

    private HashMap<Long, Prayer> load_rus(long id){
        return null;
    }
    //endregion


}
