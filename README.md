The objective of the application is to provide an SDK for developers which enables them to toggle and modify settings so as to best customize the use of the Rithmio library to the solution or product they are providing.

- It is important to note that however this may be similar to the current use-case (or not, depending on how far down the road this README is read), the actual front end may be developed completely different, and these options - as well as some unused code - serve more as use-examples and stepping stones for non-Rithmio developers.

This application, on the front end, allows the user to:
- Switch between activating supervised and unsupervised learning (not integrated into the library as of yet)
- Switch between two fragments which contain lists of the learned rithms; one for rithms to be recognized, one for rithms to be deleted or have their names edited (not complete - the Rithm Edit fragment only shows a layout-implemented list, and the recognition page is yet to be created)

On the back end, it allows for developers to:
- Choose between which sensors get used (currently only Accelerometer and Gyroscope)
- Which axis of the sensor is turned on (x, y, or z)
- Choose the Sensor Delay speed (frequency of sensor readings - Normal, UI, Game, Fastest - see online for numbers on a specific device)
- Record sensor readings to file (readings taken with timestamp, written to CSV file on device's external - most likely SD - drive)
Note: sensor accuracy cannot be changed, but can be read with an accuracyReaderMethod with parameter (int accuracy), ranging from 0 (unreliable accuracy) to 3 (high accuracy).

Current use:

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






rithmArray is static for the purposes of having it appear on the recognition fragment. can be changed

create checkboxes in recognition fragment to choose which to recognize from

// TODO Keyboard input to change the Rithm Name. Make sure it changes the
	// name on both EditRithmFragment and RecognitionFragment
















