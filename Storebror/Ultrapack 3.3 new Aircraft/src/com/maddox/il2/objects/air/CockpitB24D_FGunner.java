package com.maddox.il2.objects.air;

import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Mission;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public abstract class CockpitB24D_FGunner extends CockpitGunner
{
    private class Variables
    {

        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float altimeter;
        float wiper;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
            radioCompassAzimuth = new AnglesFork();
        }

        Variables(Variables variables)
        {
            this();
        }
    }


    protected void interpTick()
    {
        setTmp = setOld;
        setOld = setNew;
        setNew = setTmp;
        setNew.altimeter = this.fm.getAltitude();
        float f = waypointAzimuth();
        setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), this.fm.Or.azimut());
        setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f);
        setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(0.1F) - 90F);
        switch(iWiper)
        {
        default:
            break;

        case 0:
            if(Mission.curCloudsType() > 4 && this.fm.getSpeedKMH() < 220F && this.fm.getAltitude() < Mission.curCloudsHeight() + 300F)
                iWiper = 1;
            break;

        case 1:
            setNew.wiper = setOld.wiper - 0.05F;
            if(setNew.wiper < -1.03F)
                iWiper++;
            if(wiState >= 2)
                break;
            if(wiState == 0)
            {
                if(fxw == null)
                {
                    fxw = aircraft().newSound("aircraft.wiper", false);
                    if(fxw != null)
                    {
                        fxw.setParent(aircraft().getRootFX());
                        fxw.setPosition(this.sfxPos);
                    }
                }
                if(fxw != null)
                    fxw.play(wiStart);
            }
            if(fxw != null)
            {
                fxw.play(wiRun);
                wiState = 2;
            }
            break;

        case 2:
            setNew.wiper = setOld.wiper + 0.05F;
            if(setNew.wiper > 1.03F)
                iWiper++;
            if(wiState > 1)
                wiState = 1;
            break;

        case 3:
            setNew.wiper = setOld.wiper - 0.05F;
            if(setNew.wiper >= 0.02F)
                break;
            if(this.fm.getSpeedKMH() > 250F || this.fm.getAltitude() > Mission.curCloudsHeight() + 400F)
                iWiper++;
            else
                iWiper = 1;
            break;

        case 4:
            setNew.wiper = setOld.wiper;
            iWiper = 0;
            wiState = 0;
            if(fxw != null)
                fxw.cancel();
            break;
        }
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

    public CockpitB24D_FGunner(String s)
    {
        super(s, "bf109");
        internalBombs = new BulletEmitter[24];
        fxw = null;
        wiStart = new Sample("wip_002_s.wav", 256, 65535);
        wiRun = new Sample("wip_002.wav", 256, 65535);
        wiState = 0;
        iWiper = 0;
        patron = 1.0F;
        patronMat = null;
        salvoLever = 0.0F;
        setOld = new Variables(null);
        setNew = new Variables(null);
        this.cockpitNightMats = (new String[] {
            "APanelSides", "Needles2", "oxygen2", "RadioComp", "textrbm9", "texture25"
        });
        setNightMats(false);
        for(int i = 0; i < internalBombs.length; i++)
        {
            String s1 = "_BombSpawn";
            if(i < 10)
                s1 = s1 + "0" + (i + 1);
            else
                s1 = s1 + (i + 1);
            internalBombs[i] = getBomb(s1);
        }

        int j = -1;
        j = this.mesh.materialFind("50CalRound");
        if(j != -1)
        {
            patronMat = this.mesh.material(j);
            patronMat.setLayer(0);
        }
    }

    private BulletEmitter getBomb(String s)
    {
        BulletEmitter bulletemitter = aircraft().getBulletEmitterByHookName(s);
        return bulletemitter;
    }

    public void reflectCockpitState()
    {
        if((this.fm.AS.astateCockpitState & 1) != 0)
            this.mesh.chunkVisible("zHolesG_D1", true);
    }

    protected boolean doFocusEnter()
    {
        return super.doFocusEnter();
    }

    protected void doFocusLeave()
    {
        super.doFocusLeave();
    }

    public void reflectWorldToInstruments(float f)
    {
        float f1 = ((B_24D140CO)aircraft()).fSightCurForwardAngle;
        float f2 = ((B_24D140CO)aircraft()).fSightCurAltitude;
        float f3 = ((B_24D140CO)aircraft()).fSightCurSpeed;
        boolean flag = ((B_24D140CO)aircraft()).bSightClutch;
        float f4 = ((B_24D140CO)aircraft()).fSightCurSideslip;
        float f5 = ((B_24D140CO)aircraft()).fSightHeadTurn * 30F;
        float f6 = ((B_24D140CO)aircraft()).getBombSightPDI();
        this.mesh.chunkSetAngles("zFootBall", f5, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zNPDI", f6 * -10F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBsClutch", f5, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBSArm01", f5, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBSArm02", f6 * 10F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zClutchLever", flag ? 0.0F : 30F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zNAutoClutch", f6 * -10F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSightAngle", 0.0F, 0.0F, -f1);
        this.mesh.chunkSetAngles("zMirDrClutch", 0.0F, 0.0F, 5F * f1);
        this.mesh.chunkSetAngles("zBSpeed", 0.0F, 0.0F, -(f3 * 0.5F) - f2 * 0.1F);
        this.mesh.chunkSetAngles("zNTurnDrift", f4, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zWiperBottom", cvt(interp(setNew.wiper, setOld.wiper, f), -1F, 1.0F, -61F, 61F), 0.0F, 0.0F);
        for(int i = 0; i < internalBombs.length; i++)
            this.mesh.chunkVisible("rack" + (i + 1) + "On", internalBombs[i].haveBullets());

        this.mesh.chunkSetLocate("zArmingPLRod", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("zHSalvoLock", 0.0F, 0.0F, salvoLever * 30F);
        this.mesh.chunkSetAngles("zHSalvoLockRod", 0.0F, 0.0F, salvoLever * 29.9F);
        this.mesh.chunkSetAngles("zHSalvoLkPL", 0.0F, 0.0F, salvoLever * 20.5F);
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.0075F * salvoLever;
        this.mesh.chunkSetLocate("zHSalvoLkPLRod", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkVisible("bombReleaseOn", this.fm.CT.saveWeaponControl[3]);
        this.mesh.chunkSetAngles("zAlt2", 0.0F, 0.0F, -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F));
        this.mesh.chunkSetAngles("zAlt1", 0.0F, 0.0F, -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F));
        this.mesh.chunkSetAngles("zCompass1", 0.0F, 0.0F, -setNew.azimuth.getDeg(f) - 90F);
        this.mesh.chunkSetAngles("zCompass2", 0.0F, 0.0F, -setNew.waypointAzimuth.getDeg(f * 0.1F) - 90F);
        this.mesh.chunkSetAngles("zCompass3", -setNew.radioCompassAzimuth.getDeg(f * 0.02F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSpeed1", 0.0F, 0.0F, -floatindex(cvt(Pitot.Indicator((float)this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), speedometerScale));
        if(this.fm.CT.WeaponControl[0] && patronMat != null)
        {
            patron += 0.07F * f;
            patronMat.setLayer(0);
            patronMat.set((byte)11, patron);
        }
        this.mesh.chunkSetAngles("zO2CylPress", 0.0F, 0.0F, -130F);
    }

    public void toggleLight()
    {
        this.cockpitLightControl = !this.cockpitLightControl;
        if(this.cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    private SoundFX fxw;
    private Sample wiStart;
    private Sample wiRun;
    private int wiState;
    private int iWiper;
    private float patron;
    private Mat patronMat;
    private float salvoLever;
    private BulletEmitter internalBombs[];
    private static final float speedometerScale[] = {
        0.0F, 2.5F, 54F, 104F, 154.5F, 205.5F, 224F, 242F, 259.5F, 277.5F, 
        296.25F, 314F, 334F, 344.5F
    };
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
}
