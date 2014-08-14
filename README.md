The objective of the application is to provide an SDK for developers which enables them to toggle and modify settings so as to best customize the use of the Rithmio library to the solution or product they are providing.

- It is important to note that however this may be similar to the current use-case (or not, depending on how far down the road this README is read), the actual front end may be developed completely different, and these options - as well as some unused code - serve more as use-examples and stepping stones for non-Rithmio developers.

This application, on the front end, allows the user to:
- Switch between activating supervised and unsupervised learning (not integrated into the library as of yet)
- Switch between two fragments which contain lists of the learned rithms; one for rithms to be recognized, one for rithms to be deleted or have their names edited

On the back end, it allows for developers to:
- Choose between which sensors get used (currently only Accelerometer and Gyroscope)
- Which axis of the sensor is turned on (x, y, or z)
- Choose the Sensor Delay speed (frequency of sensor readings - Normal, UI, Game, Fastest - see online for numbers on a specific device)
- Record sensor readings to file (readings taken with timestamp, written to CSV file on device's external - most likely SD - drive)
Note: sensor accuracy cannot be changed, but can be read with an accuracyReaderMethod with parameter (int accuracy), ranging from 0 (unreliable accuracy) to 3 (high accuracy).

Current use:
- User powers on application
	- Variables are initialized
	- Sensors are started at a specific Delay setting
	- If Developer wants to switch Sensor Delay settings during runtime, use registerSensors, otherwise startSampling
	- Sensors are registered
	- Sensor values are gathered
	- File in external directory is opened
	- Sensor values are sent to file
	- File is closed on app close/switch
- Features back-end
	- Toggling POWER of Accelerometer and Gyroscope sensors
	- Toggling POWER of Accelerometer and Gyroscope axis
	- Switching between Sensor Delay frequencies

Current code functionality:
- User powers on application
	- Functionality variables are initialized
		- Sensor Manager initializer variable
		- Accelerometer and Gyroscope (A&G) initializer variable
		- Current A&G Sensor Delay switch variable
		- A&G POWER ON booleans
		- A&G axis POWER ON booleans
		- Data-to-CSV writer POWER ON boolean
		- CSV file writer
	- GUI variables are initialized
		- TextView for each A&G axis for display on UI
		- Fragment call-and-switch variable
		- Fragment call button and ImageView initialization variables
		- Variable for Rithmio Logo image
		- Rithmio logo animation rotation quantities (360 degrees per second counterclockwise)
- onCreate
	- Layout main.xml is set as front page
		- Contains Fragment R.id.fragment_switch
			- Calls Fragment BlankFragment()
			- Inflates R.layout.blank to front (as name suggests, fragment and layout are blank - no dimensions)
	- SensorManager, Accelerometer, and Gyroscope are initialized and assigned to variables
	- Current Sensor Delay is set to Normal Delay (slowest)
	- Toasts if a sensor is missing
	- Initializes front-end (fragment switch buttons and Rithmio logo ImageView)
	- Starts Rithmio logo spinning animation
	- Turns on Accelerometer and Gyroscope sensors (currently for DEBUGGING purposes)
- Back-end functionality
	- onResume called when application resumes
	- onPause called when application is paused, destroyed, or sent to background
		- Stops listening for and writing sensor data, closes CSV file
		- Unregisters listeners
	- onAccuracyChanged
	- onSensorChanged
		- Gets sensor axis values (event.values[x])
		- Sends to front end for display - frontEndAxisGyro/Accel
		- Checks if CSV file is open, then sends the values to it - processSample()
	- processSample
		- Writes the accelerometer and gyroscope sensor values (if sensor and axis are active) to a CSV file (sensorDataFile) which is created whenever the application opens (when startSampling is run).
	- startSampling
		- For developer use, if Sensor Delay is not changed during runtime, registerSensors is not used, and the A&E sensors with a dev-specified Sensor Delay setting are registered.
		- File name is set to Rithmio_currentDate.csv
		- File is created and opened in an external storage directory
		- isSamplingOn is set to true so that onSensorChanged values are written to file
	- stopSampling	
		- Called when on onPause
		- Unregisters listeners, closes data file, sets isSamplingOn to false so no more sensor readings are gathered (mostly for use if the application is restarted - memory usage, etc)
	- registerSensors
		- Used for switching Sensor Delay frequencies
		- Unregisters all sensor listeners
		- Registers the active sensors (A&G) with the new Delay setting
	- accelerometerPower / gyroscopePower
		- onClick called from View
		- Toggles Accelerometer / Gyroscope power on and off
		- Calls registerSensors. Can be used interchangeably with stopSampling/startSampling
	- sensorDelayChange
		- onClick called from View
		- Switch between different the four Sensor Delay settings for either Accelerometer or Gyroscope
		- Calls registerSensors. Cannot be used interchangeably with stopSampling/StartSampling, as sensors need to be unregistered before new Sensor Delay values are registered
	- checkBoxAxis
		- onClick called from View
		- Switch toggles the visibility of 
- Front-end GUI - modifiable/not necessary components of the code
	- frontEndAxisGyro
		- Retrieves Gyroscope axis sensor data to be displayed
	- frontEndAxisAccel
		- Retrieves Accelerometer axis sensor data to be displayed
	- selectFragment
		- Selects between four fragments to be displayed on the main activity screen
			- Edit Rithm Names
			- Recognize Rithms
			- Learn Supervised Activities
			- Learn Unsupervised Activities
	- sensorCheckToaster
		- Toasts to indicate whether device currently has requrired sensors
	- frontEndInitialization
		- Assigns variables to buttons and ImageViews
	- logoAnimation
		- Assigns an animation to the Rithmio Logo being displayed on the screen - one rotation per second counterclockwise
	- logoClick 
		- Creates a link from Rithmio Logo to Rithmio website (www.rithmio.com)




NOTES:
- Fragments are not essential component of code, so explanation is being left out (this includes all java files in this package that are not MainActivity.java)
- rithmArray is static for the purposes of having it appear on the recognition fragment. Can be changed

To Do:
- Create checkboxes in recognition fragment to choose which rithms to recognize from
- Keyboard input to change the Rithm Name. Make sure it changes the name on both EditRithmNameFragment and RecognitionFragment
















