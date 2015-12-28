package com.alexander.syndicatefighter.Worker;

import com.alexander.syndicatefighter.Skills.Skills;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex7370 on 11/5/2015.
 */
public class Worker {
    private int level;
    private long currentXP;
    private long nextXP;
    private TeamType EmpType;
    private String name;
    private long currentHP;
    private long maxHP;
    private boolean alive;
    private List<Skills> skillsList = new ArrayList<Skills>();
    private double percentOfHealthRemaining;

    public Worker(String Name, long xp, TeamType TypeOfCharacter)
    {
        this.name = Name;
        this.EmpType = TypeOfCharacter;
        this.currentXP = 0;
        this.level = 1;
        this.generateMaxHP();
        this.nextXP = 222;
        this.adjustXP(xp);
        this.currentHP = this.maxHP;
        this.alive = true;
        this.percentOfHealthRemaining = 1;
    }

    public Worker()
    {
        this("David", 25, TeamType.DEV);
    }

    public Worker(String Name)
    {
        this(Name, 25, TeamType.DEV);
    }

    public Worker(String Name, long xp)
    {
        this(Name, xp, TeamType.DEV);
    }

    public Worker(String Name, TeamType TypeOfCharacter)
    {
        this(Name, 25, TypeOfCharacter);
    }

    public int getLevel()
    {
        return this.level;
    }

    public long getCurrentXP()
    {
        return this.currentXP;
    }

    public void adjustXP(long xp)
    {
        if ((this.currentXP + xp) > this.nextXP)
        {
            if (this.level < 99) {
                this.level++;
                this.generateMaxHP();
            }
            xp -= this.nextXP;
            this.currentXP += this.nextXP;
            this.nextXP += 222*this.level;
            adjustXP(xp);
        }
        else {
            this.currentXP+=xp;
        }
    }

    public TeamType getType()
    {
        return this.EmpType;
    }

    public String getName()
    {
        return this.name;
    }

    public long getMaxHP() {
        return this.maxHP;
    }

    public long getCurrentHP() {
        return this.currentHP;
    }

    private void generateMaxHP() {
        if (this.level == 1)
        {
            this.maxHP = 25;
        }
        else {
            this.maxHP += 2 * this.level;
            this.currentHP += 2*this.level;
        }
    }

    public boolean getAliveStatus()
    {
        return this.alive;
    }

    public void adjustHP(int changeInHP)
    {
        if(changeInHP<0)
            if ((-changeInHP) >= this.currentHP) {
                this.alive = false;
                this.currentHP = 0;
            } else {
                this.currentHP += changeInHP;
                this.percentOfHealthRemaining = (double)((double)(this.currentHP) / (double)(this.maxHP));
            }
        else
        {
            if (changeInHP >= (this.maxHP - this.currentHP))
            {
                this.currentHP = this.maxHP;
            }
            else
            {
                this.currentHP += changeInHP;
            }
        }
    }

    public void addSkill(Skills skill)
    {
        skillsList.add(skill);
    }

    public void removeSkill(int skillIndex)
    {
        skillsList.remove(skillIndex);
    }

    public Skills getSkill(int skillIndex)
    {
        return skillsList.get(skillIndex);
    }

    public int getSkillCount()
    {
        return skillsList.size();
    }

    public double getPercentOfHealthRemaining() {
        return this.percentOfHealthRemaining;
    }
}
