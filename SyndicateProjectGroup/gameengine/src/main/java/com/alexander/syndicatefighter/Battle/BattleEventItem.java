package com.alexander.syndicatefighter.Battle;

/**
 * Created by Andrew on 12/28/15.
 */
public class BattleEventItem {

    //TODO not well thought out yet
    private String id;
    private String battleType;
    private String name;

    public BattleEventItem(String id, String battleType, String name){
        this.id = id;
        this.battleType = battleType;
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBattleType() {
        return battleType;
    }

    public void setBattleType(String battleType) {
        this.battleType = battleType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
