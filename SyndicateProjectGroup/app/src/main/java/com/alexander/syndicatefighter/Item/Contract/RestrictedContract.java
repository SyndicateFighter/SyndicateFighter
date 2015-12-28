package com.alexander.syndicatefighter.Item.Contract;

import com.alexander.syndicatefighter.Worker.Worker;

/**
 * Created by alexandernohe on 11/25/15.
 */
public class RestrictedContract extends Contract{


    @Override
    public boolean Action(Worker workerToCapture) {
        if (workerToCapture.getPercentOfHealthRemaining() < .2) {
            return true;
        } else {
            return false;
        }
    }
}
