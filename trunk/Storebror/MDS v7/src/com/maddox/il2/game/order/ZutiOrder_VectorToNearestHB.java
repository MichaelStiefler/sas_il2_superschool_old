package com.maddox.il2.game.order;

import java.util.Timer;
import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.ZutiSupportMethods_Net;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.vehicles.radios.BeaconGeneric;

class ZutiOrder_VectorToNearestHB extends Order
{
	private static Point3d V1 = new Point3d();
	private static Point3d V2 = new Point3d();
	
	private class DelayedOrder extends java.util.TimerTask
    {
        public void run()
        {
            Voice.speakHeadingToHome(ac, ZutiOrder_VectorToNearestHB.V1);
        }

        private java.util.Timer timer;
        private Aircraft ac;

        public DelayedOrder(Aircraft aircraft, double d)
        {
            timer = null;
            ac = null;
            ac = aircraft;
            timer = new Timer();
            long l = (long)Math.random() * (long)(d / 7D);
            timer.schedule(this, 10000L + l);
        }
    }
	
	public ZutiOrder_VectorToNearestHB()
	{
		super("mds.vectorToNearestHB");
	}
	
	public void run()
	{
		if( Mission.MDS_VARIABLES().zutiMisc_DisableVectoring )
		{
			HUD.log("mds.vectoringNotPossible");
			return;
		}
		
		try
		{
			Player().FM.moveCarrier();
			Point3d playerPos = Player().FM.actor.pos.getAbsPoint();
			if( Main.cur().netServerParams != null && Main.cur().netServerParams.isDogfight() )
			{
				//Get user born place
				BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace(playerPos.x, playerPos.y, Player().getArmy());
				if( bp != null )
					V2 = new Point3d(bp.place.x, bp.place.y, 0.0D);
				else
					V2 = new Point3d(0.0D, 0.0D, 0.0D);
			}
			else
			{
				//Original code
				com.maddox.il2.ai.Way way = Player().FM.AP.way;
				com.maddox.il2.ai.WayPoint waypoint = way.get(way.size() - 1);
				waypoint.getP(V2);
			}
			
			//System.out.println("V1: " + V1.toString());
			//System.out.println("V2: " + V2.toString());
			
			Player().FM.actor.pos.getAbs(V1);
			V1.sub(V2);
			
			double d = Math.sqrt(V1.x * V1.x + V1.y * V1.y);
	        double d1 = BeaconGeneric.lineOfSightDelta(Player().FM.getAltitude(), V2.z, d);
	        if((d1 < 0.0D || d > 90000D) && World.cur().diffCur.RealisticNavigationInstruments)
	            return;
			
	        /*
			if( Main.cur().netServerParams != null && Main.cur().netServerParams.isMaster() )
			{
				float f = 57.32484F * (float)java.lang.Math.atan2(V1.x, V1.y);
				int i = (int)f;
				i = (i + 180) % 360;
				HUD.logCenter("Nearest HomeBase heading: " + new Integer(i).toString() + "°");
			}
			*/
	        
			if(isEnableVoice())
			{
				if(World.cur().diffCur.RealisticNavigationInstruments)
                	new DelayedOrder(Player(), d);
                else
                    Voice.speakHeadingToHome(Player(), V1);
			}
		}
		catch(Exception ex){ex.toString();}
	}
}