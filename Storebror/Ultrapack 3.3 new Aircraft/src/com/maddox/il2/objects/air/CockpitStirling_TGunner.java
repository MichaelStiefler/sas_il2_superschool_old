package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitStirling_TGunner extends CockpitGunner
{

    protected void interpTick()
    {
        if(!isRealMode())
            return;
        if(this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable || suppressGunFire)
            this.bGunFire = false;
        this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
        if(this.bGunFire)
        {
            if(hook1 == null)
                hook1 = new HookNamed(aircraft(), "_MGUN03");
            doHitMasterAircraft(aircraft(), hook1, "_MGUN03");
            if(hook2 == null)
                hook2 = new HookNamed(aircraft(), "_MGUN04");
            doHitMasterAircraft(aircraft(), hook2, "_MGUN04");
        }
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Turret1A", -orient.getYaw() - orient.getTangage() * 0.03F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Turret2B", 0.0F, orient.getTangage() + 1.0F, 0.0F);
        this.mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage() + 1.0F, 0.0F);
    }

    public void clipAnglesGun(Orient orient)
    {
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        for(; f < -180F; f += 360F);
        for(; f > 180F; f -= 360F);
        if(isRealMode())
        {
            for(; prevA0 < -180F; prevA0 += 360F);
            for(; prevA0 > 180F; prevA0 -= 360F);
        } else
        {
            prevA0 = f;
            return;
        }
        if(bNeedSetUp)
        {
            prevTime = Time.current() - 1L;
            bNeedSetUp = false;
            reflectPlaneMats();
        }
        if(f < -120F && prevA0 > 120F)
            f += 360F;
        else
        if(f > 120F && prevA0 < -120F)
            prevA0 += 360F;
        float f2 = f - prevA0;
        float f3 = 0.001F * (float)(Time.current() - prevTime);
        float f4 = Math.abs(f2 / f3);
        if(f4 > 120F)
            if(f > prevA0)
                f = prevA0 + 120F * f3;
            else
            if(f < prevA0)
                f = prevA0 - 120F * f3;
        prevTime = Time.current();
        suppressGunFire = false;
        if(f >= 3F && f < 11F)
        {
            if(f1 < 27F)
                suppressGunFire = true;
        } else
        if(f >= 11F && f < 38F)
        {
            if(f1 < -6F)
                suppressGunFire = true;
        } else
        if(f >= 102F && f < 173F)
        {
            if(f1 < -1F)
                suppressGunFire = true;
        } else
        if(f >= 173F || f < -168F)
        {
            if(f1 < 6F)
                suppressGunFire = true;
        } else
        if(f >= -168F && f < -91F)
        {
            if(f1 < 1.5F)
                suppressGunFire = true;
        } else
        if(f >= -25F && f < 3F && f1 < -6F)
            suppressGunFire = true;
        if(f1 > 75F)
            f1 = 75F;
        if(f1 < -20F)
            f1 = -20F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
        prevA0 = f;
    }

    public void doGunFire(boolean flag)
    {
        if(!isRealMode())
            return;
        if(this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable || suppressGunFire)
            this.bGunFire = false;
        else
            this.bGunFire = flag;
        this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Turret2A_D0", false);
            aircraft().hierMesh().chunkVisible("Turret2B_D0", false);
            aircraft().hierMesh().chunkVisible("Wire", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        if(!isFocused())
        {
            return;
        } else
        {
            aircraft().hierMesh().chunkVisible("Turret2A_D0", true);
            aircraft().hierMesh().chunkVisible("Turret2B_D0", true);
            aircraft().hierMesh().chunkVisible("Wire", true);
            super.doFocusLeave();
            return;
        }
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            prevTime = Time.current() - 1L;
            bNeedSetUp = false;
            reflectPlaneMats();
        }
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void reflectCockpitState()
    {
        if((this.fm.AS.astateCockpitState & 4) != 0)
            this.mesh.chunkVisible("Z_Holes1_D1", true);
        if((this.fm.AS.astateCockpitState & 0x10) != 0)
            this.mesh.chunkVisible("Z_Holes2_D1", true);
    }

    public CockpitStirling_TGunner()
    {
        super("3DO/Cockpit/Stirling-TGun/hier.him", "bf109");
        bNeedSetUp = true;
        prevTime = -1L;
        prevA0 = 0.0F;
        hook1 = null;
        hook2 = null;
        suppressGunFire = false;
    }

    private boolean bNeedSetUp;
    private long prevTime;
    private float prevA0;
    private Hook hook1;
    private Hook hook2;
    private boolean suppressGunFire;

    static 
    {
        Property.set(CockpitStirling_TGunner.class, "aiTuretNum", 1);
        Property.set(CockpitStirling_TGunner.class, "weaponControlNum", 11);
        Property.set(CockpitStirling_TGunner.class, "astatePilotIndx", 2);
    }
}
