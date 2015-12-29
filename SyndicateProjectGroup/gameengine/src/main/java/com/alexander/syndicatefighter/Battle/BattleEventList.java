package com.alexander.syndicatefighter.Battle;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 12/28/15.
 */
public class BattleEventList {
    private List<BattleEventItem> battleEventList = new ArrayList<BattleEventItem>();


    //TODO prototype, a lot improvements needed
    public BattleEventList(){
        BattleEventItem bei = new BattleEventItem("123","trainer","idunno");
        battleEventList.add(bei);
    }

    public List<BattleEventItem> getBattleEventList() {
        return battleEventList;
    }

    public void setBattleEventList(List<BattleEventItem> battleEventList) {
        this.battleEventList = battleEventList;
    }
}
