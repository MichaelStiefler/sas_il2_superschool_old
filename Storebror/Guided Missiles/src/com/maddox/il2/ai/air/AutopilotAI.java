/*Modified AutopilotAI class for the SAS Engine Mod*/
package com.maddox.il2.ai.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Airport;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.fm.Autopilotage;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.FMMath;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.il2.objects.sounds.Voice;

public class AutopilotAI extends Autopilotage {
	private static Orientation O = new Orientation();
	private static Point3d P = new Point3d();
	private static Point3d PlLoc = new Point3d();
	private float Ail;
	private float avoidance;
	public boolean bStabAltitude;
	public boolean bStabDirection;
	public boolean bStabSpeed;
	public boolean bWayPoint;
	protected Vector3d courseV;
	private float Ev;
	protected Pilot FM;
	// TODO:
	private boolean overrideMissileControl = false;
	private float Pw;
	private float SA;
	protected double StabAltitude;
	protected double StabDirection;
	protected double StabSpeed;
	private Controls theMissileControls = null;
	private Vector3d Ve;
	protected Vector3d windV;
	protected Point3d WPoint;
	protected WayPoint WWPoint;

	public AutopilotAI(FlightModel flightmodel) {
		this.WPoint = new Point3d();
		this.courseV = new Vector3d();
		this.windV = new Vector3d();
		this.Ve = new Vector3d();
		this.FM = (Pilot) flightmodel;
	}

	public boolean getStabAltitude() {
		return this.bStabAltitude;
	}

	public boolean getStabDirection() {
		return this.bStabDirection;
	}

	public boolean getStabSpeed() {
		return this.bStabSpeed;
	}

	public boolean getWayPoint() {
		return this.bWayPoint;
	}

	public float getWayPointDistance() {
		if (this.WPoint == null) return 1000000F;
		else {
			this.way.curr().getP(P);
			P.sub(this.FM.Loc);
			return (float) Math.sqrt(P.x * P.x + P.y * P.y);
		}
	}

	private void landUpdate(float f) {
		if (((this.FM.getAltitude() - 10F) + this.FM.getVertSpeed() * 5F) - this.SA > 0.0F) {
			if (this.FM.Vwld.z > -10D) {
				this.FM.Vwld.z -= 1.0F * f;
			}
		} else if (this.FM.Vwld.z < 10D) {
			this.FM.Vwld.z += 1.0F * f;
		}
		if (this.FM.getAOA() > 11F && this.FM.CT.ElevatorControl > -0.3F) {
			this.FM.CT.ElevatorControl -= 0.3F * f;
		}
	}

	// TODO:
	public void setOverrideMissileControl(Controls theControls, boolean overrideMissile) {
		this.theMissileControls = theControls;
		this.overrideMissileControl = overrideMissile;
	}

	public void setStabAll(boolean flag) {
		this.bWayPoint = false;
		this.setStabDirection(flag);
		this.setStabAltitude(flag);
		this.setStabSpeed(flag);
		this.setStabDirection(flag);
	}

	public void setStabAltitude(boolean flag) {
		this.bStabAltitude = flag;
		if (!flag) return;
		this.bWayPoint = false;
		this.FM.getLoc(P);
		this.StabAltitude = P.z;
		if (!this.bStabSpeed) {
			this.StabSpeed = this.FM.getSpeed();
		}
		this.Pw = this.FM.CT.PowerControl;
	}

	public void setStabAltitude(float f) {
		this.bStabAltitude = true;
		this.bWayPoint = false;
		this.FM.getLoc(P);
		this.StabAltitude = f;
		if (!this.bStabSpeed) {
			this.StabSpeed = this.FM.getSpeed();
		}
		this.Pw = this.FM.CT.PowerControl;
	}

	public void setStabDirection(boolean flag) {
		this.bStabDirection = flag;
		if (!flag) return;
		else {
			this.bWayPoint = false;
			O.set(this.FM.Or);
			this.StabDirection = O.getAzimut();
			this.Ail = this.FM.CT.AileronControl;
			return;
		}
	}

	public void setStabDirection(float f) {
		this.bStabDirection = true;
		this.bWayPoint = false;
		this.StabDirection = (f + 3600F) % 360F;
		this.Ail = this.FM.CT.AileronControl;
	}

	public void setStabSpeed(boolean flag) {
		this.bStabSpeed = flag;
		if (!flag) return;
		else {
			this.bWayPoint = false;
			this.StabSpeed = this.FM.getSpeed();
			return;
		}
	}

