/*4.10.1 class*/
package com.maddox.il2.engine.cmd;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.Orient;
import com.maddox.util.NumberTokenizer;
import com.maddox.util.QuoteTokenizer;

public class CmdActorSpawn extends com.maddox.rts.Cmd
{
	public static final java.lang.String EMPTY = "";
	public static final java.lang.String NAME = "NAME";
	public static final java.lang.String OVR = "OVR";
	public static final java.lang.String ARMY = "ARMY";
	public static final java.lang.String POSP = "POSP";
	public static final java.lang.String POSO = "POSO";
	public static final java.lang.String BASED = "BASED";
	public static final java.lang.String BASE = "BASE";
	public static final java.lang.String HOOK = "HOOK";
	public static final java.lang.String OWNER = "OWNER";
	public static final java.lang.String ICON = "ICON";
	public static final java.lang.String MESH = "MESH";
	public static final java.lang.String MAT = "MAT";
	public static final java.lang.String PARAMFILE = "PARAMFILE";
	public static final java.lang.String SIZE = "SIZE";
	public static final java.lang.String TIMELEN = "TIMELEN";
	public static final java.lang.String TIMENATIVE = "TIMENATIVE";
	public static final java.lang.String TYPE = "TYPE";
	public static final java.lang.String PATH = "PATH";
	public static final java.lang.String TARGET = "TARGET";
	public static final java.lang.String ACOUSTIC = "ACOUSTIC";
	public static final java.lang.String SOUND = "SOUND";
	public static final java.lang.String PRELOAD = "PRELOAD";
	public static final java.lang.String COLOR = "COLOR";
	public static final java.lang.String LIGHT = "LIGHT";
	public static final java.lang.String Z0 = "Z0";
	public static final java.lang.String FM = "FM";
	public static final java.lang.String FM_Type = "FM_Type";
	public static final java.lang.String WEAPONS = "WEAPONS";
	public static final java.lang.String FUEL = "FUEL";
	public static final java.lang.String SPEED = "SPEED";
	public static final java.lang.String SKILL = "SKILL";
	public static final java.lang.String PLAYER = "PLAYER";
	public static final java.lang.String BORNPLACE = "BORNPLACE";
	public static final java.lang.String STAYPLACE = "STAYPLACE";
	public static final java.lang.String NUMBEROFF = "NUMBEROFF";
	public static final java.lang.String RAWDATA = "RAWDATA";
	
	//TODO: Added by |ZUTI|
	//-----------------------------------------------------------------
	public static final java.lang.String Z_AIRSTART = "Z_AIRSTART";
	public static final java.lang.String Z_MULTICREW = "Z_MULTICREW";
	public static final java.lang.String Z_MULTICREW_ANYTIME = "Z_MULTICREW_ANYTIME";
	//-----------------------------------------------------------------
	
	private boolean nameExist;
	private boolean ovrExist;
	private com.maddox.il2.engine.Actor basedActor;
	private com.maddox.util.QuoteTokenizer tokens;
	private java.lang.String word;
	private com.maddox.il2.engine.ActorSpawnArg sarg;
	private com.maddox.JGP.Point3d P;
	private com.maddox.il2.engine.Orient O;
	private float light[];
	private com.maddox.JGP.Color3f color3f;
	private com.maddox.JGP.Vector3d speed3d;
	private boolean bExit;
	
	class Token
	{
		public void parse()
		{}
		
		public java.lang.String getStr()
		{
			if (!tokens.hasMoreTokens())
			{
				word = null;
				return "";
			}
			java.lang.StringBuffer stringbuffer = new StringBuffer();
			int i = 0;
			word = null;
			java.lang.String s;
			for (; tokens.hasMoreTokens(); stringbuffer.append(s))
			{
				s = tokens.nextToken();
				if (paramContainsKey(s))
				{
					word = s;
					break;
				}
				if (i++ > 0)
					stringbuffer.append(' ');
			}
			
			if (stringbuffer.length() > 0)
				return stringbuffer.toString();
			else
				return "";
		}
		
		Token()
		{}
	}
	
