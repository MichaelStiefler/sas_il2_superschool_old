/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.game.order;

import java.util.ArrayList;

import com.maddox.il2.ai.Airport;
import com.maddox.il2.ai.AirportGround;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.Voice;

public class OrderRequest_For_RunwayLights extends Order {

    public OrderRequest_For_RunwayLights() {
        super("Request_Runway_Lights");
    }

    private AirportGround getNearestAirportWithLights(Aircraft aircraft) {
        AirportGround airportground = null;
        double d = 1.7976931348623157E+308D;
        ArrayList arraylist = new ArrayList();
        World.getAirports(arraylist);
        for (int i = 0; i < arraylist.size(); i++) {
            Airport airport = (Airport) arraylist.get(i);
            if (airport != null && (airport instanceof AirportGround) && airport.isAlive() && ((AirportGround) airport).hasLights()/* && GUI.pad.getAirportArmy(airport) == aircraft.getArmy() */) {
                double d1 = airport.distance(aircraft);
                if (d1 < 10000D && d1 < d) {
                    d = d1;
                    airportground = (AirportGround) airport;
                }
            }
        }

        return airportground;
    }

    public void run() {
        Aircraft aircraft = this.Player();
        AirportGround airportground = this.getNearestAirportWithLights(aircraft);
        if (airportground != null) {
            Actor actor = War.GetNearestEnemy(aircraft, -1, 12000F, 16);
            if (actor == null) {
                Voice.speakRooger(aircraft);
                airportground.turnOnLights(aircraft);
            } else if (actor.getArmy() == aircraft.getArmy()) {
                Voice.speakRooger(aircraft);
                airportground.turnOnLights(aircraft);
            } else {
                Voice.speakNegative(aircraft);
            }
        }
    }
}
