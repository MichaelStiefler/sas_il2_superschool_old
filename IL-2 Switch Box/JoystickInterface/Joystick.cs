/******************************************************************************
 * C# Joystick Library - Copyright (c) 2006 Mark Harris - MarkH@rris.com.au
 ******************************************************************************
 * You may use this library in your application, however please do give credit
 * to me for writing it and supplying it. If you modify this library you must
 * leave this notice at the top of this file. I'd love to see any changes you
 * do make, so please email them to me :)
 *****************************************************************************/
using System;
using System.Collections.Generic;
using System.Text;
using Microsoft.DirectX.DirectInput;
using System.Diagnostics;
using System.Threading;
using System.Runtime.InteropServices;

namespace com.sas1946.il2.directx.dinput.Joystick
{
    /// <summary>
    /// Class to interface with a joystick device.
    /// </summary>
    public class Joystick
    {
        [DllImport("user32.dll")]
        static extern bool PostMessage(IntPtr hWnd, uint Msg, int wParam, int lParam);

        private Device joystickDevice;
        private JoystickState state;

        private AutoResetEvent joystickEvent = new AutoResetEvent(true);
        private WaitHandle[] joystickEventWait = new WaitHandle[1];
        
        private int axisCount;
        /// <summary>
        /// Number of axes on the joystick.
        /// </summary>
        public int AxisCount
        {
            get { return axisCount; }
        }
        
        private int axisA;
        /// <summary>
        /// The first axis on the joystick.
        /// </summary>
        public int AxisA
        {
            get { return axisA; }
        }
        
        private int axisB;
        /// <summary>
        /// The second axis on the joystick.
        /// </summary>
        public int AxisB
        {
            get { return axisB; }
        }
        
        private int axisC;
        /// <summary>
        /// The third axis on the joystick.
        /// </summary>
        public int AxisC
        {
            get { return axisC; }
        }
        
        private int axisD;
        /// <summary>
        /// The fourth axis on the joystick.
        /// </summary>
        public int AxisD
        {
            get { return axisD; }
        }
        
        private int axisE;
        /// <summary>
        /// The fifth axis on the joystick.
        /// </summary>
        public int AxisE
        {
            get { return axisE; }
        }
        
        private int axisF;
        /// <summary>
        /// The sixth axis on the joystick.
        /// </summary>
        public int AxisF
        {
            get { return axisF; }
        }
        private IntPtr hWnd;
        private uint updateMessage;

        private bool[] buttons;
        /// <summary>
        /// Array of buttons availiable on the joystick. This also includes PoV hats.
        /// </summary>
        public bool[] Buttons
        {
            get { return buttons; }
        }

        private string[] systemJoysticks;

        /// <summary>
        /// Constructor for the class.
        /// </summary>
        /// <param name="window_handle">Handle of the window which the joystick will be "attached" to.</param>
        /// <param name="update_message">Message which will be sent on updates.</param>
        public Joystick(IntPtr window_handle, uint update_message)
        {
            hWnd = window_handle;
            updateMessage = update_message;
            this.joystickEventWait[0] = this.joystickEvent;
            axisA = -1;
            axisB = -1;
            axisC = -1;
            axisD = -1;
            axisE = -1;
            axisF = -1;
            axisCount = 0;
        }

        private void Poll()
        {
            try
            {
                // poll the joystick
                joystickDevice.Poll();
                // update the joystick state field
                state = joystickDevice.CurrentJoystickState;
            }
            catch (Exception err)
            {
                // we probably lost connection to the joystick
                // was it unplugged or locked by another application?
                Debug.WriteLine("Poll()");
                Debug.WriteLine(err.Message);
                Debug.WriteLine(err.StackTrace);
            }
        }

