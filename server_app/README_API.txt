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

//—------------------------------------------------------------------------------
// (TENTATIVE)
// Function Name: getAllCrumbs
// Description: Returns all crumbs in the database, but only their basic
//     information such as, title, bites, rating, etc.
// URL: http://uaf132701.ddns.uark.edu/api/all-crumbs
// Method: GET
//-------------------------------------------------------------------------------

//—------------------------------------------------------------------------------
// Function Name: addCrumb
// Description: Add a crumb to the database
// URL: http://uaf132701.ddns.uark.edu/api/crumb/add
// Method: POST
// Payload: JSON Crumb Object: Fields are:
//    1. user_id
//    2. title
//    2. latitude
//    3. longitude
//    4. text
//-------------------------------------------------------------------------------

//-----------------------------------------------------------------------------$
// Function Name: editCrumb   
// Description: Edit crumb information. (Title, text)
// URL: http://uaf132701.ddns.uark.edu/api/crumb/edit
// Method: POST
// Payload: JSON Crumb Object: Required* Fields are:
//    1. note_id
//    2. title
//    4. text
//
// *These 3 fields are required, however, more can be submitted, so you can 
//  simply parse a crumb object as a JSON, and send it on up as long as it
//  has these three fields at least.
//-----------------------------------------------------------------------------$