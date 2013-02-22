using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using System.Reflection;
using System.Text.RegularExpressions;
using System.Diagnostics;
using System.Collections;
using System.Globalization;

namespace ButtonsToCsv
{
    class Program
    {
        private class fmdData
        {
            public AircraftData Aircraft = new AircraftData();
            public MassData Mass = new MassData();
            public ControlsData Controls = new ControlsData();
            public SquaresData Squares = new SquaresData();
            public ToughnessData Toughness = new ToughnessData();
            public ArmData Arm = new ArmData();
            public EngineData Engine = new EngineData();
            public GearData Gear = new GearData();
            public ParamsData Params = new ParamsData();
            public PolaresData Polares = new PolaresData();
            public SOUNDData SOUND = new SOUNDData();
            public class AircraftData
            {
                public int Type = -1;
                public int Crew = 0;
                public float Wingspan = 0.0F;
                public float Length = 0.0F;
                public int Seaplane = 0;
                public int Canard = 0;
                public int Jet = 0;
                public int JetHiV = 0;
                public int CrewFunction0 = 1;
                public int CrewFunction1 = 7;
                public int CrewFunction2 = 7;
                public int CrewFunction3 = 7;
                public int CrewFunction4 = 7;
                public int CrewFunction5 = 7;
                public int CrewFunction6 = 7;
                public int CrewFunction7 = 7;
                public int CrewFunction8 = 7;
                public int CrewFunction9 = 7;
            }
            public class MassData
            {
                public float Empty = 0.0F;
                public float TakeOff = 0.0F;
                public float Oil = -1.0F;
                public float Fuel = 0.0F;
                public float Nitro = 0.0F;
            }
            public class ControlsData
            {
                public int CAileron = 0;
                public int CAileronTrim = 0;
                public int CElevator = 0;
                public int CElevatorTrim = 0;
                public int CRudder = 0;
                public int CRudderTrim = 0;
                public int CFlap = 0;
                public int CFlapPos = -1;
                public int CDiveBrake = 0;
                public int CUndercarriage = 0;
                public int CLockTailwheel = 0;
                public int CStabilizer = 0;
                public int CArrestorHook = 0;
                public int CWingFold = 0;
                public int CCockpitDoor = 0;
                public int CWheelBrakes = 1;
                public int CBombBay = 0;
                public int CElectricProp = 0;
                public float CAileronThreshold = 360.0F;
                public float CRudderThreshold = 360.0F;
                public float CElevatorThreshold = 403.20001F;
                public float DefaultAileronTrim = -999.0F;
                public float DefaultElevatorTrim = -999.0F;
                public float DefaultRudderTrim = -999.0F;
                public float GearPeriod = -999.0F;
                public float WingPeriod = -999.0F;
                public float CockpitDoorPeriod = -999.0F;
            }
            public class SquaresData
            {
                public float Wing = 0.0F;
                public float Aileron = 0.0F;
                public float Flap = 0.0F;
                public float Stabilizer = 0.0F;
                public float Elevator = 0.0F;
                public float Keel = 0.0F;
                public float Rudder = 0.0F;
                public float Wing_In = 0.0F;
                public float Wing_Mid = 0.0F;
                public float Wing_Out = 0.0F;
                public float AirbrakeCxS = 0.0F;
            }
            public class ToughnessData
            {
                public int AroneL = 100;
                public int AroneR = 100;
                public int CF = 100;
                public int Engine1 = 100;
                public int Engine2 = 100;
                public int Engine3 = 100;
                public int Engine4 = 100;
                public int GearC2 = 100;
                public int FlapR = 100;
                public int GearL2 = 100;
                public int GearR2 = 100;
                public int Keel1 = 100;
                public int Keel2 = 100;
                public int Nose = 100;
                public int Oil = 100;
                public int Rudder1 = 100;
                public int Rudder2 = 100;
                public int StabL = 100;
                public int StabR = 100;
                public int Tail1 = 100;
                public int Tail2 = 100;
                public int Tank1 = 100;
                public int Tank2 = 100;
                public int Tank3 = 100;
                public int Tank4 = 100;
                public int Turret1B = 100;
                public int Turret2B = 100;
                public int Turret3B = 100;
                public int Turret4B = 100;
                public int Turret5B = 100;
                public int Turret6B = 100;
                public int VatorL = 100;
                public int VatorR = 100;
                public int WingLIn = 100;
                public int WingLMid = 100;
                public int WingLOut = 100;
                public int WingRIn = 100;
                public int WingRMid = 100;
                public int WingROut = 100;
                public int Flap01 = 100;
                public int Flap02 = 100;
                public int Flap03 = 100;
                public int Flap04 = 100;
                public int NullPart = 100;
                public int EXPIRED = 100;
            }
            public class ArmData
            {
                public float Aileron = 0.0F;
                public float Flap = 0.0F;
                public float Stabilizer = 0.0F;
                public float Keel = 0.0F;
                public float Elevator = 0.0F;
                public float Rudder = 0.0F;
                public float Wing_In = 0.0F;
                public float Wing_Mid = 0.0F;
                public float Wing_Out = 0.0F;
                public float Wing_V = 0.0F;
                public float GCenter = 0.0F;
                public float GCenterZ = 0.0F;
                public float GC_AOA_Shift = 0.0F;
                public float GC_Flaps_Shift = 0.0F;
                public float GC_Gear_Shift = 0.0F;
            }
            public class EngineData
            {
                public string Engine0Family = "";
                public string Engine0SubModel = "";
                public string Engine1Family = "";
                public string Engine1SubModel = "";
                public string Engine2Family = "";
                public string Engine2SubModel = "";
                public string Engine3Family = "";
                public string Engine3SubModel = "";
                public string Engine4Family = "";
                public string Engine4SubModel = "";
                public string Engine5Family = "";
                public string Engine5SubModel = "";
                public float Position0x = 0.0F;
                public float Position0y = 0.0F;
                public float Position0z = 0.0F;
                public float PropPosition0x = 0.0F;
                public float PropPosition0y = 0.0F;
                public float PropPosition0z = 0.0F;
                public float Vector0x = 0.0F;
                public float Vector0y = 0.0F;
                public float Vector0z = 0.0F;
                public float Position1x = 0.0F;
                public float Position1y = 0.0F;
                public float Position1z = 0.0F;
                public float PropPosition1x = 0.0F;
                public float PropPosition1y = 0.0F;
                public float PropPosition1z = 0.0F;
                public float Vector1x = 0.0F;
                public float Vector1y = 0.0F;
                public float Vector1z = 0.0F;
                public float Position2x = 0.0F;
                public float Position2y = 0.0F;
                public float Position2z = 0.0F;
                public float PropPosition2x = 0.0F;
                public float PropPosition2y = 0.0F;
                public float PropPosition2z = 0.0F;
                public float Vector2x = 0.0F;
                public float Vector2y = 0.0F;
                public float Vector2z = 0.0F;
                public float Position3x = 0.0F;
                public float Position3y = 0.0F;
                public float Position3z = 0.0F;
                public float PropPosition3x = 0.0F;
                public float PropPosition3y = 0.0F;
                public float PropPosition3z = 0.0F;
                public float Vector3x = 0.0F;
                public float Vector3y = 0.0F;
                public float Vector3z = 0.0F;
                public float Position4x = 0.0F;
                public float Position4y = 0.0F;
                public float Position4z = 0.0F;
                public float PropPosition4x = 0.0F;
                public float PropPosition4y = 0.0F;
                public float PropPosition4z = 0.0F;
                public float Vector4x = 0.0F;
                public float Vector4y = 0.0F;
                public float Vector4z = 0.0F;
                public float Position5x = 0.0F;
                public float Position5y = 0.0F;
                public float Position5z = 0.0F;
                public float PropPosition5x = 0.0F;
                public float PropPosition5y = 0.0F;
                public float PropPosition5z = 0.0F;
                public float Vector5x = 0.0F;
                public float Vector5y = 0.0F;
                public float Vector5z = 0.0F;
            }
            public class GearData
            {
                public float SinkFactor = 1.0F;
                public float SpringsStiffness = 1.0F;
                public float TailStiffness = 0.6F;
                public int FromIni = 0;
                public float H = 2.0F;
                public float Pitch = 10.0F;
                public string WaterClipList = "-";
            }
            public class ParamsData
            {
                public float CriticalAOA = 16F;
                public float Vmax = 500F;
                public float Vmin = 160F;
                public float VmaxFLAPS = 270F;
                public float VminFLAPS = 140F;
                public float HofVmax = 100F;
                public float T_turn = 20F;
                public float V_turn = 300F;
                public float Vz_climb = 18F;
                public float V_climb = 270F;
                public float K_max = 14F;
                public float Cy0_max = 0.15F;
                public float FlapsMult = 0.16F;
                public float FlapsAngSh = 4F;
                public float ReferenceWeight = 0.0F;
                public float G_CLASS = 12F;
                public float G_CLASS_COEFF = 20F;
                public float VmaxH = 1.0F;
                public float SensYaw = 1.0F;
                public float SensPitch = 1.0F;
                public float SensRoll = 1.0F;
                public float VmaxAllowed = 1.3F;
                public float Range = 800F;
                public float CruiseSpeed = 0.7F * 500F;
                public float SpinCxLoss = -1F;
                public float SpinCyLoss = -1F;
            }
            public class PolaresData
            {
                public float lineCyCoeff = -999F;
                public float AOAMinCx_Shift = 0.0F;
                public float Cy0_0 = 0.15F;
                public float AOACritH_0 = 16F;
                public float AOACritL_0 = -16F;
                public float CyCritH_0 = 1.1F;
                public float CyCritL_0 = -0.8F;
                public float parabCxCoeff_0 = 0.0008F;
                public float CxMin_0 = 0.026F;
                public float Cy0_1 = 0.65F;
                public float AOACritH_1 = 15F;
                public float AOACritL_1 = -18F;
                public float CyCritH_1 = 1.6F;
                public float CyCritL_1 = -0.75F;
                public float CxMin_1 = 0.09F;
                public float parabCxCoeff_1 = 0.0025F;
                public float parabAngle = 5F;
                public float Decline = 0.007F;
                public float maxDistAng = 30F;
                private string mc3Internal = "";
                public string mc3
                {
                    get
                    {
                        return this.mc3Internal;
                    }
                    set
                    {
                        this.mc3Internal = value;
                        string[] mc3ValueStrings = value.Split(",".ToCharArray());
                        for (int i = 0; i < mc3ValueStrings.Length; i++)
                        {
                            if (i > 7) break;
                            float.TryParse(mc3ValueStrings[i], NumberStyles.Any, CultureInfo.InvariantCulture, out mc3Values[i]);
                        }
                    }
                }
                public float[] mc3Values = new float[8];
                private string mc4Internal = "";
                public string mc4
                {
                    get
                    {
                        return this.mc4Internal;
                    }
                    set
                    {
                        this.mc4Internal = value;
                        string[] mc4ValueStrings = value.Split(",".ToCharArray());
                        for (int i = 0; i < mc4ValueStrings.Length; i++)
                        {
                            if (i > 7) break;
                            float.TryParse(mc4ValueStrings[i], NumberStyles.Any, CultureInfo.InvariantCulture, out mc4Values[i]);
                        }
                    }
                }
                public float[] mc4Values = new float[8];
                private string mmInternal = "";
                public string mm
                {
                    get
                    {
                        return this.mmInternal;
                    }
                    set
                    {
                        this.mmInternal = value;
                        string[] mmValueStrings = value.Split(",".ToCharArray());
                        for (int i = 0; i < mmValueStrings.Length; i++)
                        {
                            if (i > 7) break;
                            float.TryParse(mmValueStrings[i], NumberStyles.Any, CultureInfo.InvariantCulture, out mmValues[i]);
                        }
                    }
                }
                public float[] mmValues = new float[8];
                private string mzInternal = "";
                public string mz
                {
                    get
                    {
                        return this.mzInternal;
                    }
                    set
                    {
                        this.mzInternal = value;
                        string[] mzValueStrings = value.Split(",".ToCharArray());
                        for (int i = 0; i < mzValueStrings.Length; i++)
                        {
                            if (i > 7) break;
                            float.TryParse(mzValueStrings[i], NumberStyles.Any, CultureInfo.InvariantCulture, out mzValues[i]);
                        }
                    }
                }
                public float[] mzValues = new float[8];
            }
            public class SOUNDData
            {
                public string FeedType = "PNEUMATIC";
                public string Diving = "";
                public string Engine = "std";
            }
        }

