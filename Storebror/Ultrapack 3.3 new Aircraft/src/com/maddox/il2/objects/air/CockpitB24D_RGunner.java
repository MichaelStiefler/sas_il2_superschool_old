package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitB24D_RGunner extends CockpitB24D_WGunner
{

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        float f = -40F;
        if(orient.getYaw() > 0.0F)
            f = cvt(orient.getYaw(), 0.0F, 66F, -39F, -30F);
        else
            f = cvt(orient.getYaw(), -39F, 0.0F, -35F, -39F);
        this.mesh.chunkSetAngles("GunSwivelR_D0", 0.0F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("GunSwivelR_Cam", 0.0F, -orient.getYaw() + cvt(orient.getTangage(), -45F, f, 20F, 0.0F), 0.0F);
        this.mesh.chunkSetAngles("WeaponRight_D0", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("WeaponRight_Cam", 0.0F, cvt(orient.getTangage(), f, 35F, f, 35F), 0.0F);
        this.lastRTan = orient.getTangage();
        this.lastRYaw = orient.getYaw();
    }

    public void clipAnglesGun(Orient orient)
    {
        if(!isRealMode())
            return;
        if(!aiTurret().bIsOperable)
        {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if(f < -39F)
            f = -39F;
        if(f > 66F)
            f = 66F;
        if(f1 > 40F)
            f1 = 40F;
        float f2 = cvt(f, 50F, 66F, -45F, -35F);
        if(f1 < f2)
            f1 = f2;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick()
    {
        if(this.bNeedSetUp)
        {
            this.bNeedSetUp = false;
            reflectPlaneMats();
        }
        if(!isRealMode())
            return;
        if(this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
            this.bGunFire = false;
        this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
        if(this.bGunFire)
        {
            if(hook1 == null)
                hook1 = new HookNamed(aircraft(), "_MGUN09");
            doHitMasterAircraft(aircraft(), hook1, "_MGUN09");
        }
    }

    public void doGunFire(boolean flag)
    {
        if(!isRealMode())
            return;
        if(((B_24D140CO)aircraft()).fRGunPos < 0.9F)
            return;
        if(this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
            this.bGunFire = false;
        else
            this.bGunFire = flag;
        this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
    }

    public CockpitB24D_RGunner()
    {
        super("3DO/Cockpit/B-24D-Waist/R_hier.him");
        hook1 = null;
    }

    public void reflectWorldToInstruments(float f)
    {
        super.reflectWorldToInstruments(f);
        float f1 = ((B_24D140CO)aircraft()).fLGunPos;
        if(f1 > 0.99F)
        {
            this.mesh.chunkSetAngles("GunSwivelL_D0", 0.0F, aircraft().FM.turret[3].tu[0], 0.0F);
            this.mesh.chunkSetAngles("WeaponLeft_D0", 0.0F, aircraft().FM.turret[3].tu[1], 0.0F);
        }
    }

    private Hook hook1;

    static 
    {
        Property.set(CockpitB24D_RGunner.class, "aiTuretNum", 4);
        Property.set(CockpitB24D_RGunner.class, "weaponControlNum", 14);
        Property.set(CockpitB24D_RGunner.class, "astatePilotIndx", 5);
        Property.set(CockpitB24D_RGunner.class, "normZN", 1.8F);
    }
}
