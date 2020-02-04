package com.maddox.il2.ai;

import java.util.Random;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.game.Mission;

public class TriNewAircraftAirLevel extends Trigger
{

    public TriNewAircraftAirLevel(String zname, int i, int j, int posx, int posy, int r, String s, 
            int zmin, int zmax, int ziaHumans, boolean bSortie, int zAvionMin, int zProba, int zAltiDiff, 
            String zsLink, String sTextDisplay, int zTextDuree)
    {
        super(zname, i, j, posx, posy, r, zmin, zmax, ziaHumans, bSortie, zAvionMin, zProba, zsLink, sTextDisplay, zTextDuree);
        nameTarget = s;
        altiDiff = zAltiDiff;
        if(nameTarget == "" || nameTarget == null)
            destroy();
        World.cur().triggersGuard.listTriggerAvionAppar.add(nameTarget);
        World.cur().triggersGuard.listTriggerAvionAirLevel.add(nameTarget);
    }

    protected boolean checkPeriodic()
    {
        if(Actor.getByName(nameTarget) == null)
        {
            checkMove();
            if(super.sLink != "" && super.sLink != null && Actor.getByName(super.sLink) == null)
                return false;
            com.maddox.il2.engine.CollideEnv.ResultTrigger result = Engine.collideEnv().getEnemiesInCyl(super.posTigger, super.rayon, super.altiMin, super.altiMax, super.army, super.iaHumans, super.avionMin);
            if(super.bTSortie)
            {
                if(super.bIsEnter)
                {
                    super.altiMsg = (int)result.altiSea;
                    return !result.result;
                }
                if(result.result)
                {
                    super.bIsEnter = true;
                    alti = result.altiSea + (double)altiDiff;
                }
            } else
            if(result.result)
            {
                alti = result.altiSea + (double)altiDiff;
                super.altiMsg = (int)result.altiSea;
                return true;
            }
            return false;
        } else
        {
            destroy();
            return false;
        }
    }

    protected void execute()
    {
        Random r = new Random();
        float f = r.nextFloat() * 100F + 1.0F;
        if(f <= (float)super.proba)
        {
            super.declanche = true;
            if(nameTarget != null && Actor.getByName(nameTarget) == null)
                Mission.cur().loadWingOnTrigger(nameTarget);
            if(super.sLink == "" || super.sLink == null)
            {
                EventLog.onTriggerActivate(Actor.getByName(nameTarget), this);
                doSendMsg(false);
            } else
            {
                EventLog.onTriggerActivateLink(Actor.getByName(nameTarget), this);
                doSendMsg(true);
            }
        }
        super.execute();
    }

    public double getAlti()
    {
        return alti;
    }

    public String getTarget()
    {
        return nameTarget;
    }

    public void destroy()
    {
        super.destroy();
        World.cur().triggersGuard.listTriggerAvionAirLevel.remove(World.cur().triggersGuard.listTriggerAvionAirLevel.indexOf(nameTarget));
        World.cur().triggersGuard.listTriggerAvionAppar.remove(World.cur().triggersGuard.listTriggerAvionAppar.indexOf(nameTarget));
    }

    String nameTarget;
    double alti;
    int altiDiff;
}
