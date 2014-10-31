package com.maddox.il2.objects.air;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.maddox.JGP.Matrix3d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FMMath;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;
import com.maddox.sound.SoundFX;

public class CockpitAV_8A extends CockpitPilot {
	private class ClipBoard {

		public int setPage(int val) {
			hidePages();
			if (val < 1)
				page_ = 1;
			else if (val > MAX_PAGE)
				page_ = MAX_PAGE;
			else
				page_ = val;
			mesh.chunkVisible("Int_ClipBoardP" + page_, true);
			return page_;
		}

		public void hidePages() {
			for (int i = 1; i <= MAX_PAGE; i++)
				mesh.chunkVisible("Int_ClipBoardP" + i, false);

		}

		public void clipBoardShowHide(boolean isShow) {
			if (isShow) {
				Cockpit.xyz[0] = 0.05F;
				Cockpit.xyz[1] = 0.255F;
				Cockpit.xyz[2] = -0.05F;
				mesh.chunkSetLocate("Int_ClipBoard", Cockpit.xyz, Cockpit.ypr);
			} else {
				Cockpit.xyz[0] = 0.0F;
				Cockpit.xyz[1] = 0.0F;
				Cockpit.xyz[2] = 0.0F;
				mesh.chunkSetLocate("Int_ClipBoard", Cockpit.xyz, Cockpit.ypr);
			}
		}

		int page_;
		public static final int MAX_PAGE = 10;

		private ClipBoard() {
			page_ = 1;
			hidePages();
			mesh.chunkVisible("Int_ClipBoardP1", true);
		}

		ClipBoard(ClipBoard clipboard) {
			this();
		}
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			aircraft().hierMesh().chunkVisible("Canopy_D0", false);
			aircraft().hierMesh().chunkVisible("Blister1_D0", false);
			aircraft().hierMesh().chunkVisible("Head1_D0", false);
			aircraft().hierMesh().chunkVisible("HMask1_D0", false);
			aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
			aircraft().hierMesh().chunkVisible("Seat1_D0", false);
			aircraft().hierMesh().chunkVisible("Refillrod1", false);
			aircraft().hierMesh().chunkVisible("Refillrod2", false);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		aircraft().hierMesh().chunkVisible("Canopy_D0", true);
		aircraft().hierMesh().chunkVisible("Blister1_D0", true);
		aircraft().hierMesh().chunkVisible("Head1_D0", true);
		aircraft().hierMesh().chunkVisible("HMask1_D0", true);
		aircraft().hierMesh().chunkVisible("Pilot1_D0", true);
		aircraft().hierMesh().chunkVisible("Seat1_D0", true);
		aircraft().hierMesh().chunkVisible("Refillrod1", true);
		aircraft().hierMesh().chunkVisible("Refillrod2", true);
		super.doFocusLeave();
	}

	class Interpolater extends InterpolateRef {