        static void Main(string[] args)
        {
            StreamWriter logFile = File.CreateText("ButtonsToCsv.log");
            StreamWriter csvFmd = File.CreateText("fmd.csv");

            fmdData emptyFmdData = new fmdData();
            csvFmd.Write("FMD");
            foreach (FieldInfo fi1 in emptyFmdData.GetType().GetFields())
            {
                if (fi1.Name == "Engine") continue;
                foreach (FieldInfo fi2 in fi1.FieldType.GetFields())
                {
                    switch (fi2.Name)
                    {
                        case "mc3Values":
                            csvFmd.Write(";[{0}] {1}[0];[{0}] {1}[1];[{0}] {1}[2];[{0}] {1}[3];[{0}] {1}[4];[{0}] {1}[5];[{0}] {1}[6];[{0}] {1}[7]", fi1.Name, "mc3");
                            break;
                        case "mc4Values":
                            csvFmd.Write(";[{0}] {1}[0];[{0}] {1}[1];[{0}] {1}[2];[{0}] {1}[3];[{0}] {1}[4];[{0}] {1}[5];[{0}] {1}[6];[{0}] {1}[7]", fi1.Name, "mc4");
                            break;
                        case "mmValues":
                            csvFmd.Write(";[{0}] {1}[0];[{0}] {1}[1];[{0}] {1}[2];[{0}] {1}[3];[{0}] {1}[4];[{0}] {1}[5];[{0}] {1}[6];[{0}] {1}[7]", fi1.Name, "mm");
                            break;
                        case "mzValues":
                            csvFmd.Write(";[{0}] {1}[0];[{0}] {1}[1];[{0}] {1}[2];[{0}] {1}[3];[{0}] {1}[4];[{0}] {1}[5];[{0}] {1}[6];[{0}] {1}[7]", fi1.Name, "mz");
                            break;
                        default:
                            csvFmd.Write(";[{0}] {1}", fi1.Name, fi2.Name);
                            break;
                    }
                }
            }
            csvFmd.WriteLine();
            Console.WriteLine("Searching for .fmd files...");
            string[] fmdFiles = Directory.GetFiles(".", "*.fmd", SearchOption.AllDirectories);
            foreach (string theFmdFile in fmdFiles)
            {
                Dictionary<string, Dictionary<string, string>> fmdDataFromFile = new Dictionary<string, Dictionary<string, string>>();
                string section = "";
                string fmdLine;
                StreamReader fmdReader = new StreamReader(theFmdFile);
                Dictionary<string, string> sectionDictionary = null;
                while ((fmdLine = fmdReader.ReadLine()) != null)
                {
                    fmdLine = fmdLine.Trim();
                    if (fmdLine.StartsWith("["))
                    {
                        if (sectionDictionary != null)
                            if (!fmdDataFromFile.ContainsKey(section))
                                fmdDataFromFile.Add(section, sectionDictionary);
                        section = fmdLine.TrimStart("[".ToCharArray()).TrimEnd("]".ToCharArray());
                        sectionDictionary = new Dictionary<string, string>();
                        continue;
                    }
                    if (section.Length == 0) continue;
                    int spacePos = fmdLine.IndexOf(" ");
                    if (spacePos == -1)
                    {
                        if (!sectionDictionary.ContainsKey(fmdLine))
                            sectionDictionary.Add(fmdLine, "");
                    }
                    else
                    {
                        if (!sectionDictionary.ContainsKey(fmdLine.Substring(0, spacePos)))
                            sectionDictionary.Add(
                                fmdLine.Substring(0, spacePos),
                                fmdLine.Substring(spacePos + 1, fmdLine.Length - spacePos - 1));
                    }
                }
                if (sectionDictionary != null) fmdDataFromFile.Add(section, sectionDictionary);
                fmdReader.Close();
                fmdData theFmdData = new fmdData();

                foreach (FieldInfo fi in theFmdData.GetType().GetFields())
                {
                    if (fmdDataFromFile.ContainsKey(fi.Name))
                    {
                        Type fieldDataType = fi.FieldType;
                        foreach (KeyValuePair<string, string> fmdKVP in fmdDataFromFile[fi.Name])
                        {
                            FieldInfo fieldDataKeyInfo = fieldDataType.GetField(fmdKVP.Key, BindingFlags.IgnoreCase | BindingFlags.Public | BindingFlags.Instance);
                            if (fieldDataKeyInfo != null)
                            {
                                if (fmdKVP.Value.Length > 0)
                                    fieldDataKeyInfo.SetValue(fi.GetValue(theFmdData), Convert.ChangeType(fmdKVP.Value, fieldDataKeyInfo.FieldType, NumberFormatInfo.InvariantInfo));
                                else
                                    fieldDataKeyInfo.SetValue(fi.GetValue(theFmdData), null);
                                continue;
                            }
                            PropertyInfo propertyDataKeyInfo = fieldDataType.GetProperty(fmdKVP.Key, BindingFlags.IgnoreCase | BindingFlags.Public | BindingFlags.Instance);
                            if (propertyDataKeyInfo != null)
                            {
                                if (fmdKVP.Value.Length > 0)
                                    propertyDataKeyInfo.SetValue(fi.GetValue(theFmdData), Convert.ChangeType(fmdKVP.Value, propertyDataKeyInfo.PropertyType, NumberFormatInfo.InvariantInfo), null);
                                else
                                    propertyDataKeyInfo.SetValue(fi.GetValue(theFmdData), null, null);
                                continue;
                            }
                            logFile.WriteLine("Ignoring invalid Entry in {3}: [{0}] {1} = {2}", fi.Name, fmdKVP.Key, fmdKVP.Value, Path.GetFileName(theFmdFile));
                        }
                    }
                }
                csvFmd.Write(Path.GetFileName(theFmdFile));
                foreach (FieldInfo fi1 in theFmdData.GetType().GetFields())
                {
                    if (fi1.Name == "Engine") continue;
                    foreach (FieldInfo fi2 in fi1.FieldType.GetFields())
                    {
                        switch (fi2.Name)
                        {
                            case "mc3Values":
                            case "mc4Values":
                            case "mmValues":
                            case "mzValues":
                                {
                                    Object machValue = fi2.GetValue(fi1.GetValue(theFmdData));
                                    Single[] machValues = (Single[])machValue;
                                    for (int i = 0; i < 8; i++)
                                    {
                                        csvFmd.Write(string.Format(";{0}", machValues[i]));
                                    }
                                }
                                break;

                            default:
                                csvFmd.Write(string.Format(";{0}", fi2.GetValue(fi1.GetValue(theFmdData))));
                                break;
                        }
                    }
                }
                csvFmd.WriteLine();
            }
            csvFmd.Close();
            logFile.Close();
            Console.ReadKey();

        }
    }
}
