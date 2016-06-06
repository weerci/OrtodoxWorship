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
    public HashMap<Worship.Language, Prayer> Prayers(Worship.Language language){
        if (loadPrayers(language)) {
            return _prayersHash.get()
        };
        return null;
    }

    //region Helper
    private boolean loadPrayers(Worship.Language language){
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

    private boolean load_cks(long id){
        return false;
    }

    private boolean load_eng(long id){
        return false;
    }

    private boolean load_greek(long id){
        return false;
    }

    private boolean load_latin(long id){
        return false;
    }

    private boolean load_rus(long id){
        return false;
    }
    //endregion

}
