This ZIP archive file is a Catapult MOD v1.01 for IL-2 1946 4.12 + Modact 5
!!!! SAS Modact5 ONLY !!!!

=== Catapult/Arresting wire MOD ===

Catapult MOD is originated by Fireball, Pablo and fokker tweaked to fit The Jet Era, modified by western0221 for use in Modact 5

This MOD depends on SAS Modact5.
On other MOD environment it likely won't work.

How to Install:

1. Drop the contents of this zip file into your IL-2 1946 4.12 + Modact 5 game folder.

2. Run JSGME and enable the mod.


- Catapult power increasing at each year
MOD default parameters are changed by mission's year:
At     -1945 CatapultPower=10 , CatapultPowerJets:ignored and use CatapultPower
At 1946-1950 CatapultPower=13 , CatapultPowerJets=19
At 1951-1954 CatapultPower=16 , CatapultPowerJets=22
At 1955-1971 CatapultPower=19 , CatapultPowerJets=25
At 1971-     CatapultPower=19 , CatapultPowerJets=30
 +PLUS 1953- adding push power variablly with Fuel/Weapons Loadout 

You can chenge CatapultPower and CatapultPowerJets parameters in conf.ini [Mods] section.
Parameters you write are used at missions 1955-1971.
Before 1955, Powers are automatically decreased each -3, 
and CatapultPowerJets is ignored before 1946.


- There are several parameters that the mission builder can add to the [Mods] section of the mission (.mis) file that affect the use of the catapult. By default the catapult will work on all ships for both human players and AI. You only need to add any of these parameters if want to change the default behavior. NOTE: If you make any changes to the mission file in the Full Mission Builder after adding these parameters, they will disappear and will have to be re-entered:

  CatapultAllow - Setting this to 0 disables the catapults on all ships for human players and AI. Default is 1.

  CatapultAllowAI - Setting this to 0 causes the AI to use rolling takeoff on all ships. Human players can still use catapult. Default is 1. (This parameter can also be put in the [Mods] section of your conf.ini to set the default behavior of the AI.)

  CatapultBoost - In dogfight and single-player missions the mission builder can set the parameter 'CatapultBoost 1' . This will increase the power of the catapult by approximately 30 knots. This gives you roughly the same results as you would have in a carrier moving at 30 knots in a co-op mission. Default is 1.
When you need steam catapult powerful pushing, you have to set CatapultBoost 1 or Default.

  CatapultPower - Catapult Boost power for Recipros, default =19. Need CatapultBoost=1.
  CatapultPowerJets - Catapult Boost power for Jets after mission year 1945, default =25. Need CatapultBoost=1. 
  - Catapult power increasing at each year
  MOD default parameters are changed by mission's year:
  At     -1945 CatapultPower=10 , CatapultPowerJets:ignored and use CatapultPower
  At 1946-1950 CatapultPower=13 , CatapultPowerJets=19
  At 1951-1954 CatapultPower=16 , CatapultPowerJets=22
  At 1955-1958 CatapultPower=19 , CatapultPowerJets=25
  At 1959-     CatapultPower=22 , CatapultPowerJets=35
   +PLUS 1953- adding push power variablly with Fuel/Weapons Loadout 


  StandardDeckCVL - Setting this to 1 causes the catapult to be setup for the "standard deck" on the CVL's. Default is 0 (short deck).  (This parameter can also be put in the [Mods] section of your conf.ini to set the default behavior of the AI and your aircraft.)

  CatapultAI_CVE - Setting this to 0 causes the AI to use rolling takeoff on all CVE's instead of catapult. Default is 1. (This parameter can also be put in the [Mods] section of your conf.ini to set the default behavior of the AI.)

  CatapultAI_CVL - Setting this to 0 causes the AI to use rolling takeoff on all CVL's instead of catapult. Default is 1. (This parameter can also be put in the [Mods] section of your conf.ini to set the default behavior of the AI.)

  CatapultAI_EssexClass - Setting this to 0 causes the AI to use rolling takeoff on Essex and Intrepid instead of catapult. Default is 1. (This parameter can also be put in the [Mods] section of your conf.ini to set the default behavior of the AI.)

  CatapultAI_Illustrious - Setting this to 0 causes the AI to use rolling takeoff on Illustrious instead of catapult. Default is 1. (This parameter can also be put in the [Mods] section of your conf.ini to set the default behavior of the AI.)

  CatapultAI_GrafZep - Setting this to 0 causes the AI to use rolling takeoff on the Graf Zeppelin instead of catapult. Default is 1. (This parameter can also be put in the [Mods] section of your conf.ini to set the default behavior of the AI.)

  CatapultAI_ClemenceauClass - Setting this to 0 causes the AI to use rolling takeoff on the Clemenceau Class instead of catapult. Default is 1. (This parameter can also be put in the [Mods] section of your conf.ini to set the default behavior of the AI.)

  NoNavLightsAI - Setting this to 1 will cause the navlights on AI aircraft to stay off at night. Default is 0. (This parameter can also be put in the [Mods] section of your conf.ini to set the default behavior of the AI.).

  FastLaunchAI - Setting this to 1 will cause the AI to move into launch position with their wings already unfolded. Default is 0. In co-op missions, players other than the host will still see the AI unfold their wings AFTER they've moved into position, and they may takeoff before their wings are completely unfolded. The wings will finish unfolding as they launch, and it will not affect the flight characteristics. The FastLaunchAI parameter is useful if you just want to get the AI off the deck as quickly as possible. For most aircraft it will not save much time, but for aircraft that unfold their wings very slowly (ex. F4U, B5N, B6N) it can save several seconds per aircraft launched. (This parameter can also be put in the [Mods] section of your conf.ini to set the default behavior of the AI.).

Sample [Mods] section in mission (.mis) file:

[Mods]
  CatapultAI_EssexClass  0
  NoNavLightsAI  1
  FastLaunchAI  1 

Some AI-related parameters, as described above, can be put into the host's conf.ini instead of the mission file. The format in the conf.ini is slightly different. The parameter name and value are separated by an equal (=) sign instead of one or more spaces.

Sample [Mods] section in conf.ini file:

[Mods]
  CatapultAI_EssexClass=0
  NoNavLightsAI=1
  WingsFoldedAI=1 


Staff:
java - Fireball, Pablo, fokker95, western0221, SAS~Storebror