package com.maddox.il2.game.order;

import com.maddox.il2.ai.World;
import com.maddox.il2.game.HUD;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.air.Aircraft;

class ZutiOrder_ResourcesInformation extends com.maddox.il2.game.order.Order
{	
	public ZutiOrder_ResourcesInformation()
	{
		super("mds.ResourcesInfo");
	}
	
	public void run()
	{
		try
		{
			Aircraft aircraft = World.getPlayerAircraft();
			if( aircraft == null )
			{
				System.out.println("Resources information can not be obtained because player AC does not exist!");
				return;
			}
			
			ZutiSupportMethods_NetSend.requestResourcesInformation(aircraft.pos.getAbsPoint());
			HUD.log( "mds.resourcesInfo" );
		}
		catch(Exception ex){ex.printStackTrace();}
	}
}