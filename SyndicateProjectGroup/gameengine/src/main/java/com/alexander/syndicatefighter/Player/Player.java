package com.alexander.syndicatefighter.Player;

import com.alexander.syndicatefighter.Backpack;
import com.alexander.syndicatefighter.Worker.Worker;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

public class Player {

    private String Uid;
    private String provider;
    private String name;
    private Gender gender;
    private long cash;
    private Status status;
    private Backpack backpack;
    private List<Worker> workerList;
    private String avatar;   //TODO: Currently defined as string. Is it ok? Any better option?


    //region Constructors
    public Player() {
        this("123", "google", "Newplayer Changeyourname");      //TODO: 1)auto increment for ID in database, 2)default avatar and email
    }

//    public Player(long Uid, String email, String name, String avatar) {
//        this(Uid, email, name, Gender.Neither, 0L, Status.New, new Backpack(), new ArrayList<Worker>(), avatar);
//    }

    public Player(String Uid, String provider, String name){
        this(Uid, provider, name, Gender.Neither, 0L, Status.New, new Backpack(), new ArrayList<Worker>(), "DEFAULT_AVATAR");
    }

    private Player(String Uid, String provider,  String name, Gender gender, long cash, Status status, Backpack backpack, List<Worker> workerList, String avatar) {
        this.Uid = Uid;
        this.provider = provider;
        this.name = name;
        this.gender = gender;
        this.cash = cash;
        this.status = status;
        this.backpack = backpack;
        this.workerList = workerList;
        this.avatar = avatar;
    }
    //endregion

    //region Public Methods for Properties
    public String getUid() {
        return this.Uid;
    }

    public void setUid(String uid) {
        this.Uid = uid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public long getCash() {
        return this.cash;
    }

    public void setCash(long cash) {
        this.cash = cash < 0 ? 0 : cash;
    }

    public void increaseCash(long increase) {
        if (increase >= 0)
            this.cash += increase;
        else
            //if add <0 , then call decrease
            decreaseCash(-increase);
    }

    public void decreaseCash(long decrease) {
        this.cash -= decrease;
        //If now cash is below 0, then set it to 0
        this.cash = this.cash < 0 ? 0 : this.cash;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Backpack getBackpack() {
        return this.backpack;
    }

    public void setBackpack(Backpack backpack) {
        this.backpack = backpack;
    }

    public List<Worker> getWorkerList() {
        return this.workerList;
    }

    public void setWorkerList(List<Worker> workerList) {
        this.workerList = workerList;
    }

    //endregion

    public void activate() {
        try {
            this.status = Status.Normal;
            //what if user was InBattle or DoNotDisturb? Do we use this method to force user back to normal or just leave it?
        } catch (Exception ex) {
        }
    }

    public void deactivate() {
        this.status = Status.InActive;
    }

    public boolean canBattle() {
        return this.status == Status.Normal;
    }

    public String toJSON() {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(this);
        return jsonStr;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