		public boolean tick() {
			setTmp = setOld;
			setOld = setNew;
			setNew = setTmp;
			setNew.altimeter = fm.getAltitude();
			setNew.throttlel = (10F * setOld.throttlel + ((FlightModelMain) (fm)).EI.engines[0].getControlThrottle()) / 11F;
			setNew.throttler = (10F * setOld.throttler + ((FlightModelMain) (fm)).EI.engines[0].getControlThrottle()) / 11F;
			float f = 200F;
			if(((AV_8) aircraft()).radargunsight == 0)
				f = 200F;
			if(((AV_8) aircraft()).radargunsight == 1)
				f = ((AV_8) aircraft()).k14Distance;
			setNew.k14w = (5F * CockpitAV_8A.k14TargetWingspanScale[((AV_8) aircraft()).k14WingspanType]) / f;
			setNew.k14w = 0.9F * setOld.k14w + 0.1F * setNew.k14w;
			setNew.k14wingspan = 0.9F * setOld.k14wingspan + 0.1F * CockpitAV_8A.k14TargetMarkScale[((AV_8) aircraft()).k14WingspanType];
			setNew.k14mode = 0.8F * setOld.k14mode + 0.2F * (float) ((AV_8) aircraft()).k14Mode;
			com.maddox.JGP.Vector3d vector3d = ((SndAircraft) (aircraft())).FM.getW();
			double d = 0.00125D * (double) f;
			float f1 = (float) Math.toDegrees(d * ((Tuple3d) (vector3d)).z);
			float f2 = -(float) Math.toDegrees(d * ((Tuple3d) (vector3d)).y);
			float f3 = floatindex((f - 200F) * 0.04F, CockpitAV_8A.k14BulletDrop) - CockpitAV_8A.k14BulletDrop[0];
			f2 += (float) Math.toDegrees(Math.atan(f3 / f));
			setNew.k14x = 0.92F * setOld.k14x + 0.08F * f1;
			setNew.k14y = 0.92F * setOld.k14y + 0.08F * f2;
			if (setNew.k14x > 7F)
				setNew.k14x = 7F;
			if (setNew.k14x < -7F)
				setNew.k14x = -7F;
			if (setNew.k14y > 7F)
				setNew.k14y = 7F;
			if (setNew.k14y < -7F)
				setNew.k14y = -7F;
			f = waypointAzimuth();
			setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), ((FlightModelMain) (fm)).Or.azimut());
			if (useRealisticNavigationInstruments()) {
				setNew.waypointAzimuth.setDeg(f - 90F);
				setOld.waypointAzimuth.setDeg(f - 90F);
				setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.02F), radioCompassAzimuthInvertMinus() - setOld.azimuth.getDeg(1.0F) - 90F);
				if (((FlightModelMain) (fm)).AS.listenLorenzBlindLanding) {
					setNew.ilsLoc = (10F * setOld.ilsLoc + getBeaconDirection()) / 11F;
					setNew.ilsGS = (10F * setOld.ilsGS + getGlidePath()) / 11F;
					setNew.navDiviation0 = setNew.ilsLoc;
				} else {
					setNew.ilsLoc = 0.0F;
					setNew.ilsGS = 0.0F;
					setNew.navDiviation0 = normalizeDegree(normalizeDegree(waypointAzimuth(0.02F) + 90F) - normalizeDegree(getNDBDirection()));
					if (Math.abs(setNew.navDiviation0) < 90F) {
					} else if (setNew.navDiviation0 > 270F) {
						setNew.navDiviation0 = setNew.navDiviation0 - 360F;
					} else {
						setNew.navDiviation0 = normalizeDegree180(setNew.navDiviation0);
						if (setNew.navDiviation0 >= 90F)
							setNew.navDiviation0 = 180F - setNew.navDiviation0;
						else
							setNew.navDiviation0 = -180F - setNew.navDiviation0;
					}
				}
			} else {
				setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f);
				setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(0.1F) - 90F);
			}
			setNew.vspeed = (299F * setOld.vspeed + fm.getVertSpeed()) / 300F;
			setNew.vspeed2 = (49F * setOld.vspeed2 + fm.getVertSpeed()) / 50F;
			setNew.pitch = ((FlightModelMain) (fm)).Or.getPitch();
			setNew.bank = ((FlightModelMain) (fm)).Or.getRoll();
			Vector3d vec = new Vector3d();
			((FlightModelMain) (fm)).Or.transformInv(((FlightModelMain) (fm)).Vwld, vec);
			setNew.fpmPitch = FMMath.RAD2DEG(-(float) Math.atan2(((Tuple3d) (vec)).z, ((Tuple3d) (vec)).x));
			setNew.fpmYaw = FMMath.RAD2DEG((float) Math.atan2(((Tuple3d) (vec)).y, ((Tuple3d) (vec)).x));
			if (cockpitDimControl) {
				if (setNew.dimPosition > 0.0F)
					setNew.dimPosition = setOld.dimPosition - 0.05F;
			} else if (setNew.dimPosition < 1.0F)
				setNew.dimPosition = setOld.dimPosition + 0.05F;
			if (((FlightModelMain) (fm)).EI.engines[0].getRPM() > 200F || ((FlightModelMain) (fm)).EI.engines[0].getRPM() > 200F)
				setNew.isGeneratorAllive = true;
			else
				setNew.isGeneratorAllive = false;
			f = ((AV_8) aircraft()).fSightCurForwardAngle;
			f1 = ((AV_8) aircraft()).fSightCurSideslip;
			f2 = fm.getAltitude();
			f3 = (float) (-(Math.abs(fm.Vwld.length()) * Math.sin(Math.toRadians(Math.abs(fm.Or.getTangage())))) * 0.10189999639987946D);
			f3 += (float) Math.sqrt(f3 * f3 + 2.0F * f2 * 0.1019F);
			float f4 = Math.abs((float) fm.Vwld.length()) * (float) Math.cos(Math.toRadians(Math.abs(fm.Or.getTangage())));
			float f5 = (f4 * f3 + 10F) - 10F;
			alpha = 90F - Math.abs(fm.Or.getTangage()) - (float) Math.toDegrees(Math.atan(f5 / f2));
			return true;
		}

		Interpolater() {
		}
	}

	private class Variables {

		float altimeter;
		float throttlel;
		float throttler;
		AnglesFork azimuth;
		AnglesFork waypointAzimuth;
		AnglesFork radioCompassAzimuth;
		float ilsLoc;
		float ilsGS;
		float navDiviation0;
		float vspeed;
		float vspeed2;
		float dimPosition;
		float pitch;
		float bank;
		float fpmPitch;
		float fpmYaw;
		boolean isGeneratorAllive;
		boolean isBatteryOn;
		float k14wingspan;
		float k14mode;
		float k14x;
		float k14y;
		float k14w;

		private Variables() {
			azimuth = new AnglesFork();
			waypointAzimuth = new AnglesFork();
			radioCompassAzimuth = new AnglesFork();
			isGeneratorAllive = false;
			isBatteryOn = false;
		}

		Variables(Variables variables) {
			this();
		}
	}

	protected float waypointAzimuth() {
		return super.waypointAzimuthInvertMinus(10F);
	}

	public CockpitAV_8A() {
		super("3DO/Cockpit/AV-8A/hier.him", "bf109");
		setOld = new Variables(null);
		setNew = new Variables(null);
		w = new Vector3f();
		tmpP = new Point3d();
		tmpV = new Vector3d();
		tmpMat = new Matrix3d();
		bNeedSetUp = true;
		isDimmer = false;
		blinkCounter = 0;
		super.cockpitNightMats = (new String[] { "2petitsb_d1", "2petitsb", "aiguill1", "badinetm_d1", "badinetm", "baguecom", "brasdele", "comptemu_d1", "comptemu", "petitfla_d1", "petitfla", "turnbank" });
		setNightMats(false);
		setNew.dimPosition = 1.0F;
		interpPut(new Interpolater(), null, Time.current(), null);
		if (super.acoustics != null)
			super.acoustics.globFX = new ReverbFXRoom(0.05F);
		aoaWarnFX = aircraft().newSound("aircraft.AOA_Slow", false);
		aoaWarnFX2 = aircraft().newSound("aircraft.AOA_Mid", false);
		aoaWarnFX3 = aircraft().newSound("aircraft.AOA_Fast", false);
		aoaWarnFX4 = aircraft().newSound("aircraft.AOA_Stall", false);
		clipBoard = new ClipBoard(null);
		hudPitchRudderStr = new String[37];
		for (int i = 18; i >= 0; i--)
			hudPitchRudderStr[i] = "Z_Z_HUD_PITCH" + (90 - i * 5);

		for (int i = 1; i <= 18; i++)
			hudPitchRudderStr[i + 18] = "Z_Z_HUD_PITCHN" + i * 5;

		((AircraftLH) aircraft()).bWantBeaconKeys = true;
		t = 0; //TODO:Radar parameter
		x = 0;
		t2 = 0;
		FOV = 1.0D;
        ScX = 0.0069999997764825821D;
        ScY = 0.0000055;
        ScZ = 0.0010000000474974513D;
        FOrigX = 0.0F;
        FOrigY = 0.0F;
        nTgts = 10;
        RRange = 50000F;
        RClose = 5F;
        BRange = 0.1F;
        BRefresh = 1300;
        BSteps = 12;
        BDiv = BRefresh / BSteps;
        tBOld = 0L;
        radarPlane = new ArrayList();
	}

	public void reflectWorldToInstruments(float f) {
		if(bNeedSetUp)
        {
            reflectPlaneMats();
            reflectPlaneToModel();
            bNeedSetUp = false;
        }
		if ((fm.AS.astateCockpitState & 2) == 0) {
			int i = ((AV_8) aircraft()).k14Mode;
			resetYPRmodifier();
			Cockpit.xyz[0] = setNew.k14w;
			if (i == 0) {				
				resetYPRmodifier();
				super.mesh.chunkSetAngles("Z_Z_Bomb", 0.0F, 0.0F, -((FlightModelMain) (fm)).Or.getRoll());
				super.mesh.chunkSetAngles("Z_Z_Bombsteer", -setNew.k14x, 0.0F, 0.0F);
				super.mesh.chunkVisible("Z_Z_Bombsteer", true);
				super.mesh.chunkVisible("Z_Z_Bombmark2", true);
				super.mesh.chunkVisible("Z_Z_Bombmark3", true);
				super.mesh.chunkVisible("Z_Z_Bombmark4", true);
				super.mesh.chunkSetAngles("Z_Z_Bombmark", 0.0F, -1.4F * alpha - setNew.k14y, 0.0F);
				super.mesh.chunkVisible("Z_Z_RETICLE", false);
				super.mesh.chunkVisible("Z_Z_BulletdropL", false);
				super.mesh.chunkVisible("Z_Z_BulletdropR", false);
				Cockpit.xyz[2] = 1.3F;
				super.mesh.chunkSetLocate("Z_Z_Bombmark2", Cockpit.xyz, Cockpit.ypr);
				Cockpit.xyz[2] = ((FlightModelMain) (fm)).getAltitude()/700F;
				super.mesh.chunkSetLocate("Z_Z_Bombmark4", Cockpit.xyz, Cockpit.ypr);				
			}
			if (i == 1) {//TODO funnel
				super.mesh.chunkVisible("Z_Z_Bombsteer", false);
				super.mesh.chunkVisible("Z_Z_Bombmark2", false);
				super.mesh.chunkVisible("Z_Z_Bombmark3", false);
				super.mesh.chunkVisible("Z_Z_Bombmark4", false);
				if(((AV_8) aircraft()).radargunsight == 1){
				super.mesh.chunkSetAngles("Z_Z_RETICLE", -setNew.k14x, -setNew.k14y, 0.0F);
				super.mesh.chunkVisible("Z_Z_RETICLE", true);
				super.mesh.chunkVisible("Z_Z_BulletdropL", false);
				super.mesh.chunkVisible("Z_Z_BulletdropR", false);
				radarlock();} else
				if(((AV_8) aircraft()).radargunsight == 0){
					super.mesh.chunkVisible("Z_Z_RETICLE", false);
					super.mesh.chunkVisible("Z_Z_BulletdropL", true);
					super.mesh.chunkVisible("Z_Z_BulletdropR", true);
					//super.mesh.chunkSetAngles("Z_Z_Bulletdrop", -setNew.k14x - ((FlightModelMain) (fm)).Or.getRoll(), 0.0F, -setNew.k14y);
					super.mesh.chunkSetAngles("Z_Z_Bulletdrop", setNew.k14x*27F, 0.0F, setNew.k14y*10F);
					resetYPRmodifier();
					Cockpit.xyz[1] = -setNew.k14y/100F;
					super.mesh.chunkSetLocate("Z_Z_BulletdropL", Cockpit.xyz, Cockpit.ypr);
					Cockpit.xyz[1] = setNew.k14y/100F;
					super.mesh.chunkSetLocate("Z_Z_BulletdropR", Cockpit.xyz, Cockpit.ypr);
				}
			}
			if (i > 1) {
				super.mesh.chunkVisible("Z_Z_RETICLE", false);
				super.mesh.chunkVisible("Z_Z_Bombsteer", false);
				super.mesh.chunkVisible("Z_Z_Bombmark2", false);
				super.mesh.chunkVisible("Z_Z_Bombmark3", false);
				super.mesh.chunkVisible("Z_Z_Bombmark4", false);
				super.mesh.chunkVisible("Z_Z_BulletdropL", false);
				super.mesh.chunkVisible("Z_Z_BulletdropR", false);
			}
		}
		if (bNeedSetUp) {
			super.mesh.chunkVisible("Int_Marker", false);
			bNeedSetUp = false;
		}
		((AircraftLH) aircraft()).bWantBeaconKeys = true;
		resetYPRmodifier();
		moveControls(f);
		moveHUD(f);
		moveGauge(f);
		drawSound(f);
		mesh.chunkSetAngles("Refillrod1", Aircraft.cvt(super.fm.CT.RefuelControl, 0.0F, 0.5F, 0.0F, 40F), 0.0F, 0.0F);
		Cockpit.xyz[1] = Aircraft.cvt(super.fm.CT.RefuelControl, 0.0F, 1.0F, 0.0F, -0.5F);
    	mesh.chunkSetLocate("Refillrod2", Cockpit.xyz, Cockpit.ypr);
		if (fm.AS.bShowSmokesOn)
			super.mesh.chunkVisible("Int_SmokeON", true);
		else
			super.mesh.chunkVisible("Int_SmokeON", false);
		if (fm.CT.getAirBrake() > 0.0F)
			super.mesh.chunkVisible("Int_Speed_Ext", true);
		else
			super.mesh.chunkVisible("Int_Speed_Ext", false);
		if (fm.CT.getGear() < 0.999999F)
			super.mesh.chunkVisible("Int_LDG_ON", false);
		else
			super.mesh.chunkVisible("Int_LDG_ON", true);
		super.mesh.chunkVisible("Int_Marker", isDimmer);
		fm.Or.getMatrix(tmpMat);
		float shadowPosP = (float) tmpMat.getElement(2, 0);
		shadowPosP = 1.0F - shadowPosP;
		shadowPosP *= 0.045F;
		shadowPosP += (float) tmpMat.getElement(2, 2) * -0.014F;
		if (shadowPosP < 0.0F)
			shadowPosP = 0.0F;
		resetYPRmodifier();
		Cockpit.xyz[2] = shadowPosP;
		super.mesh.chunkSetLocate("ShadowMove1", Cockpit.xyz, Cockpit.ypr);
		radarclutter();
	}
	
	public long t;
	public int x;
	public void radarclutter() {//TODO: Radar
		boolean radar = false;
		long tr = Time.current();
		float l = 3F;
		float e = x*l;
		if(e >= 108F)
			e = 108F;
		float f = x*l;
		if(f >= 216F)
			f = 216F;		
		if(((AV_8) aircraft()).k14Mode == 2)
		{
			radar = true;
		} else
		{	
			radar = false;
			super.mesh.chunkVisible("Z_Z_lockgate", false);
			super.mesh.chunkVisible("Z_Z_RadarFrame", false);
			if(((AV_8) aircraft()).k14Mode != 1){
			super.mesh.chunkVisible("Z_Z_radarlock", false);
			super.mesh.chunkVisible("Z_Z_missilelock", false);}
		}			
		boolean radarscan = false;
		if(radar && ((AV_8) aircraft()).radarmode == 0)
			radarscan = true;
		if(!radar || ((AV_8) aircraft()).radarmode != 0)
			radarscan = false;
		super.mesh.chunkVisible("Z_Z_Scan_1", radarscan);
        super.mesh.chunkVisible("Z_Z_Scan_2", radarscan);       
		if(!radar){
			for(int j = 0; j <= nTgts; j++)
            {
                String m = "Z_Z_RadarMark" + j;
                if(super.mesh.isChunkVisible(m))
                     super.mesh.chunkVisible(m, false);            
            }
			return;
		}		
		if(((AV_8) aircraft()).radarmode == 0)
		{
		super.mesh.chunkVisible("Z_Z_lockgate", false);
		super.mesh.chunkVisible("Z_Z_radarlock", false);
		super.mesh.chunkVisible("Z_Z_missilelock", false);
		super.mesh.chunkVisible("Z_Z_RadarFrame", true);
		radarscan();		
		if(tr>t + 5L){
			t = tr;
			x++;
		}
		if(x >= 0 && x <= (108/l)){
			resetYPRmodifier();
	        Cockpit.xyz[1] = -e/300F;
	        super.mesh.chunkVisible("Z_Z_Scan_1", true);
	        super.mesh.chunkVisible("Z_Z_Scan_2", false);
	        super.mesh.chunkSetLocate("Z_Z_Scan_1", Cockpit.xyz, Cockpit.ypr);
		} else
		if(x > (108/l) && x <= (216/l)){
			resetYPRmodifier();
	        Cockpit.xyz[1] = (f - 108F)/300F;
	        super.mesh.chunkVisible("Z_Z_Scan_2", true);
	        super.mesh.chunkVisible("Z_Z_Scan_1", false);
	        super.mesh.chunkSetLocate("Z_Z_Scan_2", Cockpit.xyz, Cockpit.ypr);
		} else
		if(x>(216/l))
        	x = 0;
		}
		if(((AV_8) aircraft()).radarmode == 1 && ((AV_8) aircraft()).lockmode == 0)
		{
			radaracquire();
			super.mesh.chunkVisible("Z_Z_RadarFrame", true);
			super.mesh.chunkVisible("Z_Z_radarlock", false);
			super.mesh.chunkVisible("Z_Z_missilelock", false);
		}
		if(((AV_8) aircraft()).radarmode == 1 && ((AV_8) aircraft()).lockmode == 1)
		{
			radarlock();
			super.mesh.chunkVisible("Z_Z_RadarFrame", false);
			super.mesh.chunkVisible("Z_Z_lockgate", false);
			for(int j = 0; j <= nTgts; j++)
            {
                String m = "Z_Z_RadarMark" + j;
                if(super.mesh.isChunkVisible(m))
                     super.mesh.chunkVisible(m, false);            
            }
		}
	}
	
	private ArrayList radarPlane;
	private long t2;
	double FOV;
    double ScX;
    double ScY;
    double ScZ;
    float FOrigX;
    float FOrigY;
    int nTgts;
    float RRange;
    float RClose;
    float BRange;
    int BRefresh;
    int BSteps;
    float BDiv;
    long tBOld;
    
    private int iLockState()
    {
        if(!(super.aircraft() instanceof TypeGuidedMissileCarrier))
            return 0;
        else
            return ((TypeGuidedMissileCarrier)super.aircraft()).getGuidedMissileUtils().getMissileLockState();
    }
    
    public void radarlock() //TODO radarlock
    {
        try
        {
            Aircraft ownaircraft = World.getPlayerAircraft();           
            radarPlane.clear();
            if(Actor.isValid(ownaircraft) && Actor.isAlive(ownaircraft))
            {                                  
                    Point3d pointAC = ((Actor) (ownaircraft)).pos.getAbsPoint();
                    Orient orientAC = ((Actor) (ownaircraft)).pos.getAbsOrient();                   
                    List list = Engine.targets();
                    int i = list.size();                   
                    for(int j = 0; j < i; j++)
                    {
                        Actor actor = (Actor)list.get(j);
                        if((actor instanceof Aircraft) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy()) //basically tell that target is not your own aircraft and not friendly aircraft
                        {
                        	
                        	Vector3d vector3d = new Vector3d();                       	
                        	vector3d.set(pointAC);                       	
                        	Point3d pointOrtho = new Point3d();
                            pointOrtho.set(actor.pos.getAbsPoint());
                            pointOrtho.sub(pointAC);
                            orientAC.transformInv(pointOrtho);
                            float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                            if(((Tuple3d) (pointOrtho)).x > (double)RClose && ((Tuple3d) (pointOrtho)).x < (double)RRange - (double)(350F * f) && (((Tuple3d) (pointOrtho)).y < ((Tuple3d) (pointOrtho)).x * 0.57735026919 && ((Tuple3d) (pointOrtho)).y > -((Tuple3d) (pointOrtho)).x * 0.57735026919) && (((Tuple3d) (pointOrtho)).z < ((Tuple3d) (pointOrtho)).x * 0.36397023426 && ((Tuple3d) (pointOrtho)).z > -((Tuple3d) (pointOrtho)).x * 0.36397023426))
                                radarPlane.add(pointOrtho);                                                     
                        }               	
                    }                  
                int i1 = radarPlane.size();
                if(i1>0)
                {	
                int nt = 0;
                targetnum = ((AV_8) aircraft()).targetnum;
                if(((AV_8) aircraft()).targetnum>=i1)
                	((AV_8) aircraft()).targetnum = i1;
                int k = this.targetnum;
                if(i>0)
                {              	
                	Actor actor = (Actor)list.get(k);
                	Vector3d tarvec = new Vector3d();
                	double d = actor.getSpeed(tarvec);
                	//HUD.log(AircraftHotKeys.hudLogWeaponId, "target speed" + d);
                }
                //HUD.log(AircraftHotKeys.hudLogWeaponId, "target num" + k);
                if(i1>0)
                {
                	double x1 = ((Tuple3d) ((Point3d)radarPlane.get(k))).x;                   
                    if(x1 > (double)RClose && nt <= nTgts)
                    {
                    	FOV = 300D / x1; // distance relationship, to adjust the deviation of radar mark when getting closer to target planes
                        double NewX = -((Tuple3d) ((Point3d)radarPlane.get(k))).y * FOV; // spanning
                        double NewY = ((Tuple3d) ((Point3d)radarPlane.get(k))).z * FOV; //distance
                        float f = FOrigX + (float)(NewX * 0.0099999997764825821D) + ((float)Math.sin(Math.toRadians(((FlightModelMain) (super.fm)).Or.getKren())) * 0.011F); //FOrigX currently do nothing
                        float f1 = FOrigY + (float)(NewY * 0.0099999997764825821D);                                                                    
                        String m = "Z_Z_radarlock";
                        super.mesh.setCurChunk(m);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f;
                        Cockpit.xyz[2] = f1;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        //if(f>=0.18F || f<=-0.18F || f1>0.36F)
                        	//super.mesh.chunkVisible(m, false);
                        //else
                        if(!super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, true);
                        if(iLockState() == 2)
                        	super.mesh.chunkVisible("Z_Z_missilelock", true); else super.mesh.chunkVisible("Z_Z_missilelock", false);
                    }
                }                                               
                } else              
                {	// hide everything when there's no enemy
                	if(super.mesh.isChunkVisible("Z_Z_radarlock"))
                        super.mesh.chunkVisible("Z_Z_radarlock", false);
                	if(super.mesh.isChunkVisible("Z_Z_missilelock"))
                        super.mesh.chunkVisible("Z_Z_missilelock", false);
                    
                } 
            }                 
        }    	
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }
	
	public void radaracquire() //TODO scanacquire
    {
        try
        {
            Aircraft ownaircraft = World.getPlayerAircraft();           
            radarPlane.clear();
            if(Actor.isValid(ownaircraft) && Actor.isAlive(ownaircraft))
            {                                  
                    Point3d pointAC = ((Actor) (ownaircraft)).pos.getAbsPoint();
                    Orient orientAC = ((Actor) (ownaircraft)).pos.getAbsOrient();                   
                    List list = Engine.targets();
                    int i = list.size();                   
                    for(int j = 0; j < i; j++)
                    {
                        Actor actor = (Actor)list.get(j);
                        if((actor instanceof Aircraft) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy()) //basically tell that target is not your own aircraft and not friendly aircraft
                        {
                        	
                        	Vector3d vector3d = new Vector3d();                       	
                        	vector3d.set(pointAC);                       	
                        	Point3d pointOrtho = new Point3d();
                            pointOrtho.set(actor.pos.getAbsPoint());
                            pointOrtho.sub(pointAC);
                            orientAC.transformInv(pointOrtho);
                            float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                            if(((Tuple3d) (pointOrtho)).x > (double)RClose && ((Tuple3d) (pointOrtho)).x < (double)RRange - (double)(350F * f) && (((Tuple3d) (pointOrtho)).y < ((Tuple3d) (pointOrtho)).x * 0.57735026919 && ((Tuple3d) (pointOrtho)).y > -((Tuple3d) (pointOrtho)).x * 0.57735026919) && (((Tuple3d) (pointOrtho)).z < ((Tuple3d) (pointOrtho)).x * 0.36397023426 && ((Tuple3d) (pointOrtho)).z > -((Tuple3d) (pointOrtho)).x * 0.36397023426))
                                radarPlane.add(pointOrtho);                                                     
                        }               	
                    }                  
                int i1 = radarPlane.size();
                if(i1>0)
                {	
                int nt = 0;
                targetnum = ((AV_8) aircraft()).targetnum;
                if(((AV_8) aircraft()).targetnum>=i1)
                	((AV_8) aircraft()).targetnum = i1;
                int k = this.targetnum;
                if(i>0)
                {              	
                	Actor actor = (Actor)list.get(k);
                	Vector3d tarvec = new Vector3d();
                	double d = actor.getSpeed(tarvec);
                	//HUD.log(AircraftHotKeys.hudLogWeaponId, "target speed" + d);
                }
                //HUD.log(AircraftHotKeys.hudLogWeaponId, "target num" + k);
                if(i1>0)
                {
                	double x1 = ((Tuple3d) ((Point3d)radarPlane.get(k))).x;                   
                    if(x1 > (double)RClose && nt <= nTgts)
                    {
                    	FOV = 60D / x1; // distance relationship, to adjust the deviation of radar mark when getting closer to target planes
                        double NewX = -((Tuple3d) ((Point3d)radarPlane.get(k))).y * FOV; // spanning
                        double NewY = ((Tuple3d) ((Point3d)radarPlane.get(k))).x; //distance
                        float f = FOrigX + (float)(NewX * ScX) + ((float)Math.sin(Math.toRadians(((FlightModelMain) (super.fm)).Or.getKren())) * 0.011F); //FOrigX currently do nothing
                        float f1 = FOrigY + (float)(NewY * ScY);
                        if(f1 < 0)
                        	f1 = 0;
                        //HUD.log(AircraftHotKeys.hudLogWeaponId, "span" + f);                       
                        String m = "Z_Z_lockgate";
                        super.mesh.setCurChunk(m);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f;
                        Cockpit.xyz[2] = f1;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        if(f>=0.18F || f<=-0.18F || f1>0.36F)
                        	super.mesh.chunkVisible(m, false);
                        else
                        if(!super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, true);
                        
                    }
                }                
                for(int j = 0; j < i1; j++)
                {
                    double x = ((Tuple3d) ((Point3d)radarPlane.get(j))).x;                   
                    if(x > (double)RClose && nt <= nTgts)
                    {
                    	FOV = 60D / x; // distance relationship, to adjust the deviation of radar mark when getting closer to target planes
                        double NewX = -((Tuple3d) ((Point3d)radarPlane.get(j))).y * FOV; // spanning
                        double NewY = ((Tuple3d) ((Point3d)radarPlane.get(j))).x; //distance
                        float f = FOrigX + (float)(NewX * ScX) + ((float)Math.sin(Math.toRadians(((FlightModelMain) (super.fm)).Or.getKren())) * 0.011F); //FOrigX currently do nothing
                        float f1 = FOrigY + (float)(NewY * ScY);
                        if(f1 < 0)
                        	f1 = 0;
                        //HUD.log(AircraftHotKeys.hudLogWeaponId, "span" + f);
                        nt++; // number of marks, from 0 -> 10
                        String m = "Z_Z_RadarMark" + nt;
                        super.mesh.setCurChunk(m);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f;
                        Cockpit.xyz[2] = f1;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        if(f>=0.18F || f<=-0.18F || f1>0.36F)
                        	super.mesh.chunkVisible(m, false);
                        else
                        if(!super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, true);
                        
                    }	
                }  
                for(int j = nt + 1; j <= nTgts; j++)
                {
                    String m = "Z_Z_RadarMark" + j;
                    if(super.mesh.isChunkVisible(m))
                        super.mesh.chunkVisible(m, false);
                }                
                } else              
                {	// hide everything when there's no enemy
                	if(super.mesh.isChunkVisible("Z_Z_RadarMark0"))
                        super.mesh.chunkVisible("Z_Z_RadarMark0", false);
                	if(super.mesh.isChunkVisible("Z_Z_RadarMark11"))
                        super.mesh.chunkVisible("Z_Z_RadarMark11", false);
                	if(super.mesh.isChunkVisible("Z_Z_lockgate"))
                        super.mesh.chunkVisible("Z_Z_lockgate", false);
                    for(int j = 0; j <= nTgts + 1; j++)
                    {
                        String m = "Z_Z_RadarMark" + j;
                        if(super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, false);
                    }
                    
                } 
            } 
                
        }    	
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }
	
	protected float offset;
	protected int targetnum;
	
	public void radarscan() // TODO radar scan
    {
        try
        {
            Aircraft ownaircraft = World.getPlayerAircraft();            
            long ti = (Time.current() + World.Rnd().nextLong(-250L, 250L)); // track delay (target mark appear disappear)
            if(ti > t2 + 1250L)
            radarPlane.clear();
            if(Actor.isValid(ownaircraft) && Actor.isAlive(ownaircraft))
            {               
                if(ti > t2 + 2000L)
                {
                    t2 = ti;
                    Point3d pointAC = ((Actor) (ownaircraft)).pos.getAbsPoint();
                    Orient orientAC = ((Actor) (ownaircraft)).pos.getAbsOrient();
                    
                    List list = Engine.targets();
                    int i = list.size();
                    for(int j = 0; j < i; j++)
                    {
                        Actor actor = (Actor)list.get(j);
                        if((actor instanceof Aircraft) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy()) //basically tell that target is not your own aircraft and not friendly aircraft
                        {
                        	Vector3d vector3d = new Vector3d(); 
                        	vector3d.set(pointAC);                       	
                        	Point3d pointOrtho = new Point3d();
                            pointOrtho.set(actor.pos.getAbsPoint());
                            pointOrtho.sub(pointAC);
                            orientAC.transformInv(pointOrtho);
                            float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                            if(((Tuple3d) (pointOrtho)).x > (double)RClose && ((Tuple3d) (pointOrtho)).x < (double)RRange - (double)(350F * f) && (((Tuple3d) (pointOrtho)).y < ((Tuple3d) (pointOrtho)).x * 0.57735026919 && ((Tuple3d) (pointOrtho)).y > -((Tuple3d) (pointOrtho)).x * 0.57735026919) && (((Tuple3d) (pointOrtho)).z < ((Tuple3d) (pointOrtho)).x * 0.36397023426 && ((Tuple3d) (pointOrtho)).z > -((Tuple3d) (pointOrtho)).x * 0.36397023426))
                                radarPlane.add(pointOrtho);                           
                        }
                    }               	               
                int i1 = radarPlane.size();
                if(i1>0)
                {	
                int nt = 0;
                for(int j = 0; j < i1; j++)
                {
                    double x = ((Tuple3d) ((Point3d)radarPlane.get(j))).x;                   
                    if(x > (double)RClose && nt <= nTgts)
                    {
                    	FOV = 60D / x; // distance relationship, to adjust the deviation of radar mark when getting closer to target planes
                        double NewX = -((Tuple3d) ((Point3d)radarPlane.get(j))).y * FOV; // spanning
                        double NewY = ((Tuple3d) ((Point3d)radarPlane.get(j))).x; //distance
                        float f = FOrigX + (float)(NewX * ScX) + ((float)Math.sin(Math.toRadians(((FlightModelMain) (super.fm)).Or.getKren())) * 0.011F); //FOrigX currently do nothing
                        float f1 = FOrigY + (float)(NewY * ScY);
                        if(f1 < 0)
                        	f1 = 0;                       
                        nt++; // number of marks, from 0 -> 10
                        String m = "Z_Z_RadarMark" + nt;
                        super.mesh.setCurChunk(m);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f;
                        Cockpit.xyz[2] = f1;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        if(f>=0.18F || f<=-0.18F || f1>0.36F)
                        	super.mesh.chunkVisible(m, false);
                        else
                        if(!super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, true);
                        
                    }	
                }  
                for(int j = nt + 1; j <= nTgts; j++)
                {
                    String m = "Z_Z_RadarMark" + j;
                    if(super.mesh.isChunkVisible(m))
                        super.mesh.chunkVisible(m, false);
                }                
            } else               
                {	// hide everything when there's no enemy
                	if(super.mesh.isChunkVisible("Z_Z_RadarMark0"))
                        super.mesh.chunkVisible("Z_Z_RadarMark0", false);
                    for(int j = 0; j <= nTgts + 1; j++)
                    {
                        String m = "Z_Z_RadarMark" + j;
                        if(super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, false);
                    }
                }    
            }    
            }
            if(radarPlane.size() == 0)          
        {	// hide everything when there's no enemy
        	if(super.mesh.isChunkVisible("Z_Z_RadarMark0"))
                super.mesh.chunkVisible("Z_Z_RadarMark0", false);
            for(int j = 0; j <= nTgts + 1; j++)
            {
                String m = "Z_Z_RadarMark" + j;
                if(super.mesh.isChunkVisible(m))
                    super.mesh.chunkVisible(m, false);
            }
        }    
        }    	
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }
    
    protected void targetnum()
    {
    	List list = Engine.targets();
    	int j1 = list.size();
    	for(int k2 = ((F_18S)aircraft()).targetnum; k2 < j1; k2++)
        {
        	Aircraft[] hunted = (Aircraft[])list.get(k2);
        	if(hunted[k2] != World.getPlayerAircraft())
        	{                  	
        	float x = hunted[k2].FM.Or.getTangage();
        	float y = hunted[k2].FM.getSpeed();
        	float z = hunted[k2].FM.getAltitude();   
        	}
        }
    } //TODO Lock

	protected void moveControls(float f) {
		super.mesh.chunkSetAngles("FlightStick", fm.CT.AileronControl * 10F, 0.0F, fm.CT.ElevatorControl * 10F);
		super.mesh.chunkSetAngles("Throttole", 0.0F, 0.0F, fm.CT.PowerControl * -50F);
		Cockpit.xyz[0] = 0.0F;
		Cockpit.xyz[1] = 0.0F;
		Cockpit.xyz[2] = fm.CT.getRudder() * -0.07F;
		super.mesh.chunkSetLocate("FootPedal_L", Cockpit.xyz, Cockpit.ypr);
		Cockpit.xyz[2] = fm.CT.getRudder() * 0.07F;
		super.mesh.chunkSetLocate("FootPedal_R", Cockpit.xyz, Cockpit.ypr);		
	}

	protected void moveGauge(float f) {
		super.mesh.chunkSetAngles("Gauge_ADI1", setNew.bank, 0.0F, setNew.pitch);
		super.mesh.chunkSetAngles("Gauge_StdByADI1", setNew.bank, 0.0F, setNew.pitch);
		super.mesh.chunkSetAngles("GaugeMove_RPM_L", floatindex(cvt(fm.EI.engines[0].getRPM(), 0.0F, 3828F, 0.0F, 300F) / 100F, rpmScale), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("GaugeMove_RPM_R", floatindex(cvt(fm.EI.engines[0].getRPM(), 0.0F, 3828F, 0.0F, 300F) / 100F, rpmScale), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("GaugeMove_TGT_L", cvt(fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 200F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("GaugeMove_TGT_R", cvt(fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 200F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("GaugeMove_FF_L", cvt(fm.EI.engines[0].getRPM(), 0.0F, 3828F, 0.0F, 330F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("GaugeMove_FF_R", cvt(fm.EI.engines[0].getRPM(), 0.0F, 3828F, 0.0F, 330F), 0.0F, 0.0F);
		float alt = interp(setNew.altimeter, setOld.altimeter, f);
		float val = cvt(alt, 0.0F, 30480F, 0.0F, 36000F);
		super.mesh.chunkSetAngles("GaugeMove_ALT_N", val, 0.0F, 0.0F);
		super.mesh.chunkSetAngles("GaugeMove_ALT_3", 0.0F, 0.0F, -val);
		val = cvt(alt, 0.0F, 30480F, 0.0F, 3600F);
		float val2 = (float) (int) (val / 36F) * 36F;
		if (val - val2 > 34.2F)
			val2 += (1.0F - ((val2 + 36F) - val) / 1.8F) * 36F;
		super.mesh.chunkSetAngles("GaugeMove_ALT_4", 0.0F, 0.0F, -val2);
		val = cvt(alt, 0.0F, 30480F, 0.0F, 360F);
		val2 = (float) (int) (val / 36F) * 36F;
		if (val - val2 > 35.82F)
			val2 += (1.0F - ((val2 + 36F) - val) / 0.18F) * 36F;
		super.mesh.chunkSetAngles("GaugeMove_ALT_5", 0.0F, 0.0F, -val2);
		val = setNew.azimuth.getDeg(f) + 90F;
		super.mesh.chunkSetAngles("GaugeMove_HSI_HDG", -val, 0.0F, 0.0F);
		val = setNew.waypointAzimuth.getDeg(f) + 90F;
		super.mesh.chunkSetAngles("GaugeMove_HSI_CRS", val, 0.0F, 0.0F);
		val = setNew.radioCompassAzimuth.getDeg(f * 0.02F) + 90F + 180F;
		super.mesh.chunkSetAngles("GaugeMove_HSI_TACAN", val, 0.0F, 0.0F);
		resetYPRmodifier();
		if (fm.AS.listenLorenzBlindLanding) {
			val = setNew.ilsLoc;
			Cockpit.xyz[0] = -val * 0.001F;
		} else if (fm.AS.listenNDBeacon || fm.AS.listenRadioStation) {
			val = setNew.navDiviation0;
			Cockpit.xyz[0] = -val * 0.002F;
		}
		if (Cockpit.xyz[0] < -0.02F)
			Cockpit.xyz[0] = -0.02F;
		else if (Cockpit.xyz[0] > 0.02F)
			Cockpit.xyz[0] = 0.02F;
		super.mesh.chunkSetLocate("GaugeMove_HSI_DIV", Cockpit.xyz, Cockpit.ypr);
		float deg = 0.0F;
		float mach = calculateMach();
		deg = mach * 180F;
		super.mesh.chunkSetAngles("GaugeMove_SPD_MACH", deg, 0.0F, 0.0F);
		deg = 0.0F;
		float knot = Pitot.Indicator((float) ((Tuple3d) (fm.Loc)).z, super.fm.getSpeedKMH());
		knot *= 0.53996F;
		if (knot <= 50F)
			deg = (knot / 50F) * 15F;
		else if (knot <= 100F)
			deg = (knot - 50F) + 15F;
		else if (knot <= 150F)
			deg = (knot - 100F) * 1.2F + 65F;
		else if (knot <= 200F)
			deg = (knot - 150F) * 0.9F + 125F;
		else if (knot <= 300F)
			deg = (knot - 200F) * 0.6F + 170F;
		else if (knot <= 400F)
			deg = (knot - 300F) * 0.45F + 230F;
		else if (knot <= 700F)
			deg = (knot - 400F) * 0.3F + 275F;
		super.mesh.chunkSetAngles("GaugeMove_SPD", deg, 0.0F, 0.0F);
		float vs = setNew.vspeed2 * 3.48F;
		vs *= 60F;
		float vsabs = Math.abs(vs);
		boolean isNeg = vs < 0.0F;
		if (vsabs <= 1000F)
			val = vsabs * 0.07F;
		else if (vsabs <= 2000F)
			val = (vsabs - 1000F) * 0.035F + 70F;
		else if (vsabs <= 4000F)
			val = (vsabs - 2000F) * 0.0175F + 105F;
		else if (vsabs <= 6000F)
			val = (vsabs - 4000F) * 0.00875F + 140F;
		else
			val = 157.5F;
		if (isNeg)
			val = -val;
		super.mesh.chunkSetAngles("GaugeMove_VS", val, 0.0F, 0.0F);
		val = super.fm.getAOA() * 9F;
		if (val < 0.0F)
			val = 0.0F;
		else if (val > 270F)
			val = 270F;
		val *= -1F;
		super.mesh.chunkSetAngles("GaugeMove_AOA", val, 0.0F, 0.0F);
		float gLoad = super.fm.getOverload();
		gLoad--;
		val = gLoad * 20F;
		super.mesh.chunkSetAngles("GaugeMove_G", val, 0.0F, 0.0F);
		val = fm.M.fuel / fm.M.maxFuel;
		val *= 220F;
		super.mesh.chunkSetAngles("GaugeMove_FUEL_L", val, 0.0F, 0.0F);
		super.mesh.chunkSetAngles("GaugeMove_FUEL_R", val, 0.0F, 0.0F);
		if (fm.CT.getGear() > 0.0F)
			super.mesh.chunkSetAngles("GaugeMove_Gear", 0.0F, 0.0F, 0.0F);
		else
			super.mesh.chunkSetAngles("GaugeMove_Gear", 0.0F, 0.0F, -15.5F);
	}

	protected void moveHUD(float f) {//TODO: HUD
		blinkCounter++;
		float knot = Pitot.Indicator((float) ((Tuple3d) (fm.Loc)).z, super.fm.getSpeedKMH());
		knot *= 0.53996F;
		Cockpit.xyz[0] = 0.0F;
		Cockpit.xyz[1] = 0.001613948F * knot;
		Cockpit.xyz[2] = 0.0F;
		int isBlink = 0;
		boolean isHudDisp = false;
		if (!setNew.isBatteryOn && !setNew.isGeneratorAllive)
			isHudDisp = false;
		else
			isHudDisp = true;
		for (int i = 0; i < 37; i++)
			super.mesh.chunkVisible(hudPitchRudderStr[i], false);

		super.mesh.chunkVisible("Z_Z_HUD_PITCHN3", false);
		super.mesh.chunkVisible("Z_Z_ALT_1", isHudDisp);
		super.mesh.chunkVisible("Z_Z_ALT_2", isHudDisp);
		super.mesh.chunkVisible("Z_Z_ALT_3", isHudDisp);
		super.mesh.chunkVisible("Z_Z_ALT_4", isHudDisp);
		super.mesh.chunkVisible("Z_Z_ALT_5", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HUD_ALT_CY", isHudDisp);
		super.mesh.chunkVisible("Z_Z_G_1", isHudDisp);
		super.mesh.chunkVisible("Z_Z_G_2", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HDG_3", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HDG_2", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HDG_1", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HUD_HDG", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HUD_Mach_3", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HUD_Mach_2", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HUD_Mach_1", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HUD_SPD", isHudDisp);
//		super.mesh.chunkVisible("Z_Z_HUD_HIDE1", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HUD_LOWER_ROLL", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HUD_MISC", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HUD_FPM", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HUD_FPM_NO", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HUD_VS", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HUD_VSBG", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HUD_AOABG", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HUD_AOA", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HUD_BANK", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HUD_DST_0", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HUD_DST_1", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HUD_DST_2", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HUD_DST_3", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HUD_DIR", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HUD_DIR_BG", isHudDisp);
		super.mesh.chunkVisible("Z_Z_HUD_GS", isHudDisp);
		super.mesh.chunkVisible("Z_Z_RETICLECROSS", isHudDisp);
		if (!isHudDisp)
			return;
		super.mesh.chunkSetLocate("Z_Z_HUD_SPD", Cockpit.xyz, Cockpit.ypr);
		float pitchVal = setNew.pitch;
		if (pitchVal > 90F)
			for (; pitchVal > 90F; pitchVal -= 360F)
				;
		else if (pitchVal < -90F)
			for (; pitchVal < -90F; pitchVal += 360F)
				;
		pitchVal -= 90F;
		pitchVal = -pitchVal;
		int indexOffset = (int) pitchVal / 5;
		for (int j = indexOffset - 3; j <= indexOffset + 2; j++)
			if (j >= 0 && j < 37 && (((AV_8) aircraft()).k14Mode != 2 || (((AV_8) aircraft()).k14Mode == 2 && ((AV_8) aircraft()).lockmode == 1))) {
				super.mesh.chunkVisible(hudPitchRudderStr[j], true);
				super.mesh.chunkSetAngles(hudPitchRudderStr[j], setNew.bank, 0.0F, -setNew.pitch);
			}

		if (fm.CT.getGear() < 0.999999F) {
			super.mesh.chunkVisible("Z_Z_HUD_PITCHN3", false);
		} else {
			super.mesh.chunkSetAngles("Z_Z_HUD_PITCHN3", setNew.bank, 0.0F, -setNew.pitch);
			super.mesh.chunkVisible("Z_Z_HUD_PITCHN3", true);
		}
		Cockpit.xyz[0] = 0.0F;
		boolean fpmNoFlag = false;
		if (fm.Gears.nOfGearsOnGr < 1 || knot > 10F) {
			Cockpit.xyz[1] = -setNew.fpmYaw;
			if (Cockpit.xyz[1] < -6.5F) {
				Cockpit.xyz[1] = -6.5F;
				fpmNoFlag = true;
			} else if (Cockpit.xyz[1] > 6.5F) {
				Cockpit.xyz[1] = 6.5F;
				fpmNoFlag = true;
			}
			Cockpit.xyz[2] = -setNew.fpmPitch;
			if (Cockpit.xyz[2] < -11.5F) {
				Cockpit.xyz[2] = -11.5F;
				fpmNoFlag = true;
			} else if (Cockpit.xyz[2] > 7F) {
				Cockpit.xyz[2] = 7F;
				fpmNoFlag = true;
			}
		} else {
			Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;
		}
		super.mesh.chunkSetAngles("Z_Z_HUD_FPM", 0.0F, Cockpit.xyz[1], Cockpit.xyz[2]);
		super.mesh.chunkVisible("Z_Z_HUD_FPM_NO", fpmNoFlag);
		float val = normalizeDegree(setNew.bank);
		super.mesh.chunkSetAngles("Z_Z_HUD_BANK", val, 0.0F, 0.0F);
		if (val > 90F && val < 270F)
			super.mesh.chunkVisible("Z_Z_HUD_LOWER_ROLL", true);
		else
			super.mesh.chunkVisible("Z_Z_HUD_LOWER_ROLL", false);
		float alt = setNew.altimeter;
		val = cvt(alt, 0.0F, 30480F, 0.0F, 3600000F);
		val = 0.0F;
		super.mesh.chunkSetAngles("Z_Z_ALT_1", 0.0F, 0.0F, val);
		val = cvt(alt, 0.0F, 30480F, 0.0F, 360000F);
		val = (float) (int) (val / 36F) * 36F;
		super.mesh.chunkSetAngles("Z_Z_ALT_2", 0.0F, 0.0F, val);
		val = cvt(alt, 0.0F, 30480F, 0.0F, 36000F);
		val = (float) (int) (val / 36F) * 36F;
		super.mesh.chunkSetAngles("Z_Z_ALT_3", 0.0F, 0.0F, val);
		val = cvt(alt, 0.0F, 30480F, 0.0F, 3600F);
		val = (float) (int) (val / 36F) * 36F;
		super.mesh.chunkSetAngles("Z_Z_ALT_4", 0.0F, 0.0F, val);
		val = cvt(alt, 0.0F, 30480F, 0.0F, 1800F);
		if (val <= 360F) {
			super.mesh.chunkSetAngles("Z_Z_HUD_ALT_CY", 0.0F, 0.0F, val);
			super.mesh.chunkVisible("Z_Z_HUD_ALT_CY", true);
		} else {
			super.mesh.chunkVisible("Z_Z_HUD_ALT_CY", false);
		}
		val = cvt(alt, 0.0F, 30480F, 0.0F, 360F);
		val = (float) (int) (val / 36F) * 36F;
		super.mesh.chunkSetAngles("Z_Z_ALT_5", 0.0F, 0.0F, val);
		float gLoad = super.fm.getOverload();
		val = (float) (int) Math.abs(gLoad) * 36F;
		super.mesh.chunkSetAngles("Z_Z_G_1", 0.0F, 0.0F, val);
		val = (float) ((int) (Math.abs(gLoad) * 10F) % 10) * 36F;
		super.mesh.chunkSetAngles("Z_Z_G_2", 0.0F, 0.0F, val);
		float hdg = normalizeDegree(setNew.azimuth.getDeg(f) + 90F);
		super.mesh.chunkSetAngles("Z_Z_HUD_HDG", 0.0F, hdg, 0.0F);
		hdg += 0.5F;
		if (hdg > 360F)
			hdg = 0.0F;
		val = (float) (int) (hdg / 100F) * 36F;
		super.mesh.chunkSetAngles("Z_Z_HDG_1", 0.0F, 0.0F, val);
		val = (float) (((int) hdg % 100) / 10) * 36F;
		super.mesh.chunkSetAngles("Z_Z_HDG_2", 0.0F, 0.0F, val);
		val = (float) ((int) hdg % 10) * 36F;
		super.mesh.chunkSetAngles("Z_Z_HDG_3", 0.0F, 0.0F, val);
		val = super.fm.getAOA();
		isBlink = 0;
		if (val < 0.0F) {
			val = 0.0F;
			isBlink = 1;
		} else if (val > 20F) {
			val = 20F;
			isBlink = 1;
		}
		Cockpit.xyz[0] = 0.0F;
		Cockpit.xyz[1] = val * 0.01F;
		Cockpit.xyz[2] = 0.0F;
		super.mesh.chunkSetLocate("Z_Z_HUD_AOA", Cockpit.xyz, Cockpit.ypr);
		if (isBlink == 1 && blinkCounter % 10 < 5)
			super.mesh.chunkVisible("Z_Z_HUD_AOA", false);
		else
			super.mesh.chunkVisible("Z_Z_HUD_AOA", true);
		val = setNew.vspeed2 * 3.48F;
		val *= 60F;
		isBlink = 0;
		if (val > 2000F) {
			val = 2000F;
			isBlink = 1;
		}
		if (val < -2000F) {
			val = -2000F;
			isBlink = 1;
		}
		val /= 1000F;
		Cockpit.xyz[0] = 0.0F;
		Cockpit.xyz[1] = val * 0.05F;
		Cockpit.xyz[2] = 0.0F;
		super.mesh.chunkSetLocate("Z_Z_HUD_VS", Cockpit.xyz, Cockpit.ypr);
		if (isBlink == 1 && blinkCounter % 10 < 5)
			super.mesh.chunkVisible("Z_Z_HUD_VS", false);
		else
			super.mesh.chunkVisible("Z_Z_HUD_VS", true);
		float mach = calculateMach();
		val = (float) (int) mach * 36F;
		super.mesh.chunkSetAngles("Z_Z_HUD_Mach_1", 0.0F, 0.0F, val);
		val = (float) ((int) (mach * 10F) % 10) * 36F;
		super.mesh.chunkSetAngles("Z_Z_HUD_Mach_2", 0.0F, 0.0F, val);
		val = (float) ((int) (mach * 100F) % 10) * 36F;
		super.mesh.chunkSetAngles("Z_Z_HUD_Mach_3", 0.0F, 0.0F, val);
		if (fm.AS.listenLorenzBlindLanding || fm.AS.listenRadioStation || fm.AS.listenNDBeacon) {
			super.mesh.chunkVisible("Z_Z_HUD_DST_0", true);
			super.mesh.chunkVisible("Z_Z_HUD_DST_1", true);
			super.mesh.chunkVisible("Z_Z_HUD_DST_2", true);
			super.mesh.chunkVisible("Z_Z_HUD_DST_3", true);
			super.mesh.chunkVisible("Z_Z_HUD_DIR", true);
			super.mesh.chunkVisible("Z_Z_HUD_DIR_BG", true);
			float dst;
			if (fm.AS.listenLorenzBlindLanding) {
				dst = getNDBDist();
				super.mesh.chunkVisible("Z_Z_HUD_GS", true);
				val = setNew.ilsGS;
				Cockpit.xyz[0] = 0.0F;
				Cockpit.xyz[1] = val * 0.05F;
				Cockpit.xyz[2] = 0.0F;
				if (Cockpit.xyz[1] < -0.1F)
					Cockpit.xyz[1] = -0.1F;
				else if (Cockpit.xyz[1] > 0.1F)
					Cockpit.xyz[1] = 0.1F;
				super.mesh.chunkSetLocate("Z_Z_HUD_GS", Cockpit.xyz, Cockpit.ypr);
				val = setNew.ilsLoc;
				Cockpit.xyz[0] = -val * 0.005F;
			} else {
				dst = getNDBDist();
				super.mesh.chunkVisible("Z_Z_HUD_GS", false);
				val = setNew.navDiviation0;
				Cockpit.xyz[0] = -val * 0.01F;
			}
			Cockpit.xyz[1] = 0.0F;
			Cockpit.xyz[2] = 0.0F;
			if (Cockpit.xyz[0] < -0.1F)
				Cockpit.xyz[0] = -0.1F;
			else if (Cockpit.xyz[0] > 0.1F)
				Cockpit.xyz[0] = 0.1F;
			val = ((float) (int) dst / 100F) * 36F;
			super.mesh.chunkSetAngles("Z_Z_HUD_DST_3", 0.0F, 0.0F, val);
			val = (float) (((int) dst % 100) / 10) * 36F;
			super.mesh.chunkSetAngles("Z_Z_HUD_DST_2", 0.0F, 0.0F, val);
			val = (float) (int) (dst % 10F) * 36F;
			super.mesh.chunkSetAngles("Z_Z_HUD_DST_1", 0.0F, 0.0F, val);
			val = ((dst * 10F) % 10F) * 36F;
			super.mesh.chunkSetAngles("Z_Z_HUD_DST_0", 0.0F, 0.0F, val);
			super.mesh.chunkSetLocate("Z_Z_HUD_DIR", Cockpit.xyz, Cockpit.ypr);
		} else {
			super.mesh.chunkVisible("Z_Z_HUD_DST_0", false);
			super.mesh.chunkVisible("Z_Z_HUD_DST_1", false);
			super.mesh.chunkVisible("Z_Z_HUD_DST_2", false);
			super.mesh.chunkVisible("Z_Z_HUD_DST_3", false);
			super.mesh.chunkVisible("Z_Z_HUD_DIR", false);
			super.mesh.chunkVisible("Z_Z_HUD_DIR_BG", false);
			super.mesh.chunkVisible("Z_Z_HUD_GS", false);
		}
	}

	protected void drawSound(float f) {
		if (aoaWarnFX != null)
			if (setNew.fpmPitch >= 9.7F && setNew.fpmPitch < 12F && fm.Gears.nOfGearsOnGr < 1) {
				if (!aoaWarnFX.isPlaying())
					aoaWarnFX.play();
			} else {
				aoaWarnFX.cancel();
			}
		if (aoaWarnFX2 != null)
			if (setNew.fpmPitch >= 12F && setNew.fpmPitch < 14F && fm.Gears.nOfGearsOnGr < 1) {
				if (!aoaWarnFX2.isPlaying())
					aoaWarnFX2.play();
			} else {
				aoaWarnFX2.cancel();
			}
		if (aoaWarnFX3 != null)
			if (setNew.fpmPitch >= 14F && setNew.fpmPitch < 15.5F && fm.Gears.nOfGearsOnGr < 1) {
				if (!aoaWarnFX3.isPlaying())
					aoaWarnFX3.play();
			} else {
				aoaWarnFX3.cancel();
			}
		if (aoaWarnFX4 != null)
			if (setNew.fpmPitch >= 15.4F && fm.Gears.nOfGearsOnGr < 1) {
				if (!aoaWarnFX4.isPlaying())
					aoaWarnFX4.play();
			} else {
				aoaWarnFX4.cancel();
			}
	}

	public float normalizeDegree(float val) {
		if (val < 0.0F)
			do
				val += 360F;
			while (val < 0.0F);
		else if (val > 360F)
			do
				val -= 360F;
			while (val >= 360F);
		return val;
	}

	public float normalizeDegree180(float val) {
		if (val < -180F)
			do
				val += 360F;
			while (val < -180F);
		else if (val > 180F)
			do
				val -= 360F;
			while (val > 180F);
		return val;
	}

	public float getMachForAlt(float theAltValue) {
		theAltValue /= 1000F;
		int i = 0;
		for (i = 0; i < TypeSupersonic.fMachAltX.length; i++)
			if (TypeSupersonic.fMachAltX[i] > theAltValue)
				break;

		if (i == 0) {
			return TypeSupersonic.fMachAltY[0];
		} else {
			float baseMach = TypeSupersonic.fMachAltY[i - 1];
			float spanMach = TypeSupersonic.fMachAltY[i] - baseMach;
			float baseAlt = TypeSupersonic.fMachAltX[i - 1];
			float spanAlt = TypeSupersonic.fMachAltX[i] - baseAlt;
			float spanMult = (theAltValue - baseAlt) / spanAlt;
			return baseMach + spanMach * spanMult;
		}
	}

	public float calculateMach() {
		return super.fm.getSpeedKMH() / getMachForAlt(super.fm.getAltitude());
	}

	public void reflectCockpitState() {
	}
	
	void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
    }

    protected void reflectPlaneToModel()
    {
//        HierMesh hiermesh = aircraft().hierMesh();
//        mesh.chunkVisible("Nose_D0", hiermesh.isChunkVisible("Nose_D0"));
//    	mesh.chunkVisible("Refillrod1", hiermesh.isChunkVisible("Refillrod1"));
//    	mesh.chunkVisible("Refillrod2", hiermesh.isChunkVisible("Refillrod2"));
    }

	public void toggleDim() {
		isDimmer = !isDimmer;
	}

	public void toggleLight() {
		super.cockpitLightControl = !super.cockpitLightControl;
		if (super.cockpitLightControl) {
			super.mesh.chunkVisible("Z_Z_NVision", true);
		} else {
			super.mesh.chunkVisible("Z_Z_NVision", false);
		}
	}

	protected float getNDBDist() {
		int i = fm.AS.getBeacon();
		if (i == 0) {
			return 0.0F;
		} else {
			ArrayList arraylist = Main.cur().mission.getBeacons(((Interpolate) (super.fm)).actor.getArmy());
			Actor actor = (Actor) arraylist.get(i - 1);
			tmpV.sub(actor.pos.getAbsPoint(), fm.Loc);
			return (float) (tmpV.length() * 0.0010000000474974513D) / 1.853F;
		}
	}

	protected float getNDBDirection() {
		int i = fm.AS.getBeacon();
		if (i == 0) {
			return 0.0F;
		} else {
			ArrayList arraylist = Main.cur().mission.getBeacons(((Interpolate) (super.fm)).actor.getArmy());
			Actor actor = (Actor) arraylist.get(i - 1);
			tmpV.x = ((Tuple3d) (actor.pos.getAbsPoint())).x;
			tmpV.y = ((Tuple3d) (actor.pos.getAbsPoint())).y;
			tmpV.z = ((Tuple3d) (actor.pos.getAbsPoint())).z + 20D;
			((Actor) (aircraft())).pos.getAbs(tmpP);
			tmpP.sub(tmpV);
			float f1 = 57.32484F * (float) Math.atan2(-((Tuple3d) (tmpP)).y, -((Tuple3d) (tmpP)).x);
			return 360F - f1;
		}
	}

	public boolean useRealisticNavigationInstruments() {
		return World.cur().diffCur.RealisticNavigationInstruments;
	}

	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	public Vector3f w;
	private Point3d tmpP;
	private Vector3d tmpV;
	private Matrix3d tmpMat;
	private boolean bNeedSetUp;
	private boolean isDimmer;
	private int blinkCounter;
	private SoundFX aoaWarnFX;
	private SoundFX aoaWarnFX2;
	private SoundFX aoaWarnFX3;
	private SoundFX aoaWarnFX4;
	private String hudPitchRudderStr[];
	private float alpha;
	private static final float rpmScale[] = { 0.0F, 190F, 220F, 300F };
	private ClipBoard clipBoard;
	private static final float k14TargetMarkScale[] = { 0.0F, -4.5F, -27.5F, -42.5F, -56.5F, -61.5F, -70F, -95F, -102.5F, -106F };
	private static final float k14TargetWingspanScale[] = { 9.9F, 10.52F, 13.8F, 16.34F, 19F, 20F, 22F, 29.25F, 30F, 32.85F };
	private static final float k14BulletDrop[] = { 5.812F, 6.168F, 6.508F, 6.978F, 7.24F, 7.576F, 7.849F, 8.108F, 8.473F, 8.699F, 8.911F, 9.111F, 9.384F, 9.554F, 9.787F, 9.928F, 9.992F, 10.282F, 10.381F, 10.513F, 10.603F, 10.704F, 10.739F, 10.782F,
			10.789F };

}