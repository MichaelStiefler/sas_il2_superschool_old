package com.maddox.il2.objects.vehicles.planes;

public abstract class PlaneAH1 extends Plane {
	public static class AH1 extends PlaneGeneric {

		public AH1() {
		}
	}

	public PlaneAH1() {
	}

	static {
		new PlaneGeneric.SPAWN(PlaneAH1.AH1.class);
	}
}
