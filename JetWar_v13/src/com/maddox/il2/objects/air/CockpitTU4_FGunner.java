package com.maddox.il2.objects.air;

import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitTU4_FGunner extends CockpitGunner
{

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        super.mesh.chunkSetAngles("Body", 180F, 0.0F, 180F);
        super.mesh.chunkSetAngles("Turret1A", orient.getYaw(), 180F, 180F);
        super.mesh.chunkSetAngles("Turret1B", 180F, -orient.getTangage(), 180F);
    }

    public void clipAnglesGun(Orient orient)
    {
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        for(; f < -180F; f += 360F);
        for(; f > 180F; f -= 360F);
        for(; prevA0 < -180F; prevA0 += 360F);
        for(; prevA0 > 180F; prevA0 -= 360F);
        if(!isRealMode())
        {
            prevA0 = f;
        } else
        {
            if(bNeedSetUp)
            {
                prevTime = Time.current() - 1L;
                bNeedSetUp = false;
            }
            if(f < -120F && prevA0 > 120F)
                f += 360F;
            else
            if(f > 120F && prevA0 < -120F)
                prevA0 += 360F;
            float f3 = f - prevA0;
            float f4 = 0.001F * (float)(Time.current() - prevTime);
            float f5 = Math.abs(f3 / f4);
            if(f5 > 120F)
                if(f > prevA0)
                    f = prevA0 + 120F * f4;
                else
                if(f < prevA0)
                    f = prevA0 - 120F * f4;
            prevTime = Time.current();
            if(f1 > 5F)
                f1 = 5F;
            if(f1 < -85F)
                f1 = -85F;
            orient.setYPR(f, f1, 0.0F);
            orient.wrap();
            prevA0 = f;
        }
    }

    protected void interpTick()
    {
        if(isRealMode())
        {
            if(super.emitter == null || !super.emitter.haveBullets() || !aiTurret().bIsOperable)
                super.bGunFire = false;
            ((FlightModelMain) (super.fm)).CT.WeaponControl[weaponControlNum()] = super.bGunFire;
            if(super.bGunFire)
            {
                if(hook1 == null)
                    hook1 = new HookNamed(aircraft(), "_MGUN07");
                doHitMasterAircraft(aircraft(), hook1, "_MGUN07");
                if(hook2 == null)
                    hook2 = new HookNamed(aircraft(), "_MGUN08");
                doHitMasterAircraft(aircraft(), hook2, "_MGUN08");
            }
        }
    }

    public void doGunFire(boolean flag)
    {
        if(isRealMode())
        {
            if(super.emitter == null || !super.emitter.haveBullets() || !aiTurret().bIsOperable)
                super.bGunFire = false;
            else
                super.bGunFire = flag;
            ((FlightModelMain) (super.fm)).CT.WeaponControl[weaponControlNum()] = super.bGunFire;
        }
    }

    public void reflectCockpitState()
    {
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 4) != 0)
            super.mesh.chunkVisible("Z_Holes1_D1", true);
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x10) != 0)
            super.mesh.chunkVisible("Z_Holes2_D1", true);
    }

    public CockpitTU4_FGunner()
    {
        super("3DO/Cockpit/A-20G-TGun/FGunnerTU4.him", "he111_gunner");
        bNeedSetUp = true;
        prevTime = -1L;
        prevA0 = 0.0F;
        hook1 = null;
        hook2 = null;
    }

    static Class _mthclass$(String s)
    {
        Class class1;
        try
        {
            class1 = Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
        return class1;
    }

    private boolean bNeedSetUp;
    private long prevTime;
    private float prevA0;
    private Hook hook1;
    private Hook hook2;

    static 
    {
        Property.set(com.maddox.il2.objects.air.CockpitTU4_FGunner.class, "aiTuretNum", 1);
        Property.set(com.maddox.il2.objects.air.CockpitTU4_FGunner.class, "weaponControlNum", 11);
        Property.set(com.maddox.il2.objects.air.CockpitTU4_FGunner.class, "astatePilotIndx", 4);
    }
}