package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitLiberator_GR_V_BGunner extends CockpitGunner
{

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        aircraft().hierMesh().chunkVisible("Turret1B_D0", aircraft().hierMesh().isChunkVisible("Turret1A_D0"));
        super.doFocusLeave();
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Body", 180F, 0.0F, 180F);
        this.mesh.chunkSetAngles("BaseB24", 180F, 0.0F, 180F);
        this.mesh.chunkSetAngles("Turret1A", orient.getYaw(), 180F, 180F);
        this.mesh.chunkSetAngles("Turret1B", 180F, -orient.getTangage(), 180F);
    }

    public void clipAnglesGun(Orient orient)
    {
        float f = orient.getYaw();
        float f_1_ = orient.getTangage();
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
            float f_3_ = f - prevA0;
            float f_4_ = 0.001F * (float)(Time.current() - prevTime);
            float f_5_ = Math.abs(f_3_ / f_4_);
            if(f_5_ > 120F)
                if(f > prevA0)
                    f = prevA0 + 120F * f_4_;
                else
                if(f < prevA0)
                    f = prevA0 - 120F * f_4_;
            prevTime = Time.current();
            if(f_1_ > 0.0F)
                f_1_ = 0.0F;
            if(f_1_ < -85F)
                f_1_ = -85F;
            orient.setYPR(f, f_1_, 0.0F);
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
                    hook1 = new HookNamed(aircraft(), "_MGUN05");
                doHitMasterAircraft(aircraft(), hook1, "_MGUN05");
                if(hook2 == null)
                    hook2 = new HookNamed(aircraft(), "_MGUN06");
                doHitMasterAircraft(aircraft(), hook2, "_MGUN06");
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

    public void reflectCockpitState()
    {
        if((this.fm.AS.astateCockpitState & 4) != 0)
            this.mesh.chunkVisible("Z_Holes1_D1", true);
        if((this.fm.AS.astateCockpitState & 0x10) != 0)
            this.mesh.chunkVisible("Z_Holes2_D1", true);
    }

    public CockpitLiberator_GR_V_BGunner()
    {
        super("3DO/Cockpit/A-20G-TGun/BGunnerB24D.him", "he111_gunner");
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
        Property.set(CockpitLiberator_GR_V_BGunner.class, "aiTuretNum", 2);
        Property.set(CockpitLiberator_GR_V_BGunner.class, "weaponControlNum", 12);
        Property.set(CockpitLiberator_GR_V_BGunner.class, "astatePilotIndx", 1);
    }
}
