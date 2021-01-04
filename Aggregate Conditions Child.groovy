/**

 *  Aggregate Multiple Conditions to One Virtual Switch
 *  Copyright 2020 Steve Jackson
 *
 *  Changelog:
 *	Version 1.2 - 06/04/2020, added parent/child relationship
 *  Version 1.1 - 06/04/2020, added DISABLE switches with state OFF option
 *  Version 1.0 - 06/02/2020, initial create
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is
 *  distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 *  
 */

definition(
    name: "Aggregate Conditions Child",
    namespace: "SRJ",
    author: "Steve Jackson",
    description: "Turns a virtual switch on/off based on the state of several other switches",
    category: "My Apps",
    parent: "SRJ:Aggregate Conditions",
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: ""
)

preferences {
	section("Select Condition Switches (These Switches MUST BE ON For Disable)?"){
		input "disableswitchon", "capability.switch", multiple: true, required: false, title: "Conditional Switches (ON to Disable)?"
	}
    section("Select Condition Switches (These Switches MUST BE OFF For Disable)?"){
		input "disableswitchoff", "capability.switch", multiple: true, required: false, title: "Conditional Switches (OFF to Disable)?"
	}     
    section("Virtual Switch to Control?"){
		input "switchtoturnon", "capability.switch", multiple: true, required: true, title: "Which Switch?"
	}
}

def installed() {
		initialize()
}

def updated() {
		unsubscribe()
		initialize() 
}

//subscribing to the events that our app will monitor
def initialize() {                                      
    	subscribe(disableswitchon, "switch", switchHandler)
        subscribe(disableswitchoff, "switch", switchHandler)
}

//section handles when a BYPASS switches state has changed
def switchHandler(evt) {                    
        def switcheson = false              //sets variable to false
        def switchesoff = false             //sets variable to false
        disableswitchon.each { switcheson = switcheson || it.currentSwitch == "on" }       //any user DISABLE off switches on?
        disableswitchoff.each { switchesoff = switchesoff || it.currentSwitch == "off" }   //any user DISABLE on switches off?
        
        if ((switcheson) || (switchesoff)) {   // see if switcheson OR switchesoff has been set
            controlledswitcheson() }           //at least one DISABLE switch is ACTIVE so turn on the virtual switch
            else 
               {controlledswitchesoff()}       //all DISABLE switches are INACTIVE so turn off the virtual switch
}                

def controlledswitchesoff() {
        switchtoturnon.off()          //turn off virtual switch
}

def controlledswitcheson() {
        switchtoturnon.on()          //turn on virtual switch
}
