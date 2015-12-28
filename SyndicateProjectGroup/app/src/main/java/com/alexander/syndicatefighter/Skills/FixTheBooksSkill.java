package com.alexander.syndicatefighter.Skills;

import com.alexander.syndicatefighter.Worker.Worker;

/**
 * Created by alexandernohe on 11/14/15.
 */
public class FixTheBooksSkill extends Skills {

    public FixTheBooksSkill(String Name, int MaxNRG, int Cooldown, int CostPerAttach, int CurrentNRG)
    {
        super(Name, MaxNRG, Cooldown, CostPerAttach, CurrentNRG);
    }

    public FixTheBooksSkill()
    {
        super("Fix the Books", 25, 0, 1, 25);
    }

    public FixTheBooksSkill(int CurrentNRG)
    {
        super("Fix the Books", CurrentNRG, 0, 1, 25);
    }

    @Override
    public void Action(Worker attackingWorker, Worker defendingWorker) {
        if (this.currentNRG == 0)
        {
            //Maybe we could throw a custom exception here
        }
        else {
            attackingWorker.adjustHP((int) (2*attackingWorker.getMaxHP()));
        }
    }
}
