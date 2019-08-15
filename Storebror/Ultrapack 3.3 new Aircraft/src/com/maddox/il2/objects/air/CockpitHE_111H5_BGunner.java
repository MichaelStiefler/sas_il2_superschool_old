package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitHE_111H5_BGunner extends CockpitGunner
{

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            ((HE_111)((Interpolate) (this.fm)).actor).bPitUnfocused = false;
            aircraft().hierMesh().chunkVisible("Korzina_D0", false);
            aircraft().hierMesh().chunkVisible("Turret4B_D0", false);
            aircraft().hierMesh().chunkVisible("Turret5B_D0", false);
            aircraft().hierMesh().chunkVisible("Turret6C_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot4_FAK", false);
            aircraft().hierMesh().chunkVisible("Pilot4_FAL", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        if(isFocused())
        {
            ((HE_111)((Interpolate) (this.fm)).actor).bPitUnfocused = true;
            aircraft().hierMesh().chunkVisible("Korzina_D0", true);
            aircraft().hierMesh().chunkVisible("Turret4B_D0", true);
            aircraft().hierMesh().chunkVisible("Turret5B_D0", true);
            aircraft().hierMesh().chunkVisible("Turret6C_D0", true);
            aircraft().hierMesh().chunkVisible("Pilot4_FAK", aircraft().hierMesh().isChunkVisible("Pilot4_D0"));
            aircraft().hierMesh().chunkVisible("Pilot4_FAL", aircraft().hierMesh().isChunkVisible("Pilot4_D1"));
            super.doFocusLeave();
        }
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurretBA", 0.0F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("TurretBB", 0.0F, orient.getTangage(), 0.0F);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("TurretFA", 0.0F, this.fm.turret[5].tu[0], 0.0F);
        this.mesh.chunkSetAngles("TurretFB", 0.0F, this.fm.turret[5].tu[1], 0.0F);
        this.mesh.chunkSetAngles("TurretLA", 0.0F, this.fm.turret[3].tu[0], 0.0F);
        this.mesh.chunkSetAngles("TurretLB", 0.0F, this.fm.turret[3].tu[1], 0.0F);
        this.mesh.chunkSetAngles("TurretRA", 0.0F, this.fm.turret[4].tu[0], 0.0F);
        this.mesh.chunkSetAngles("TurretRB", 0.0F, this.fm.turret[4].tu[1], 0.0F);
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
        if(f < -35F)
            f = -35F;
        if(f > 40F)
            f = 40F;
        if(f1 > 46F)
            f1 = 46F;
        if(f1 < -30F)
            f1 = -30F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick()
    {
        if(!isRealMode())
            return;
        if(this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
            this.bGunFire = false;
        this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
        if(this.bGunFire)
        {
            if(hook1 == null)
                hook1 = new HookNamed(aircraft(), "_MGUN03");
            doHitMasterAircraft(aircraft(), hook1, "_MGUN03");
            if(iCocking > 0)
                iCocking = 0;
            else
                iCocking = 1;
            iNewVisDrums = (int)((float)this.emitter.countBullets() / 250F);
            if(iNewVisDrums < iOldVisDrums)
            {
                iOldVisDrums = iNewVisDrums;
                this.mesh.chunkVisible("DrumB1", iNewVisDrums > 1);
                this.mesh.chunkVisible("DrumB2", iNewVisDrums > 0);
                sfxClick(13);
            }
        } else
        {
            iCocking = 0;
        }
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.07F * (float)iCocking;
        this.mesh.chunkSetLocate("LeverB", Cockpit.xyz, Cockpit.ypr);
    }

    public void doGunFire(boolean flag)
    {
        if(!isRealMode())
            return;
        if(this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
            this.bGunFire = false;
        else
            this.bGunFire = flag;
        this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
    }

    public CockpitHE_111H5_BGunner()
    {
        super("3DO/Cockpit/He-111P-4-BGun/hier-H5.him", "he111_gunner");
        hook1 = null;
        iCocking = 0;
        iOldVisDrums = 2;
        iNewVisDrums = 2;
        HookNamed hooknamed = new HookNamed(this.mesh, "LIGHT1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        light1.light.setColor(203F, 198F, 161F);
        light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LIGHT1", light1);
        hooknamed = new HookNamed(this.mesh, "LIGHT2");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        light2.light.setColor(203F, 198F, 161F);
        light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LIGHT2", light2);
        bNeedSetUp = true;
    }

    public void toggleLight()
    {
        this.cockpitLightControl = !this.cockpitLightControl;
        if(this.cockpitLightControl)
        {
            light1.light.setEmit(0.004F, 6.05F);
            light2.light.setEmit(1.1F, 0.2F);
            this.mesh.chunkVisible("Flare", true);
            setNightMats(true);
        } else
        {
            light1.light.setEmit(0.0F, 0.0F);
            light2.light.setEmit(0.0F, 0.0F);
            this.mesh.chunkVisible("Flare", false);
            setNightMats(false);
        }
    }

    public void reflectCockpitState()
    {
        if(this.fm.AS.astateCockpitState != 0)
            this.mesh.chunkVisible("Holes_D1", true);
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    private LightPointActor light1;
    private LightPointActor light2;
    private Hook hook1;
    private int iCocking;
    private int iOldVisDrums;
    private int iNewVisDrums;
    private boolean bNeedSetUp;

    static 
    {
        Property.set(CockpitHE_111H5_BGunner.class, "aiTuretNum", 2);
        Property.set(CockpitHE_111H5_BGunner.class, "weaponControlNum", 12);
        Property.set(CockpitHE_111H5_BGunner.class, "astatePilotIndx", 3);
    }
}
