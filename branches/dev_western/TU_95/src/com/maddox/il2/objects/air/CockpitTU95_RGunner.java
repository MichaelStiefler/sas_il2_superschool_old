// Source File Name:   CockpitTU95_RGunner.java

package com.maddox.il2.objects.air;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitGunner

public class CockpitTU95_RGunner extends CockpitGunner
{

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        mesh.chunkSetAngles("Body", 180F, 0.0F, 181F);
        mesh.chunkSetAngles("Turret1A", orient.getYaw(), 180F, 180F);
        mesh.chunkSetAngles("Turret1B", 180F, -orient.getTangage(), 180F);
    }

    public void clipAnglesGun(Orient orient)
    {
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        float f2 = Math.abs(f);
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
            if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
                bGunFire = false;
            fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
            if(bGunFire)
            {
                if(hook1 == null)
                    hook1 = new HookNamed(aircraft(), "_MGUN09");
                doHitMasterAircraft(aircraft(), hook1, "_MGUN09");
                if(hook2 == null)
                    hook2 = new HookNamed(aircraft(), "_MGUN10");
                doHitMasterAircraft(aircraft(), hook2, "_MGUN10");
            }
        }
    }

    public void doGunFire(boolean flag)
    {
        if(isRealMode())
        {
            if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
                bGunFire = false;
            else
                bGunFire = flag;
            fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
        }
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 4) != 0)
            mesh.chunkVisible("Z_Holes1_D1", true);
        if((fm.AS.astateCockpitState & 0x10) != 0)
            mesh.chunkVisible("Z_Holes2_D1", true);
    }

    public CockpitTU95_RGunner()
    {
        super("3DO/Cockpit/TU95-RGun/RGunnerTU95.him", "he111_gunner");
        bNeedSetUp = true;
        prevTime = -1L;
        prevA0 = 0.0F;
        hook1 = null;
        hook2 = null;
    }

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    private boolean bNeedSetUp;
    private long prevTime;
    private float prevA0;
    private Hook hook1;
    private Hook hook2;

    static 
    {
        Property.set(com.maddox.il2.objects.air.CockpitTU95_RGunner.class, "aiTuretNum", 0);
        Property.set(com.maddox.il2.objects.air.CockpitTU95_RGunner.class, "weaponControlNum", 10);
        Property.set(com.maddox.il2.objects.air.CockpitTU95_RGunner.class, "astatePilotIndx", 2);
    }
}