package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitB_24D_TGunner extends CockpitGunner
{

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Turret2B_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        aircraft().hierMesh().chunkVisible("Turret2B_D0", aircraft().hierMesh().isChunkVisible("Turret2A_D0"));
        super.doFocusLeave();
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurretA", 0.0F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("TurretB", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("TurretC", 0.0F, cvt(orient.getTangage(), -10F, 58F, -10F, 58F), 0.0F);
    }

    public void clipAnglesGun(Orient orient)
    {
        float f = orient.getYaw();
        float f_0_ = orient.getTangage();
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
                reflectPlaneMats();
            }
            if(f < -120F && prevA0 > 120F)
                f += 360F;
            else
            if(f > 120F && prevA0 < -120F)
                prevA0 += 360F;
            float f_2_ = f - prevA0;
            float f_3_ = 0.001F * (float)(Time.current() - prevTime);
            float f_4_ = Math.abs(f_2_ / f_3_);
            if(f_4_ > 120F)
                if(f > prevA0)
                    f = prevA0 + 120F * f_3_;
                else
                if(f < prevA0)
                    f = prevA0 - 120F * f_3_;
            prevTime = Time.current();
            if(f_0_ > 73F)
                f_0_ = 73F;
            if(f_0_ < 0.0F)
                f_0_ = 0.0F;
            orient.setYPR(f, f_0_, 0.0F);
            orient.wrap();
            prevA0 = f;
        }
    }

    protected void interpTick()
    {
        if(isRealMode())
        {
            if(this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
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
    }

    public void doGunFire(boolean bool)
    {
        if(isRealMode())
        {
            if(this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
                this.bGunFire = false;
            else
                this.bGunFire = bool;
            this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
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
            this.mesh.chunkVisible("XGlassDamage1", true);
        if((this.fm.AS.astateCockpitState & 0x10) != 0)
            this.mesh.chunkVisible("XGlassDamage2", true);
    }

    public CockpitB_24D_TGunner()
    {
        super("3DO/Cockpit/B-25J-TGun/TGunnerB24D.him", "bf109");
        bNeedSetUp = true;
        prevTime = -1L;
        prevA0 = 0.0F;
        hook1 = null;
        hook2 = null;
    }

    private boolean bNeedSetUp;
    private long prevTime;
    private float prevA0;
    private Hook hook1;
    private Hook hook2;

    static 
    {
        Property.set(CockpitB_24D_TGunner.class, "aiTuretNum", 1);
        Property.set(CockpitB_24D_TGunner.class, "weaponControlNum", 11);
        Property.set(CockpitB_24D_TGunner.class, "astatePilotIndx", 3);
    }
}