	public boolean isRawFormat()
	{
		return true;
	}
	
	protected boolean paramContainsKey(java.lang.String s)
	{
		return param.containsKey(s);
	}
	
	protected void ERR_HARD(java.lang.String s)
	{
		super.ERR_HARD(s);
	}
	
	public java.lang.Object exec(com.maddox.rts.CmdEnv cmdenv, java.lang.String s)
	{
		com.maddox.il2.engine.ActorSpawn actorspawn = null;
		nameExist = false;
		ovrExist = false;
		basedActor = null;
		bExit = false;
		sarg.clear();
		tokens = new QuoteTokenizer(s);
		for (word = null; tokens.hasMoreTokens() || word != null;)
		{
			if (word == null)
				word = tokens.nextToken();
			Token token = (Token)param.get(word);
			if (token != null)
			{
				if (actorspawn == null)
				{
					ERR_HARD("class of actor NOT present");
					basedActor = null;
					return null;
				}
				token.parse();
				if (bExit)
				{
					basedActor = null;
					return null;
				}
			}
			else
			{
				java.lang.Object obj = com.maddox.rts.Spawn.get_WithSoftClass(word, false);
				if (obj == null)
					obj = com.maddox.rts.Spawn.get_WithSoftClass("com.maddox.il2." + word, false);
				if (obj == null)
					obj = com.maddox.rts.Spawn.get_WithSoftClass("com.maddox.il2.objects." + word, false);
				if (actorspawn == null)
				{
					if (obj == null)
					{
						ERR_HARD("class " + word + " NOT found or NOT registered in Spawn database");
						basedActor = null;
						return null;
					}
					if (!(obj instanceof com.maddox.il2.engine.ActorSpawn))
					{
						ERR_HARD("class " + word + " NOT contains ActorSpawn interface");
						basedActor = null;
						return null;
					}
					actorspawn = (com.maddox.il2.engine.ActorSpawn)obj;
				}
				word = null;
			}
		}
		
		if (nameExist)
		{
			com.maddox.il2.engine.Actor actor = com.maddox.il2.engine.Actor.getByName(sarg.name);
			if (actor != null)
				if (ovrExist)
				{
					actor.destroy();
				}
				else
				{
					ERR_HARD("actor: " + sarg.name + " alredy exist");
					basedActor = null;
					return null;
				}
		}
		if (basedActor != null)
		{
			com.maddox.JGP.Point3d point3d;
			com.maddox.il2.engine.Orient orient;
			if (sarg.baseActor != null)
			{
				point3d = basedActor.pos.getRelPoint();
				orient = basedActor.pos.getRelOrient();
			}
			else
			{
				point3d = basedActor.pos.getAbsPoint();
				orient = basedActor.pos.getAbsOrient();
			}
			if (sarg.point != null)
			{
				sarg.point.add(point3d);
			}
			else
			{
				P.set(point3d);
				sarg.point = P;
			}
			if (sarg.orient != null)
			{
				sarg.orient.add(orient);
			}
			else
			{
				O.set(orient);
				sarg.orient = O;
			}
		}
		basedActor = null;
		com.maddox.il2.engine.Actor actor1 = actorspawn.actorSpawn(sarg);
		if (actor1 != null && com.maddox.il2.engine.Config.isAppEditor())
		{
			com.maddox.rts.Property.set(actor1, "spawn", s);
			com.maddox.rts.Property.set(actor1, "spawn arg", new ActorSpawnArg(sarg));
		}
		return actor1;
	}
	
