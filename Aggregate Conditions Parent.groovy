/**
 *  Aggregate Conditions (Parent)
 *  Copyright 2020 Steve Jackson
 *
 *  Changelog:
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
    name: "Aggregate Conditions",
    namespace: "SRJ",
    author: "Steve Jackson",
    description: "Aggregate Multiple Conditional Switches to a Single Virtual Switch",
    category: "My Apps",
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: "")


preferences {
    // The parent app preferences are pretty simple: just use the app input for the child app.
    page(name: "mainPage", title: "", install: true, uninstall: true,submitOnChange: true) {
        section {
            app(name: "aggregateconditions", appName: "Aggregate Conditions Child", namespace: "SRJ", title: "Create New Condition", multiple: true)
            }
    }
}

def installed() {
    log.debug "Installed with settings: ${settings}"
    initialize()
}

def updated() {
    log.debug "Updated with settings: ${settings}"
    unsubscribe()
    initialize()
}

def initialize() {
    // this just logs some messages for information purposes
    log.debug "there are ${childApps.size()} child smartapps"
    childApps.each {child ->
        log.debug "child app: ${child.label}"
    }
}

