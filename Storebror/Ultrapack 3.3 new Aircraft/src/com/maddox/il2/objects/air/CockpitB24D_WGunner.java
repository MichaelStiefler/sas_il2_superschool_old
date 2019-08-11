package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;

public abstract class CockpitB24D_WGunner extends CockpitGunner
{

    public CockpitB24D_WGunner(String s)
    {
        super(s, "he111_gunner");
        bNeedSetUp = true;
        lastRTan = 0.0F;
        lastRYaw = 0.0F;
        lastLTan = 0.0F;
        lastLYaw = 0.0F;
        this.cockpitNightMats = (new String[] {
            "alpha2", "oxygen2"
        });
        setNightMats(false);
        HookNamed hooknamed = new HookNamed(this.mesh, "_IntLight01");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        light1.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOKW1", light1);
        hooknamed = new HookNamed(this.mesh, "_IntlLight02");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        light2.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOKW2", light2);
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("skin", mat);
    }

    public void reflectWorldToInstruments(float f)
    {
        resetYPRmodifier();
        float f1 = ((B_24D140CO)aircraft()).fBallPos;
        if(((B_24D140CO)aircraft()).bBallRemoved)
            f1 = 1.0F;
        if(f1 < 0.02F)
        {
            Cockpit.xyz[2] = 0.5F * f1;
            this.mesh.chunkSetLocate("Turret3C_D0", Cockpit.xyz, Cockpit.ypr);
        } else
        {
            Cockpit.xyz[2] = -0.72F * f1 + 0.01F;
            this.mesh.chunkSetLocate("Turret3C_D0", Cockpit.xyz, Cockpit.ypr);
        }
        this.mesh.chunkSetAngles("zBallHook01", 0.0F, 0.0F, cvt(f1, 0.0F, 0.02F, 0.0F, -12F));
        this.mesh.chunkSetAngles("zBallHook02", 0.0F, 0.0F, cvt(f1, 0.0F, 0.02F, 0.0F, -12F));
        this.mesh.chunkSetAngles("zPulley01", 0.0F, 0.0F, f1 * 1170F);
        this.mesh.chunkSetAngles("zPulley02", 0.0F, 0.0F, f1 * 1170F);
        this.mesh.chunkSetAngles("zDrumNut", f1 * 10000F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSpindleForward", f1 * 2070F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSpindleRear", f1 * 2070F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFrameDrumRear", f1 * 110F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFrameDrumFwd", f1 * 110F, 0.0F, 0.0F);
        this.mesh.chunkVisible("zSafetyWireAdd", (double)f1 > 0.5D);
        this.mesh.chunkSetAngles("zSperryTrunion", aircraft().FM.turret[2].tu[0], 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSperryBall", 0.0F, aircraft().FM.turret[2].tu[1], 0.0F);
        float f2 = ((B_24D140CO)aircraft()).fLGunPos;
        float f3 = ((B_24D140CO)aircraft()).fRGunPos;
        f2 = 1.0F - f2;
        f3 = 1.0F - f3;
        resetYPRmodifier();
        if(f3 < 0.04F)
            Cockpit.xyz[2] = cvt(f3, 0.0F, 0.03F, 0.0F, -0.02F);
        else
        if(f3 < 0.09F)
            Cockpit.xyz[2] = cvt(f3, 0.05F, 0.08F, -0.02F, 0.045F);
        else
        if(f3 < 0.85F)
            Cockpit.xyz[2] = cvt(f3, 0.81F, 0.84F, 0.045F, -0.02F);
        else
            Cockpit.xyz[2] = cvt(f3, 0.86F, 0.89F, -0.02F, 0.0F);
        if(f3 < 0.06F)
            Cockpit.ypr[0] = cvt(f3, 0.03F, 0.05F, 0.0F, -60F);
        else
            Cockpit.ypr[0] = cvt(f3, 0.84F, 0.86F, -60F, 0.0F);
        this.mesh.chunkSetLocate("GunLockRod_R", Cockpit.xyz, Cockpit.ypr);
        if(f3 > 0.01F)
        {
            if(f3 < 0.1F)
                this.mesh.chunkSetAngles("GunSwivelR_D0", 0.0F, cvt(f3, 0.05F, 0.2F, -lastRYaw, 38F), 0.0F);
            else
                this.mesh.chunkSetAngles("GunSwivelR_D0", 0.0F, cvt(f3, 0.2F, 0.57F, 38F, -100F), 0.0F);
            if(f3 < 0.15F)
                this.mesh.chunkSetAngles("WeaponRight_D0", 0.0F, cvt(f3, 0.08F, 0.15F, lastRTan, 0.0F), 0.0F);
            else
                this.mesh.chunkSetAngles("WeaponRight_D0", 0.0F, cvt(f3, 0.2F, 0.55F, 0.0F, -50F), 0.0F);
        }
        if(f3 < 0.5F)
            this.mesh.chunkSetAngles("GunTubesR", cvt(f3, 0.15F, 0.43F, 0.0F, -90F), 0.0F, 0.0F);
        else
            this.mesh.chunkSetAngles("GunTubesR", cvt(f3, 0.5F, 0.79F, -90F, 0.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("HatchDoorR", 0.0F, cvt(f3, 0.4F, 0.54F, 0.0F, 135F), 0.0F);
        this.mesh.chunkSetAngles("WinDRBar", cvt(f3, 0.9F, 1.0F, 0.0F, -28F), 0.0F, 0.0F);
        this.mesh.chunkVisible("WindDR", (double)f3 < 0.95D);
        resetYPRmodifier();
        if(f2 < 0.04F)
            Cockpit.xyz[2] = cvt(f2, 0.0F, 0.03F, 0.0F, 0.02F);
        else
        if(f2 < 0.09F)
            Cockpit.xyz[2] = cvt(f2, 0.05F, 0.08F, 0.02F, -0.045F);
        else
        if(f2 < 0.85F)
            Cockpit.xyz[2] = cvt(f2, 0.81F, 0.84F, -0.045F, 0.02F);
        else
            Cockpit.xyz[2] = cvt(f2, 0.86F, 0.89F, 0.02F, 0.0F);
        if(f2 < 0.06F)
            Cockpit.ypr[0] = cvt(f2, 0.03F, 0.05F, 0.0F, -60F);
        else
            Cockpit.ypr[0] = cvt(f2, 0.84F, 0.86F, -60F, 0.0F);
        this.mesh.chunkSetLocate("GunLockRod_L", Cockpit.xyz, Cockpit.ypr);
        if(f2 > 0.01F)
        {
            if(f2 < 0.1F)
                this.mesh.chunkSetAngles("GunSwivelL_D0", 0.0F, cvt(f2, 0.05F, 0.2F, lastLYaw, -38F), 0.0F);
            else
                this.mesh.chunkSetAngles("GunSwivelL_D0", 0.0F, cvt(f2, 0.2F, 0.57F, -38F, 100F), 0.0F);
            if(f2 < 0.15F)
                this.mesh.chunkSetAngles("WeaponLeft_D0", 0.0F, cvt(f2, 0.08F, 0.15F, lastLTan, 0.0F), 0.0F);
            else
                this.mesh.chunkSetAngles("WeaponLeft_D0", 0.0F, cvt(f2, 0.2F, 0.55F, 0.0F, -50F), 0.0F);
        }
        if(f2 < 0.5F)
            this.mesh.chunkSetAngles("GunTubesL", cvt(f2, 0.15F, 0.43F, 0.0F, -90F), 0.0F, 0.0F);
        else
            this.mesh.chunkSetAngles("GunTubesL", cvt(f2, 0.5F, 0.79F, -90F, 0.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("HatchDoorL", 0.0F, cvt(f2, 0.4F, 0.54F, 0.0F, 135F), 0.0F);
        this.mesh.chunkSetAngles("WinDLBar", cvt(f2, 0.9F, 1.0F, 0.0F, -28F), 0.0F, 0.0F);
        this.mesh.chunkVisible("WindDL", (double)f2 < 0.95D);
        if(((B_24D140CO)aircraft()).bBallRemoved)
        {
            this.mesh.chunkVisible("zSperryBall", false);
            this.mesh.chunkVisible("zSperryBallDoor", false);
        }
    }

    public void toggleLight()
    {
        this.cockpitLightControl = !this.cockpitLightControl;
        if(this.cockpitLightControl)
        {
            setNightMats(true);
            light1.light.setEmit(0.8F, 4F);
            light2.light.setEmit(0.8F, 4F);
        } else
        {
            setNightMats(false);
            light1.light.setEmit(0.0F, 0.0F);
            light2.light.setEmit(0.0F, 0.0F);
        }
    }

    public void reflectCockpitState()
    {
        if((this.fm.AS.astateCockpitState & 4) != 0);
        if((this.fm.AS.astateCockpitState & 8) != 0);
        if((this.fm.AS.astateCockpitState & 0x10) != 0);
        if((this.fm.AS.astateCockpitState & 0x20) != 0);
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Tail1Int_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        aircraft().hierMesh().chunkVisible("Tail1Int_D0", true);
        super.doFocusLeave();
    }

    protected boolean bNeedSetUp;
    private LightPointActor light1;
    private LightPointActor light2;
    protected float lastRTan;
    protected float lastRYaw;
    protected float lastLTan;
    protected float lastLYaw;
}
