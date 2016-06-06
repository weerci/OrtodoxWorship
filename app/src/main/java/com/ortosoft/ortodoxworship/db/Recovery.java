package com.ortosoft.ortodoxworship.db;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import com.ortosoft.ortodoxworship.Model.Group;
import com.ortosoft.ortodoxworship.Model.Member;
import com.ortosoft.ortodoxworship.Model.Worship;
import com.ortosoft.ortodoxworship.common.Pair;
import com.ortosoft.ortodoxworship.common.State;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by admin on 17.05.2016 at 03: 04.
 * Восстановление базы данных пользователей при обновлении программы
 */
class Recovery {

    // region Fields

    // --Commented out by Inspection (07.06.2016 2:43):private final String SERIALIZE_RECOVERY_FILE = "recovery.out";

    private ArrayList<Member> _members;
    private ArrayList<Group> _groups;
    private ArrayList<Pair> _members_groups;
    private ArrayList<Pair> _worships_members;

// --Commented out by Inspection START (07.06.2016 2:43):
//    private ArrayList<Member> get_members() {
//        return _members;
//    }
// --Commented out by Inspection STOP (07.06.2016 2:43)
/*    private ArrayList<Group> get_groups() {
        return _groups;
    }
    private ArrayList<Pair> get_members_groups() {
        return _members_groups;
    }
    private ArrayList<Pair> get_worships_members() {
        return _worships_members;
    }*/

    // endregion

    // Загружает данные из полей в базу
    public void SaveToBase() {

        SQLiteDatabase sd = Connect.Item().get_db();

        sd.beginTransaction();

        sd.delete(Worship.TableWorshipsMembers.NAME, null, null);
        sd.delete(Member.TableMembersGroups.NAME, null, null);
        sd.delete(Member.TableMember.NAME, null, null);
        sd.delete(Group.TableGroup.NAME, null, null);

        try {

            String scriptForMembers = scriptForMembers();
            if (scriptForMembers != null &&!scriptForMembers.isEmpty()) {
                sd.execSQL(scriptForMembers);
            }

            String scriptForGroups = scriptForGroups();
            if (scriptForGroups != null &&!scriptForGroups.isEmpty()) {
                sd.execSQL(scriptForGroups);
            }

            String scriptForMembersGroups = scriptForMembersGroups();
            if (scriptForMembersGroups != null && !scriptForMembersGroups.isEmpty()) {
                sd.execSQL(scriptForMembersGroups);
            }

            String scriptForWorshipsMembers = scriptForWorshipsMembers();
            if (scriptForWorshipsMembers != null && !scriptForWorshipsMembers.isEmpty()) {
                sd.execSQL(scriptForWorshipsMembers);
            }

            sd.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sd.endTransaction();
        }
    }

    // Сохраняет данные в файлах
    /*public void SaveToFile() throws IOException {
        OutputStream fos = App.getContext().openFileOutput(SERIALIZE_RECOVERY_FILE, Context.MODE_PRIVATE);

        ObjectOutputStream oos = new ObjectOutputStream(fos);
        try {
            oos.writeObject(this);
            oos.flush();
        } finally {
            oos.close();
        }
    }*/

    // Сохраняет данные базы в поля класса
    public void LoadFromBase() {
        _members = Member.FindAll();
        _groups = Group.FindAll();
        _members_groups = FindAll(Member.TableMembersGroups.NAME);
        _worships_members = FindAll(Worship.TableWorshipsMembers.NAME);
    }
/*
    public  void LoadFromFiles() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(SERIALIZE_RECOVERY_FILE);
        ObjectInputStream oin = new ObjectInputStream(fis);
        Recovery rec = (Recovery) oin.readObject();

        this._members = rec.get_members();
        this._groups = rec.get_groups();
        this._members_groups = rec.get_members_groups();
        this._worships_members = rec.get_worships_members();
    }*/

    // region Helper

    private String scriptForMembers() {
        if (_members == null || _members.isEmpty())
            return null;

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO members (_id, name, comment, is_dead, baptized) VALUES ");

        for (Member m : _members)
            sb.append(String.format(Locale.US, "(%d, '%s', '%s', %d, %d),", m.get_id(), m.get_name(), m.get_comment(),
                    State.DeadToInt(m.get_isIsDead()), State.BaptizedToInt(m.get_isBaptized())));
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    private String scriptForGroups() {
        if (_groups == null || _groups.isEmpty())
            return null;

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO groups (_id, name) VALUES ");

        for (Group g : _groups)
            sb.append(String.format(Locale.US, "(%d, '%s'),", g.get_id(), g.get_name()));

        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    private String scriptForMembersGroups() {
        if (_members_groups == null || _members_groups.isEmpty())
            return null;

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO members_groups (id_members, id_groups) VALUES ");

        for (Pair p : _members_groups)
            sb.append(String.format(Locale.US, "(%d, %d),", p.get_id1(), p.get_id2()));

        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    private String scriptForWorshipsMembers() {
        if (_worships_members == null || _worships_members.isEmpty())
            return null;

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO worships_members (id_worship, id_member) VALUES ");

        for (Pair p : _worships_members)
            sb.append(String.format(Locale.US, "(%d, %d),", p.get_id1(), p.get_id2()));
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private ArrayList<Pair> FindAll(String tableName) {
        ArrayList<Pair> arr = new ArrayList<>();

        try (Cursor mCursor = Connect.Item().get_db().query(tableName, null, null, null, null, null, null)) {
            mCursor.moveToFirst();
            if (!mCursor.isAfterLast()) {
                do {
                    long id1 = mCursor.getLong(0);
                    long id2 = mCursor.getLong(1);
                    arr.add(new Pair(id1, id2));
                } while (mCursor.moveToNext());
            }
        }
        return arr;
    }

    // endregion


}