        /// <summary>
        /// Retrieves a list of joysticks attached to the computer.
        /// </summary>
        /// <example>
        /// [C#]
        /// <code>
        /// JoystickInterface.Joystick jst = new JoystickInterface.Joystick(this.Handle);
        /// string[] sticks = jst.FindJoysticks();
        /// </code>
        /// </example>
        /// <returns>A list of joysticks as an array of strings.</returns>
        public string[] FindJoysticks()
        {
            systemJoysticks = null;

            try
            {
                // Find all the GameControl devices that are attached.
                DeviceList gameControllerList = Manager.GetDevices(DeviceClass.GameControl, EnumDevicesFlags.AttachedOnly);

                // check that we have at least one device.
                if (gameControllerList.Count > 0)
                {
                    systemJoysticks = new string[gameControllerList.Count];
                    int i = 0;
                    // loop through the devices.
                    foreach (DeviceInstance deviceInstance in gameControllerList)
                    {
                        // create a device from this controller so we can retrieve info.
                        joystickDevice = new Device(deviceInstance.InstanceGuid);
                        joystickDevice.SetCooperativeLevel(hWnd,
                            CooperativeLevelFlags.Background |
                            CooperativeLevelFlags.NonExclusive);

                        systemJoysticks[i] = joystickDevice.DeviceInformation.InstanceName;

                        i++;
                    }
                }
            }
            catch (Exception err)
            {
                Debug.WriteLine("FindJoysticks()");
                Debug.WriteLine(err.Message);
                Debug.WriteLine(err.StackTrace);
            }

            return systemJoysticks;
        }

        /// <summary>
        /// Acquire the named joystick. You can find this joystick through the <see cref="FindJoysticks"/> method.
        /// </summary>
        /// <param name="name">Name of the joystick.</param>
        /// <returns>The success of the connection.</returns>
        public bool AcquireJoystick(string name)
        {
            try
            {
                DeviceList gameControllerList = Manager.GetDevices(DeviceClass.GameControl, EnumDevicesFlags.AttachedOnly);
                int i = 0;
                bool found = false;
                // loop through the devices.
                foreach (DeviceInstance deviceInstance in gameControllerList)
                {
                    if (deviceInstance.InstanceName == name)
                    {
                        found = true;
                        // create a device from this controller so we can retrieve info.
                        joystickDevice = new Device(deviceInstance.InstanceGuid);
                        joystickDevice.SetCooperativeLevel(hWnd,
                            CooperativeLevelFlags.Background |
                            CooperativeLevelFlags.NonExclusive);
                        break;
                    }

                    i++;
                }

                if (!found)
                    return false;
                
                // Tell DirectX that this is a Joystick.
                joystickDevice.SetDataFormat(DeviceDataFormat.Joystick);

                joystickDevice.SetEventNotification(this.joystickEvent);
                Thread t = new Thread(new ThreadStart(JoystickListener));
                t.IsBackground = true;
                t.Start();

                // Finally, acquire the device.
                joystickDevice.Acquire();

                // How many axes?
                // Find the capabilities of the joystick
                DeviceCaps cps = joystickDevice.Caps;
                Debug.WriteLine("Joystick Axis: " + cps.NumberAxes);
                Debug.WriteLine("Joystick Buttons: " + cps.NumberButtons);

                axisCount = cps.NumberAxes;
                buttons = new bool[cps.NumberButtons];

                //UpdateStatus();
            }
            catch (Exception err)
            {
                Debug.WriteLine("FindJoysticks()");
                Debug.WriteLine(err.Message);
                Debug.WriteLine(err.StackTrace);
                return false;
            }
//            this.UpdateStatus();
            return true;
        }

        private void JoystickListener()
        {
            while (true)
            {
                WaitHandle.WaitAll(this.joystickEventWait);
                //this.joystickEvent.WaitOne();
                this.state = joystickDevice.CurrentJoystickState;
                byte[] jsButtons = state.GetButtons();
                for (int i = 0; i < this.buttons.Length; i++)
                {
                    bool newState = jsButtons[i] >= 128;
                    if (this.buttons[i] != newState)
                    {
                        PostMessage(hWnd, updateMessage, i, newState ? 1 : 0);
                        this.buttons[i] = newState;
                    }
                }
            }
        }

        /// <summary>
        /// Unaquire a joystick releasing it back to the system.
        /// </summary>
        public void ReleaseJoystick()
        {
            joystickDevice.Unacquire();
        }

        /// <summary>
        /// Update the properties of button and axis positions.
        /// </summary>
        public void UpdateStatus()
        {
            Poll();

            int[] extraAxis = state.GetSlider();
            //Rz Rx X Y Axis1 Axis2
            axisA = state.Rz;
            axisB = state.X;
            axisC = state.Y;
            axisD = state.Rx;
            axisE = extraAxis[0];
            axisF = extraAxis[1];

            // not using buttons, so don't take the tiny amount of time it takes to get/parse
            byte[] jsButtons = state.GetButtons();
//            buttons = new bool[jsButtons.Length];

            int i = 0;
            foreach (byte button in jsButtons)
            {
                buttons[i] = button >= 128;
                i++;
                if (i >= buttons.Length) break;
            }
        }
    }
}
