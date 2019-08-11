package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitB24D_LGunner extends CockpitB24D_WGunner
{

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        float f = -40F;
        if(orient.getYaw() < 0.0F)
            f = cvt(orient.getYaw(), -66F, 0.0F, -30F, -39F);
        else
            f = cvt(orient.getYaw(), 0.0F, 38F, -39F, -35F);
        this.mesh.chunkSetAngles("GunSwivelL_D0", 0.0F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("GunSwivelL_Cam", 0.0F, -orient.getYaw() - cvt(orient.getTangage(), -45F, f, 20F, 0.0F), 0.0F);
        this.mesh.chunkSetAngles("WeaponLeft_D0", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("WeaponLeft_Cam", 0.0F, cvt(orient.getTangage(), f, 35F, f, 35F), 0.0F);
        this.lastLTan = orient.getTangage();
        this.lastLYaw = orient.getYaw();
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
        if(f < -66F)
            f = -66F;
        if(f > 38F)
            f = 38F;
        if(f1 > 40F)
            f1 = 40F;
        float f2 = cvt(f, -66F, -52F, -35F, -45F);
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
                hook1 = new HookNamed(aircraft(), "_MGUN10");
            doHitMasterAircraft(aircraft(), hook1, "_MGUN10");
        }
    }

    public void doGunFire(boolean flag)
    {
        if(!isRealMode())
            return;
        if(((B_24D140CO)aircraft()).fLGunPos < 0.9F)
            return;
        if(this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
            this.bGunFire = false;
        else
            this.bGunFire = flag;
        this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
    }

    public CockpitB24D_LGunner()
    {
        super("3DO/Cockpit/B-24D-Waist/L_hier.him");
        hook1 = null;
    }

    public void reflectWorldToInstruments(float f)
    {
        super.reflectWorldToInstruments(f);
        float f1 = ((B_24D140CO)aircraft()).fRGunPos;
        if((double)f1 > 0.99D)
        {
            this.mesh.chunkSetAngles("GunSwivelR_D0", 0.0F, aircraft().FM.turret[4].tu[0], 0.0F);
            this.mesh.chunkSetAngles("WeaponRight_D0", 0.0F, aircraft().FM.turret[4].tu[1], 0.0F);
        }
    }

    private Hook hook1;

    static 
    {
        Property.set(CockpitB24D_LGunner.class, "aiTuretNum", 3);
        Property.set(CockpitB24D_LGunner.class, "weaponControlNum", 13);
        Property.set(CockpitB24D_LGunner.class, "astatePilotIndx", 4);
        Property.set(CockpitB24D_LGunner.class, "normZN", 1.8F);
    }
}