	public void setStabSpeed(float f) {
		this.bStabSpeed = true;
		this.bWayPoint = false;
		this.StabSpeed = f / 3.6F;
	}

	public void setWayPoint(boolean flag) {
		this.bWayPoint = flag;
		if (!flag) return;
		this.bStabSpeed = false;
		this.bStabAltitude = false;
		this.bStabDirection = false;
		if (this.WWPoint != null) {
			this.WWPoint.getP(this.WPoint);
			this.StabSpeed = this.WWPoint.Speed;
			this.StabAltitude = this.WPoint.z;
		} else {
			this.StabAltitude = 1000D;
			this.StabSpeed = 80D;
		}
		this.StabDirection = O.getAzimut();
	}

	public void update(float f) {
		// TODO: Code for missile override
		if (this.overrideMissileControl) {
			this.theMissileControls.WeaponControl[2] = true;
		}
		this.FM.getLoc(PlLoc);
		this.SA = (float) Math.max(this.StabAltitude, Engine.land().HQ_Air(PlLoc.x, PlLoc.y) + 5D);
		if (((Maneuver) (this.FM)).Group != null && ((Maneuver) (this.FM)).Group.getAaaNum() > 3F && ((Aircraft) this.FM.actor).aircIndex() == 0 && this.FM.isTick(165, 0)) {
			this.avoidance = (1 - World.Rnd().nextInt(0, 1) * 2) * World.Rnd().nextFloat(15F, 30F);
		}
		if (this.bWayPoint) {
			if (this.WWPoint != this.way.auto(PlLoc) || this.way.isReached(PlLoc)) {
				this.WWPoint = this.way.auto(PlLoc);
				this.WWPoint.getP(this.WPoint);
				if (((Aircraft) this.FM.actor).aircIndex() == 0 && !this.way.isLanding()) {
					this.voiceCommand(this.WPoint, PlLoc);
					this.FM.formationType = (byte) this.way.curr().formation;
					if (((Maneuver) (this.FM)).Group != null) {
						((Maneuver) (this.FM)).Group.setFormationAndScale(this.FM.formationType, 1.0F, true);
						((Maneuver) (this.FM)).Group.formationType = this.FM.formationType;
					}
				}
				this.StabSpeed = this.WWPoint.Speed - 2.0F * ((Aircraft) this.FM.actor).aircIndex();
				this.StabAltitude = this.WPoint.z;
				if (this.WWPoint.Action == 3) {
					Actor actor = this.WWPoint.getTarget();
					if (actor != null) {
						this.FM.target_ground = null;
					} else if (((Aircraft) this.FM.actor instanceof TypeBomber) && this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][0] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].haveBullets()) {
						this.FM.CT.BayDoorControl = 1.0F;
						for (Pilot pilot1 = this.FM; pilot1.Wingman != null;) {
							pilot1 = (Pilot) pilot1.Wingman;
							pilot1.CT.BayDoorControl = 1.0F;
						}

					}
				} else {
					if (((Aircraft) this.FM.actor instanceof TypeBomber) || this.FM.CT.bHasBayDoorControl) {
						this.FM.CT.BayDoorControl = 0.0F;
						for (Pilot pilot = this.FM; pilot.Wingman != null;) {
							pilot = (Pilot) pilot.Wingman;
							pilot.CT.BayDoorControl = 0.0F;
						}

					}
					Actor actor1 = this.WWPoint.getTarget();
					if (actor1 instanceof Aircraft) if (actor1.getArmy() == this.FM.actor.getArmy()) {
						this.FM.airClient = ((Aircraft) actor1).FM;
					} else {
						this.FM.target = ((Aircraft) actor1).FM;
					}
				}
				if (this.way.isLanding()) {
					this.FM.getLoc(P);
					if (this.way.Cur() > 3 && P.z > this.WPoint.z + 500D) {
						this.way.setCur(1);
					}
					if (this.way.Cur() == 5) {
						if (!Mission.isDogfight() || !Main.cur().mission.zutiMisc_DisableAIRadioChatter) {
							Voice.speakLanding((Aircraft) this.FM.actor);
						}
					}
					if (this.way.Cur() == 6 || this.way.Cur() == 7) {
						int i = 0;
						if (Actor.isAlive(this.way.landingAirport)) {
							i = this.way.landingAirport.landingFeedback(this.WPoint, (Aircraft) this.FM.actor);
						}
						if (i == 0) {
							if (!Mission.isDogfight() || !Main.cur().mission.zutiMisc_DisableAIRadioChatter) {
								Voice.speakLandingPermited((Aircraft) this.FM.actor);
							}
						}
						if (i == 1) {
							if (!Mission.isDogfight() || !Main.cur().mission.zutiMisc_DisableAIRadioChatter) {
								Voice.speakLandingDenied((Aircraft) this.FM.actor);
							}
							this.way.first();
							this.FM.push(2);
							this.FM.push(2);
							this.FM.push(2);
							this.FM.push(2);
							this.FM.pop();
							if (!Mission.isDogfight() || !Main.cur().mission.zutiMisc_DisableAIRadioChatter) {
								Voice.speakGoAround((Aircraft) this.FM.actor);
							}
							this.FM.CT.FlapsControl = 0.4F;
							this.FM.CT.GearControl = 0.0F;
							return;
						}
						if (i == 2) {
							if (!Mission.isDogfight() || !Main.cur().mission.zutiMisc_DisableAIRadioChatter) {
								Voice.speakWaveOff((Aircraft) this.FM.actor);
							}
							if (this.FM.isReadyToReturn()) {
								if (!Mission.isDogfight() || !Main.cur().mission.zutiMisc_DisableAIRadioChatter) {
									Voice.speakGoingIn((Aircraft) this.FM.actor);
								}
								this.FM.AS.setCockpitDoor(this.FM.actor, 1);
								this.FM.CT.GearControl = 1.0F;
								return;
							} else {
								this.way.first();
								this.FM.push(2);
								this.FM.push(2);
								this.FM.push(2);
								this.FM.push(2);
								this.FM.pop();
								this.FM.CT.FlapsControl = 0.4F;
								this.FM.CT.GearControl = 0.0F;
								Aircraft.debugprintln(this.FM.actor, "Going around!.");
								return;
							}
						}
						this.FM.CT.GearControl = 1.0F;
					}
				}
			}
			if (this.way.isLanding() && this.way.Cur() < 6 && this.way.getCurDist() < 800D) {
				this.way.next();
			}
			if ((this.way.Cur() == this.way.size() - 1 && this.getWayPointDistance() < 2000F && this.way.curr().getTarget() == null && this.FM.M.fuel < 0.2F * this.FM.M.maxFuel || this.way.curr().Action == 2) && !this.way.isLanding()) {
				Airport airport = Airport.makeLandWay(this.FM);
				if (airport != null) {
					this.WWPoint = null;
					this.way.first();
					this.update(f);
					return;
				}
				this.FM.set_task(3);
				this.FM.set_maneuver(49);
				this.FM.setBusy(true);
			}
			boolean bStabDirectionSet = false;
			if (World.cur().diffCur.Wind_N_Turbulence) {
				World.cur();
				if (!World.wind().noWind && this.FM.Skill > 0) {
					World.cur();
					World.wind().getVectorAI(this.WPoint, this.windV);
					this.windV.scale(-1D);
					if (this.FM.Skill == 1) {
						this.windV.scale(0.75D);
					}
					this.courseV.set(this.WPoint.x - PlLoc.x, this.WPoint.y - PlLoc.y, 0.0D);
					this.courseV.normalize();
					this.courseV.scale(this.FM.getSpeed());
					this.courseV.add(this.windV);
					this.StabDirection = -FMMath.RAD2DEG((float) Math.atan2(this.courseV.y, this.courseV.x));
					bStabDirectionSet = true;
				}
			}
			if (!bStabDirectionSet) {
				this.StabDirection = -FMMath.RAD2DEG((float) Math.atan2(this.WPoint.y - PlLoc.y, this.WPoint.x - PlLoc.x));
			}
		}
		if (this.bStabSpeed || this.bWayPoint) {
			this.Pw = 0.3F - 0.04F * (this.FM.getSpeed() - (float) this.StabSpeed);
			if (this.Pw > 1.0F) {
				this.Pw = 1.0F;
			} else if (this.Pw < 0.0F) {
				this.Pw = 0.0F;
			}
		}
		if (this.bStabAltitude || this.bWayPoint) {
			this.Ev = this.FM.CT.ElevatorControl;
			double d = this.SA - this.FM.getAltitude();
			double d1 = 0.0D;
			double d2 = 0.0D;
			if (d > -50D) {
				float f4 = 5F + 0.00025F * this.FM.getAltitude();
				f4 = (float) (f4 + 0.02D * (250D - this.FM.Vmax));
				if (f4 > 14F) {
					f4 = 14F;
				}
				d1 = Math.min(this.FM.getAOA() - f4, this.FM.Or.getTangage() - 1.0F) * 1.0F * f + 0.5F * this.FM.getForwAccel();
			}
			if (d < 50D) {
				float f5 = -15F + this.FM.M.mass * 0.00033F;
				if (f5 < -4F) {
					f5 = -4F;
				}
				d2 = (this.FM.Or.getTangage() - f5) * 0.8F * f;
			}
			double d3 = 0.01D * (d + 50D);
			if (d3 > 1.0D) {
				d3 = 1.0D;
			}
			if (d3 < 0.0D) {
				d3 = 0.0D;
			}
			this.Ev -= d3 * d1 + (1.0D - d3) * d2;
			this.Ev += 1.0D * this.FM.getW().y + 0.5D * this.FM.getAW().y;
			if (this.FM.getSpeed() < 1.3F * this.FM.VminFLAPS) {
				this.Ev -= 0.004F * f;
			}
			float f6 = (9F * this.FM.getSpeed()) / this.FM.VminFLAPS;
			if (this.FM.VminFLAPS < 28F) {
				f6 = 10F;
			}
			if (f6 > 25F) {
				f6 = 25F;
			}
			float f7 = (f6 - this.FM.Or.getTangage()) * 0.1F;
			float f8 = -15F + this.FM.M.mass * 0.00033F;
			if (f8 < -4F) {
				f8 = -4F;
			}
			float f9 = (f8 - this.FM.Or.getTangage()) * 0.2F;
			if (this.Ev > f7) {
				this.Ev = f7;
			}
			if (this.Ev < f9) {
				this.Ev = f9;
			}
			this.FM.CT.ElevatorControl = 0.8F * this.FM.CT.ElevatorControl + 0.2F * this.Ev;
		}
		float f1 = 0.0F;
		if (this.bStabDirection || this.bWayPoint) {
			f1 = this.FM.Or.getAzimut();
			float f2 = this.FM.Or.getKren();
			if (((Maneuver) (this.FM)).Group.getAaaNum() > 3F && ((Aircraft) this.FM.actor).aircIndex() == 0) {
				f1 = (float) (f1 - (this.StabDirection + this.avoidance));
			} else {
				f1 = (float) (f1 - this.StabDirection);
			}
			f1 = (f1 + 3600F) % 360F;
			f2 = (f2 + 3600F) % 360F;
			if (f1 > 180F) {
				f1 -= 360F;
			}
			if (f2 > 180F) {
				f2 -= 360F;
			}
			float f3 = ((this.FM.getSpeed() - this.FM.VminFLAPS) * 3.6F + this.FM.getVertSpeed() * 40F) * 0.25F;
			if (this.way.isLanding()) {
				f3 = 65F;
			}
			if (f3 < 15F) {
				f3 = 15F;
			} else if (f3 > 65F) {
				f3 = 65F;
			}
			if (f1 < -f3) {
				f1 = -f3;
			} else if (f1 > f3) {
				f1 = f3;
			}
			this.Ail = -0.01F * (f1 + f2 + 3F * (float) this.FM.getW().x + 0.5F * (float) this.FM.getAW().x);
			if (this.Ail > 1.0F) {
				this.Ail = 1.0F;
			} else if (this.Ail < -1F) {
				this.Ail = -1F;
			}
			this.WPoint.get(this.Ve);
			this.Ve.sub(this.FM.Loc);
			this.FM.Or.transformInv(this.Ve);
			if (Math.abs(this.Ve.y) < 25D && Math.abs(this.Ve.x) < 150D) {
				this.FM.CT.AileronControl = -0.01F * this.FM.Or.getKren();
			} else {
				this.FM.CT.AileronControl = this.Ail;
			}
			this.FM.CT.ElevatorControl += Math.abs(f2) * 0.004F * f;
			this.FM.CT.RudderControl -= this.FM.getAOS() * 0.04F * f;
		}
		if (this.bWayPoint && this.way.isLanding()) {
			if (World.Rnd().nextFloat() < 0.01F) {
				this.FM.doDumpBombsPassively();
			}
			if (this.way.Cur() > 5) {
				this.FM.set_maneuver(25);
			}
			this.FM.CT.RudderControl -= f1 * 0.04F * f;
			this.landUpdate(f);
		}
	}

	private void voiceCommand(Point3d point3d, Point3d point3d1) {
		this.Ve.sub(point3d1, point3d);
		float f = 57.32484F * (float) Math.atan2(this.Ve.x, this.Ve.y);
		int i = (int) f;
		i = (i + 180) % 360;
		Voice.speakHeading((Aircraft) this.FM.actor, i);
		Voice.speakAltitude((Aircraft) this.FM.actor, (int) point3d.z);
	}
}