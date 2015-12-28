package com.alexander.syndicatefighter.Item.Contract;

import com.alexander.syndicatefighter.Item.Item;
import com.alexander.syndicatefighter.Worker.Worker;

/**
 * Created by alexandernohe on 11/25/15.
 */
abstract public class Contract extends Item{

    abstract public boolean Action(Worker workerToCapture);
}
