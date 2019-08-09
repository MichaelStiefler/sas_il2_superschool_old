package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitHalifaxBMkIII_TGunner extends CockpitGunner
{

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Turret2B_D0", false);
            aircraft().hierMesh().chunkVisible("Turret2C_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        aircraft().hierMesh().chunkVisible("Turret2B_D0", true);
        aircraft().hierMesh().chunkVisible("Turret2C_D0", true);
        super.doFocusLeave();
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        mesh.chunkSetAngles("TurretA", 0.0F, -orient.getYaw(), 0.0F);
        mesh.chunkSetAngles("TurretB", 0.0F, orient.getTangage(), 0.0F);
        mesh.chunkSetAngles("TurretC", 0.0F, cvt(orient.getTangage(), -10F, 58F, -10F, 58F), 0.0F);
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
            return;
        }
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
        if(f1 > 73F)
            f1 = 73F;
        if(f1 < cvt(f2, 140F, 180F, -1F, 25F))
            f1 = cvt(f2, 140F, 180F, -1F, 25F);
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
        prevA0 = f;
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
                    hook1 = new HookNamed(aircraft(), "_MGUN01");
                doHitMasterAircraft(aircraft(), hook1, "_MGUN01");
                if(hook2 == null)
                    hook2 = new HookNamed(aircraft(), "_MGUN02");
                doHitMasterAircraft(aircraft(), hook2, "_MGUN02");
                if(hook3 == null)
                    hook3 = new HookNamed(aircraft(), "_MGUN03");
                doHitMasterAircraft(aircraft(), hook3, "_MGUN03");
                if(hook4 == null)
                    hook4 = new HookNamed(aircraft(), "_MGUN04");
                doHitMasterAircraft(aircraft(), hook4, "_MGUN04");
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
        mesh.materialReplace("Gloss1D0o", mat);
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 4) != 0)
            mesh.chunkVisible("XGlassDamage1", true);
        if((fm.AS.astateCockpitState & 0x10) != 0)
            mesh.chunkVisible("XGlassDamage2", true);
    }

    public CockpitHalifaxBMkIII_TGunner()
    {
        super("3DO/Cockpit/Halifax-TGun/hier.him", "bf109");
        bNeedSetUp = true;
        prevTime = -1L;
        prevA0 = 0.0F;
        hook1 = null;
        hook2 = null;
        hook3 = null;
        hook4 = null;
        mesh.chunkSetAngles("PRICELL", 0.0F, -90F, 0.0F);
    }

    private boolean bNeedSetUp;
    private long prevTime;
    private float prevA0;
    private Hook hook1;
    private Hook hook2;
    private Hook hook3;
    private Hook hook4;

    static 
    {
        Property.set(CockpitHalifaxBMkIII_TGunner.class, "aiTuretNum", 1);
        Property.set(CockpitHalifaxBMkIII_TGunner.class, "weaponControlNum", 11);
        Property.set(CockpitHalifaxBMkIII_TGunner.class, "astatePilotIndx", 3);
    }
}
