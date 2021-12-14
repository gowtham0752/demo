Cordova Plugin for android and iOS

Follow -> https://stackoverflow.com/questions/40375555/start-android-activity-from-cordova-plugin

To Create Cordova Project :

1)Install Node.js. —> https://nodejs.org/en/download/

2)Open terminal and install —> npm install -g cordova (or) sudo npm install -g cordova
    If any permission related issue try install —> sudo npm install -g cordova --unsafe-perm node-sass

3)Check the version after installed —> cordova -v

4)Set path in Mac——>touch ~/.bash_profile; open ~/.bash_profile   (or)  touch ~/.zshrc; open ~/.zshrc

5)Past the below #PATH VARIABLES and enter command + save and command +q

6)Now check the path in terminal like—> echo $ANDROID_HOME

7)Create a project in terminal—> cordova create cordovaDemo

8)Navigate to project folder —>cd cordovaDemo

9)Create platform in terminal —>cordova platform add ios android

10)Then make build —>cordova build

11)Close the terminal and Open android studio and install plugin —> PhoneGap Cordova Plugin

12)Create configuration for Cordova to run the app in android studio

13)Finally run the app


Note:
1. Download java jdk 1.8   —>  https://www.oracle.com/java/technologies/downloads/
2. Download android Gradle file (complete) —> https://gradle.org/releases/
3. Download Android Studio ——> https://developer.android.com/studio?gclid=EAIaIQobChMIvsb7gMqw9AIVFwaRCh3JzAw7EAAYASAAEgKMYvD_BwE&gclsrc=aw.ds
4. Must set above mentioned paths in terminal


#PATH VARIABLES

#ANDROID SDK PATH SETUP
export ANDROID_HOME=/Users/gowtham/Library/Android/sdk
export ANDROID_SDK_ROOT=/Users/gowtham/Library/Android/sdk

#JAVA JDK PATH SETUP
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_311.jdk/Contents/Home
export PATH=$JAVA_HOME/bin:$PATH

#ANDROID GRADLE PATH
export PATH=$PATH:/Users/gowtham/Library/AndroidGradle/gradle-6.7.1/bin



To Create plugin for Cordova Project:

1)Install Node.js. —> https://nodejs.org/en/download/ (If not installed already)

2)Open terminal and install —> npm install -g plugman (or) sudo npm install -g plugman

3)Create cordova plugin using plugman  like —> plugman create --name PluginName --plugin_id com.example.sample.plugin --plugin_version 0.0.1

4)Navigate to project folder —>cd PluginName

5)Add android platform to plugin ——> plugman platform add --platform_name android

6)Add iOS platform to plugin ——> plugman platform add --platform_name ios 

7)Create Package json file ——>plugman createpackagejson "path of your PluginName" (or) sudo plugman createpackagejson "path of your PluginName"

8)You can see the package name, version , etc., upto json file while clicking enter in terminal after package json created.

9)Now navigate to CordovaProject in terminal and add the created plugin to your project like —> cordova plugin add your-plugin-local-path
    E.g : cordova plugin add "C:\PluginName"

10)Now plugin successfully added for both android and iOS platform.









