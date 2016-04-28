Crumble-api
-----------
//—------------------------------------------------------------------------------
// Function Name: getCrumbContent
// Description: Get the content of a single Crumb based on a note_id
// URL: http://uaf132701.ddns.uark.edu/api/crumb/<id:integer>
// Method: GET
// Returns: JSON Object:
//    if success:
//        {
//         “status”:”FOUND”,
//         “data”: “{crumb object}”
//        }
//    if not success:
//        {
//         “status”:”NOT-FOUND”
//        }
//-------------------------------------------------------------------------------

//-------------------------------------------------------------------------------
// Function Name: getAllCrumbs
// Description: Returns all crumbs in the database, but only their basic
//     information such as, title, bites, rating, etc.
// URL: http://uaf132701.ddns.uark.edu/api/crumb/all
// Method: GET
// Returns: JSON Object:
//    if success:
//        {
//         “status”:”FOUND”,
//         “data[]”: “{crumb objects}”
//        }
//    if not success:
//        {
//         “status”:”NOT-FOUND”
//        }
//
// Notes
// -----
// The crumbs returned are only partial crumbs that do not include creator_id or
// text fields.
//-------------------------------------------------------------------------------

//—------------------------------------------------------------------------------
// Function Name: addCrumb
// Description: Add a crumb to the database
// URL: http://uaf132701.ddns.uark.edu/api/crumb/add
// Method: POST
// Payload: Raw POST Request, fields are:
//    1. creator_id
//    2. title
//    2. latitude
//    3. longitude
//    4. message
//-------------------------------------------------------------------------------

//-------------------------------------------------------------------------------
// Function Name: editCrumb   
// Description: Edit crumb information. (Title, text)
// URL: http://uaf132701.ddns.uark.edu/api/crumb/edit
// Method: POST
// Payload: JSON Crumb Object: Required* Fields are:
//    1. crumb_id
//    2. title
//    4. message
//
// *These 3 fields are required, however, more can be submitted, so you can 
//  simply parse a crumb object as a JSON, and send it on up as long as it
//  has these three fields at least.
//-------------------------------------------------------------------------------

//-------------------------------------------------------------------------------
// Function Name: addUser
// Description: Add a user
// URL: http://uaf132701.ddns.uark.edu/api/user/add
// Method: POST
// Payload: JSON User Object: Required Fields are:
//    1. user_name
//    2. first_name
//    3. last_name
//    4. email
//    5. password
// Returns: JSON Object:
//    if success:
//        {
//         “status”:”OK”,
//         “data”: “{User object}”
//        }
//    if not success:
//        {
//         “status”:”ERROR”
//        }
//-------------------------------------------------------------------------------

//-------------------------------------------------------------------------------
// Function Name: findCrumb
// Description: Increments the "total_discovered" field of the specified crumb
//      and inserts a row in the Discovered table for the specified user_id
// URL: http://uaf132701.ddns.uark.edu/api/crumb/find/<crumb_id>/<user_id>
// Method: GET
// Returns: JSON Object:
//    if success:
//        {
//         “status” : ”OK”/"ALREADY-FOUND",
//         “data”   : “{crumb object}”
//        }
//    if not success:
//        {
//         “status” : ”ERROR"
//        }
//-------------------------------------------------------------------------------

//-------------------------------------------------------------------------------
// Function Name: getUserCreatedCrumbs
// Description: Get all of the crumbs, and their contents, that are created by
//              a specified user (user_id).
// URL: http://uaf132701.ddns.uark.edu/api/user/get/createdCrumbs
// Method: GET
// Returns: JSON Object:
//    if success:
//        {
//         “status”:”FOUND”,
//         “data”: “{crumb objects}”
//        }
//    if not success:
//        {
//         “status”:”NOT-FOUND”
//        }
// 
// HTTP Status Codes:
//  Success: 200 (Success)
//  User Not Found: 404 (Not Found)
//   
//-------------------------------------------------------------------------------

//-------------------------------------------------------------------------------
// Function Name: getUserDiscoveredCrumbs
// Description: Get all of the crumbs, and their contents, that have been
//              discovered by a certain user.
// URL: http://uaf132701.ddns.uark.edu/api/user/get/discoveredCrumbs
// Method: GET
// Returns: JSON Object:
//    if success:
//        {
//         “status”:”FOUND”,
//         “data”: “{crumb objects}”
//        }
//    if not success:
//        {
//         “status”:”NOT-FOUND”
//        }
// 
// HTTP Status Codes:
//  Success: 200 (Success)
//  No Cumbs Found: 404 (Not Found)
//   
//-------------------------------------------------------------------------------

//-------------------------------------------------------------------------------
// Function Name: getLogbook
// Description: Get all of the logbook entries for a specific user_id.
// URL: http://uaf132701.ddns.uark.edu/api/user/logbook/<id>
// Method: GET
// Returns: JSON Object:
//    if success:
//        {
//         “status”:”FOUND”,
//         “data”: “{logbook entries}”
//        }
//    if not success:
//        {
//         “status”:”NOT-FOUND”
//        }
// 
// HTTP Status Codes:
//  Success: 200 (Success)
//  User Not Found: 404 (Not Found)
//   
//-------------------------------------------------------------------------------

//-------------------------------------------------------------------------------
// Function Name: addLogEntry
// Description: Add a crumb to the database
// URL: http://uaf132701.ddns.uark.edu/api/crumb/add
// Method: POST
// Payload: JSON Crumb Object: Fields are:
//    1. user_id
//    2. content
//
// Returns: JSON Object:
//    if success:
//        {
//         “status”:”OK”,
//         “data”: “{logbook entry}”
//        }
//    if not success:
//        {
//         “status”:”ERROR”
//        }
// 
// HTTP Status Codes:
//  Success: 201 (Created)
//  User Not Found: 409 (Conflict)
//   
//-------------------------------------------------------------------------------

//-------------------------------------------------------------------------------
// Function Name: login
// Description: Login a user and return the user_id
// URL: http://uaf132701.ddns.uark.edu/api/user/login
// Method: POST
// Returns: JSON Object:
//    if success:
//        {
//         “status”:”FOUND”,
//         “data”: “{user_id}”
//        }
//    if not success:
//        {
//         “status”:”NOT-FOUND”
//        }
// 
// HTTP Status Codes:
//  Success: 200 (Success)
//  User Not Found: 404 (Not Found)
//   
//-------------------------------------------------------------------------------


