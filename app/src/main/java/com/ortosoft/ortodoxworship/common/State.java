package com.ortosoft.ortodoxworship.common;

/**
 * Created by admin on 16.05.2016.
 */
public class State {
    public enum IsBaptized {unknown, yes, no}
    public enum IsDead {unknown, yes, no}

    // Преобразует значение перечилсения в целое число для сохранения в базе данных
    public static State.IsBaptized IntToBaptized(int value)
    {
        switch (value){
            case 1:
                return IsBaptized.yes;
            case 2:
                return IsBaptized.no;
            default:
                return IsBaptized.unknown;
        }
    }
    // Преобразует целое число в элемент перечисления IsBaptized
    public static int BaptizedToInt(IsBaptized isBaptized)
    {
        switch (isBaptized){
            case yes:
                return 1;
            case no:
                return 2;
            default:
                return 0;
        }
    }
    // Преобразует значение перечилсения в целое число для сохранения в базе данных
    public static State.IsDead IntToDead(int value)
    {
        switch (value){
            case 1:
                return IsDead.yes;
            case 2:
                return IsDead.no;
            default:
                return IsDead.unknown;
        }
    }
    // Преобразует целое число в элемент перечисления IsDead
    public static int DeadToInt(IsDead isDead)
    {
        switch (isDead){
            case yes:
                return 1;
            case no:
                return 2;
            default:
                return 0;
        }
    }

}
