package com.alexander.syndicatefighter.Skills;

import com.alexander.syndicatefighter.Worker.Worker;

/**
 * Created by alex7370 on 11/9/2015.
 */
abstract public class Skills {
    protected String name;
    protected int currentNRG;
    protected int maxNRG;
    protected int cooldown;
    protected int costNRG;

    public Skills(String Name, int MaxNRG, int Cooldown, int CostPerAttach, int CurrentNRG)
    {
        this.name = Name;
        this.maxNRG = MaxNRG;
        this.currentNRG = CurrentNRG;
        this.cooldown = Cooldown;
        this.costNRG = CostPerAttach;

        // When writing subclasses, use the following format of constructor
        // class SubClass extends com.alexander.syndicatefighter.Skills.Skills
        // {
        //    public SubClass(String Name, int MaxNRG, int Cooldown, int CostPerAttach)
        //     {   super(String Name, int MaxNRG, int Cooldown, int CostPerAttach);   }
        // }
    }

    abstract public void Action(Worker attackingWorker, Worker defendingWorker);

    final public String getName()
    {
        return this.name;
    }

    final public int getCurrentNRG()
    {
        return this.currentNRG;
    }

    final public int getMaxNRG()
    {
        return this.maxNRG;
    }

    final public int getCooldown()
    {
        return this.cooldown;
    }

    final public void adjustNRG(int amountToAdjust)
    {
        if (amountToAdjust < 0)
        {
            if (-(amountToAdjust) > this.currentNRG)
            {
                //throw custom exception
            }
            else
            {
                this.currentNRG += amountToAdjust;
            }
        }
        else
        {
            if (amountToAdjust > (this.maxNRG - this.currentNRG))
            {
                this.currentNRG = this.maxNRG;
            }
            else
            {
                this.currentNRG += amountToAdjust;
            }
        }
    }

    final public void adjustNRG()
    {
        if(this.currentNRG == 0 || this.currentNRG < this.costNRG)
        {
            //throw custom exception about lacking enough NRG
        }
        else {
            this.currentNRG -= this.costNRG;
        }
    }

}
