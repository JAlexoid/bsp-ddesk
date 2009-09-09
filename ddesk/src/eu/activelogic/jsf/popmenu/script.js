/*
 *    Copyright 2006 Baltijos Sprendimai (http://www.bsprendimai.lt/)
 *              Authorship: Aleksandr Panzin (http://www.activelogic.eu/)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

var _popmenu_timeoutId = 0;
function _popmenu_showHelper(element){
   document.getElementById(element).style.display = "block";
}
function _popmenu_hideHelper(element){
      _popmenu_timeoutId = window.setTimeout("_popmenu_hideHelperReal('"+element+"')",250);
}
function _popmenu_hideHelperReal(element){
      document.getElementById(element).style.display = "none";
}
function _popmenu_stayAliveHelper(element){
      document.getElementById(element).style.display = "block";
      window.clearTimeout(_popmenu_timeoutId);
}