	public CmdActorSpawn()
	{
		sarg = new ActorSpawnArg();
		P = new Point3d();
		O = new Orient();
		light = new float[2];
		color3f = new Color3f();
		speed3d = new Vector3d();
		param.put("NAME", new Token()
		{
			
			public void parse()
			{
				sarg.name = getStr();
				nameExist = true;
			}
			
		});
		param.put("OVR", new Token()
		{
			
			public void parse()
			{
				getStr();
				ovrExist = true;
			}
			
		});
		param.put("ARMY", new Token()
		{
			
			public void parse()
			{
				sarg.armyExist = true;
				java.lang.String s = getStr();
				try
				{
					sarg.army = java.lang.Integer.parseInt(s);
				}
				catch (java.lang.Exception exception)
				{
					ERR_HARD("bad format army: " + s);
					bExit = true;
				}
			}
			
		});
		param.put("POSP", new Token()
		{
			
			public void parse()
			{
				java.lang.String s = getStr();
				sarg.point = P;
				sarg.point.set(0.0D, 0.0D, 0.0D);
				if (s != "")
				{
					com.maddox.util.NumberTokenizer numbertokenizer = new NumberTokenizer(s, " ");
					try
					{
						if (numbertokenizer.hasMoreTokens())
							sarg.point.x = numbertokenizer.nextDouble();
						if (numbertokenizer.hasMoreTokens())
							sarg.point.y = numbertokenizer.nextDouble();
						if (numbertokenizer.hasMoreTokens())
							sarg.point.z = numbertokenizer.nextDouble();
					}
					catch (java.lang.Exception exception)
					{
						ERR_HARD("bad format position: " + s);
						bExit = true;
					}
				}
			}
			
		});
		param.put("POSO", new Token()
		{
			
			public void parse()
			{
				java.lang.String s = getStr();
				sarg.orient = O;
				sarg.orient.set(0.0F, 0.0F, 0.0F);
				if (s != "")
				{
					com.maddox.util.NumberTokenizer numbertokenizer = new NumberTokenizer(s, " ");
					try
					{
						float f = 0.0F;
						float f1 = 0.0F;
						float f2 = 0.0F;
						if (numbertokenizer.hasMoreTokens())
							f = numbertokenizer.nextFloat();
						if (numbertokenizer.hasMoreTokens())
							f1 = numbertokenizer.nextFloat();
						if (numbertokenizer.hasMoreTokens())
							f2 = numbertokenizer.nextFloat();
						sarg.orient.set(f, f1, f2);
					}
					catch (java.lang.Exception exception)
					{
						ERR_HARD("bad format orientation: " + s);
						bExit = true;
					}
				}
			}
			
		});
		param.put("BASED", new Token()
		{
			
			public void parse()
			{
				java.lang.String s = getStr();
				basedActor = com.maddox.il2.engine.Actor.getByName(s);
				if (basedActor == null)
				{
					ERR_HARD("based actor: " + s + " not found");
					bExit = true;
				}
			}
			
		});
		param.put("BASE", new Token()
		{
			
			public void parse()
			{
				java.lang.String s = getStr();
				sarg.baseActor = com.maddox.il2.engine.Actor.getByName(s);
				if (sarg.baseActor == null)
				{
					ERR_HARD("base actor: " + s + " not found");
					bExit = true;
				}
			}
			
		});
		param.put("HOOK", new Token()
		{
			
			public void parse()
			{
				sarg.hookName = getStr();
			}
			
		});
		param.put("OWNER", new Token()
		{
			
			public void parse()
			{
				java.lang.String s = getStr();
				sarg.ownerActor = com.maddox.il2.engine.Actor.getByName(s);
				if (sarg.ownerActor == null)
				{
					ERR_HARD("owner actor: " + s + " not found");
					bExit = true;
				}
			}
			
		});
		param.put("ICON", new Token()
		{
			
			public void parse()
			{
				sarg.iconName = getStr();
			}
			
		});
		param.put("MESH", new Token()
		{
			
			public void parse()
			{
				sarg.meshName = getStr();
			}
			
		});
		param.put("MAT", new Token()
		{
			
			public void parse()
			{
				sarg.matName = getStr();
			}
			
		});
		param.put("PARAMFILE", new Token()
		{
			
			public void parse()
			{
				sarg.paramFileName = getStr();
			}
			
		});
		param.put("SIZE", new Token()
		{
			
			public void parse()
			{
				sarg.sizeExist = true;
				java.lang.String s = getStr();
				try
				{
					sarg.size = java.lang.Float.parseFloat(s);
				}
				catch (java.lang.Exception exception)
				{
					ERR_HARD("bad format size: " + s);
					bExit = true;
				}
			}
			
		});
		param.put("TIMELEN", new Token()
		{
			
			public void parse()
			{
				sarg.timeLenExist = true;
				java.lang.String s = getStr();
				try
				{
					sarg.timeLen = java.lang.Float.parseFloat(s);
				}
				catch (java.lang.Exception exception)
				{
					ERR_HARD("bad format timeLen: " + s);
					bExit = true;
				}
			}
			
		});
		param.put("TIMENATIVE", new Token()
		{
			
			public void parse()
			{
				sarg.timeNativeExist = true;
				java.lang.String s = getStr();
				try
				{
					sarg.timeNative = java.lang.Integer.parseInt(s) != 0;
				}
				catch (java.lang.Exception exception)
				{
					ERR_HARD("bad format timeNative: " + s);
					bExit = true;
				}
			}
			
		});
		param.put("TYPE", new Token()
		{
			
			public void parse()
			{
				sarg.typeExist = true;
				java.lang.String s = getStr();
				try
				{
					sarg.type = java.lang.Integer.parseInt(s);
				}
				catch (java.lang.Exception exception)
				{
					ERR_HARD("bad format type: " + s);
					bExit = true;
				}
			}
			
		});
		param.put("PATH", new Token()
		{
			
			public void parse()
			{
				sarg.path = getStr();
			}
			
		});
		param.put("TARGET", new Token()
		{
			
			public void parse()
			{
				sarg.target = getStr();
			}
			
		});
		param.put("ACOUSTIC", new Token()
		{
			
			public void parse()
			{
				sarg.acoustic = getStr();
			}
			
		});
		param.put("SOUND", new Token()
		{
			
			public void parse()
			{
				sarg.sound = getStr();
			}
			
		});
		param.put("PRELOAD", new Token()
		{
			
			public void parse()
			{
				sarg.preload = getStr();
			}
			
		});
		param.put("COLOR", new Token()
		{
			
			public void parse()
			{
				java.lang.String s = getStr();
				sarg.color3f = color3f;
				color3f.set(1.0F, 1.0F, 1.0F);
				if (s != "")
				{
					com.maddox.util.NumberTokenizer numbertokenizer = new NumberTokenizer(s, " ");
					try
					{
						if (numbertokenizer.hasMoreTokens())
							color3f.x = numbertokenizer.nextFloat();
						if (numbertokenizer.hasMoreTokens())
							color3f.y = numbertokenizer.nextFloat();
						if (numbertokenizer.hasMoreTokens())
							color3f.z = numbertokenizer.nextFloat();
					}
					catch (java.lang.Exception exception)
					{
						ERR_HARD("bad format color3f: " + s);
						bExit = true;
					}
				}
			}
			
		});
		param.put("LIGHT", new Token()
		{
			
			public void parse()
			{
				java.lang.String s = getStr();
				sarg.light = light;
				sarg.light[0] = 1.0F;
				sarg.light[1] = 10F;
				if (s != "")
				{
					com.maddox.util.NumberTokenizer numbertokenizer = new NumberTokenizer(s, " ");
					try
					{
						if (numbertokenizer.hasMoreTokens())
							sarg.light[0] = numbertokenizer.nextFloat();
						if (numbertokenizer.hasMoreTokens())
							sarg.light[1] = numbertokenizer.nextFloat();
					}
					catch (java.lang.Exception exception)
					{
						ERR_HARD("bad format light: " + s);
						bExit = true;
					}
				}
			}
			
		});
		param.put("Z0", new Token()
		{
			
			public void parse()
			{
				sarg.Z0Exist = true;
				java.lang.String s = getStr();
				try
				{
					sarg.Z0 = java.lang.Float.parseFloat(s);
				}
				catch (java.lang.Exception exception)
				{
					ERR_HARD("bad format Z0: " + s);
					bExit = true;
				}
			}
			
		});
		param.put("FM", new Token()
		{
			
			public void parse()
			{
				sarg.FM = getStr();
			}
			
		});
		param.put("FM_Type", new Token()
		{
			
			public void parse()
			{
				java.lang.String s = getStr();
				try
				{
					sarg.FM_Type = java.lang.Integer.parseInt(s);
				}
				catch (java.lang.Exception exception)
				{
					ERR_HARD("bad format FM_Type: " + s);
					bExit = true;
				}
			}
			
		});
		param.put("WEAPONS", new Token()
		{
			
			public void parse()
			{
				sarg.weapons = getStr();
			}
			
		});
		param.put("FUEL", new Token()
		{
			
			public void parse()
			{
				java.lang.String s = getStr();
				try
				{
					sarg.fuel = java.lang.Float.parseFloat(s) / 100F;
					if (sarg.fuel > 1.0F)
						sarg.fuel = 1.0F;
					if (sarg.fuel < 0.0F)
						sarg.fuel = 0.0F;
				}
				catch (java.lang.Exception exception)
				{
					ERR_HARD("bad format fuel: " + s);
					bExit = true;
				}
			}
			
		});
		param.put("SPEED", new Token()
		{
			
			public void parse()
			{
				java.lang.String s = getStr();
				sarg.speed = speed3d;
				speed3d.set(0.0D, 0.0D, -1D);
				if (s != "")
				{
					com.maddox.util.NumberTokenizer numbertokenizer = new NumberTokenizer(s, " ");
					try
					{
						if (numbertokenizer.hasMoreTokens())
							speed3d.x = numbertokenizer.nextFloat();
						if (numbertokenizer.hasMoreTokens())
							speed3d.y = numbertokenizer.nextFloat();
						if (numbertokenizer.hasMoreTokens())
							speed3d.z = numbertokenizer.nextFloat();
					}
					catch (java.lang.Exception exception)
					{
						ERR_HARD("bad format speed: " + s);
						bExit = true;
					}
				}
			}
			
		});
		param.put("SKILL", new Token()
		{
			
			public void parse()
			{
				java.lang.String s = getStr();
				try
				{
					sarg.skill = java.lang.Integer.parseInt(s);
				}
				catch (java.lang.Exception exception)
				{
					ERR_HARD("bad format skill: " + s);
					bExit = true;
				}
			}
			
		});
		param.put("PLAYER", new Token()
		{
			
			public void parse()
			{
				getStr();
				sarg.bPlayer = true;
			}
			
		});
		param.put("NUMBEROFF", new Token()
		{
			
			public void parse()
			{
				getStr();
				sarg.bNumberOn = false;
			}
			
		});
		param.put("BORNPLACE", new Token()
		{
			
			public void parse()
			{
				java.lang.String s = getStr();
				try
				{
					sarg.bornPlace = java.lang.Integer.parseInt(s);
				}
				catch (java.lang.Exception exception)
				{
					ERR_HARD("bad format born place: " + s);
					bExit = true;
				}
				sarg.bornPlaceExist = true;
			}
			
		});
		param.put("STAYPLACE", new Token()
		{
			
			public void parse()
			{
				java.lang.String s = getStr();
				try
				{
					sarg.stayPlace = java.lang.Integer.parseInt(s);
				}
				catch (java.lang.Exception exception)
				{
					ERR_HARD("bad format stay place: " + s);
					bExit = true;
				}
				sarg.stayPlaceExist = true;
			}
			
		});
		param.put("RAWDATA", new Token()
		{
			
			public void parse()
			{
				java.lang.String s = getStr();
				sarg.rawData = s;
			}
			
		});
		//TODO: Added by |ZUTI|
		//--------------------------------------------
		param.put("Z_AIRSTART", new Token()
		{
			public void parse()
			{
				getStr();
				sarg.bZutiAirstart = true;
			}
		});
		param.put("Z_MULTICREW", new Token()
		{
			public void parse()
			{
				getStr();
				sarg.bZutiMultiCrew = true;
			}
		});
		param.put("Z_MULTICREW_ANYTIME", new Token()
		{
			public void parse()
			{
				getStr();
				sarg.bZutiMultiCrewAnytime = true;
			}
		});
		//--------------------------------------------
		_properties.put("NAME", "spawn");
		_levelAccess = 0;
	}
}