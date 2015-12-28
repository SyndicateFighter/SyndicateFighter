package com.alexander.syndicatefighter.Player;

/**
 * Created by yuan8014 on 11/20/2015.
 */

public enum Status
{
    New,        //just registered, not activated or having my identity confirmed (do we want to limit 1)to Esri employees only? 2)one account per person?)
    Normal,     //can start a battle or chat with me
    InBattle,       //in battle right now
    DoNotDisturb,        //cannot start a battle or chat with me
    InActive        //my account is not active, need to be activated by admin
}
