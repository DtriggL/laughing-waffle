<?php

use Phalcon\Mvc\Micro;
use Phalcon\Loader;
use Phalcon\Di\FactoryDefault;
use Phalcon\Db\Adapter\Pdo\Mysql as PdoMysql;
use Phalcon\Http\Response;

// Use Loader() to autoload our model
$loader = new Loader();
$loader->registerDirs(
    array(
        __DIR__ . '/models/'
    )
)->register();

$di = new FactoryDefault();

// Set up the database service
$di->set('db', function () {
    return new PdoMysql(
        array(
            "host"     => "127.0.0.1",
            "username" => "root",
            "password" => "Eezohng7",
            "dbname"   => "proj"
        )
    );
});

// Create and bind the DI to the application
$app = new Micro($di);


//-------------------------------------------------------------------------------
// Function Name: addCrumb
// Description: Add a crumb to the database
// URL: http://uaf132701.ddns.uark.edu/api/crumb/add
// Method: POST
// Payload: JSON Crumb Object: Fields are:
//    1. creator_id
//    2. title
//    2. latitude
//    3. longitude
//    4. messafe
//-------------------------------------------------------------------------------
$app->post('/api/crumb/add', function () use ($app) {
    // Get the raw POST content
    $item->title = $app->request->getPost("title");
    $item->message = $app->request->getPost("message");
    $item->latitude = $app->request->getPost("latitude");
    $item->longitude = $app->request->getPost("longitude");
    $item->creator_id = $app->request->getPost("creator_id");

    $phql = "INSERT INTO Crumb (creator_id, latitude, longitude, message, title) VALUES (:creator_id:, :latitude:, :longitude:, :message:, :title:)";
    // Get a collection of users that meet the criteria
    $status = $app->modelsManager->executeQuery($phql, array(
        'creator_id' => $item->creator_id,
        'latitude' => $item->latitude,
        'longitude' => $item->longitude,
        'title' => $item->title,
        'message' => $item->message
    ));

    // Create a response
    $response = new Response();

    if ($status->success() == true) {
        // Change the HTTP status
        $response->setStatusCode(201, "Created");

        $item->crumb_id = $status->getModel()->crumb_id;

        $response->setJsonContent(
            array(
                'status' => 'OK',
                'data'   => $item
            )
        );

    } else {
        // Change the HTTP status
        $response->setStatusCode(409, "Conflict");

        // Send errors to the client
        $errors = array();
        foreach ($status->getMessages() as $message) {
            $errors[] = $message->getMessage();
        }

        $response->setJsonContent(
            array(
                'status'   => 'ERROR',
                'messages' => $errors
            )
        );
    }

    return $response;
});

//-------------------------------------------------------------------------------
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
$app->get('/api/crumb/{id:[0-9]+}', function ($id) use ($app) {
	$phql = "SELECT * FROM Crumb WHERE crumb_id = :id:";

	//Get the list that matches the given list id
	$crumbs = $app->modelsManager->executeQuery($phql, array(
			'id' => $id
	));

	//Create a response to send back to the client
	$response = new Response();
	$crumb = $crumbs->getFirst();

	if($crumb == false) {
	    $response->setJsonContent(
	    array(
	        'status' => 'NOT-FOUND')
	    );
	}
	else {
        /* Here is where we would loop through the results and build
           the JSON objects*/
        $response->setJsonContent(
           array(
                'status' => 'FOUND',
                'data' => array(
                    'crumb_id' => $crumb->crumb_id,
                    'creator_id' => $crumb->creator_id,
                    'latitude' => $crumb->latitude,
                    'longitude' => $crumb->longitude,
                    'title' => $crumb->title,
                    'message' => $crumb->message
                    )
            )
        );
    }

    return $response;
});

//-----------------------------------------------------------------------------
// Function Name: editCrumb
// Description: Edit crumb information. (Title, text)
// URL: http://uaf132701.ddns.uark.edu/api/crumb/edit
// Method: POST
//-----------------------------------------------------------------------------
$app->post('/api/crumb/edit', function () use ($app) {
    // Get the raw JSON content
    $crumb = $app->request->getJsonRawBody();

    $phql = "UPDATE Crumb SET title = :title:, message = :message: WHERE crumb_id = :id:";
    $status = $app->modelsManager->executeQuery($phql, array(
        'id' => $crumb->crumb_id,
        'title' => $crumb->title,
        'message' => $crumb->message
    ));

    // Create a response
    $response = new Response();

    if ($status->success() == true) {
        // Change the HTTP status
        $response->setStatusCode(201, "Created");

        $response->setJsonContent(
            array(
                'status' => 'OK',
                'data'   => $crumb
            )
        );

    } else {
        // Change the HTTP status
        $response->setStatusCode(409, "Conflict");

        // Send errors to the client
        $errors = array();
        foreach ($status->getMessages() as $message) {
            $errors[] = $message->getMessage();
        }

        $response->setJsonContent(
            array(
                'status'   => 'ERROR',
                'messages' => $errors
            )
        );
    }

    return $response;
});

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
$app->post('/api/user/add', function () use ($app) {
    // Get the raw JSON content
    $user = $app->request->getJsonRawBody();

    $phql = "INSERT INTO User (user_name, first_name, last_name, email, password) VALUES (:user_name:, :first_name:, :last_name:, :email:, :password:)";
    // Get a collection of users that meet the criteria
    $status = $app->modelsManager->executeQuery($phql, array(
        'user_name' => $user->user_name,
        'first_name' => $user->first_name,
        'last_name' => $user->last_name,
        'email' => $user->email,
        'password' => $user->password
    ));

    // Create a response
    $response = new Response();

    if ($status->success() == true) {
        // Change the HTTP status
        $response->setStatusCode(201, "Created");

        $user->user_id = $status->getModel()->user_id;

        $response->setJsonContent(
            array(
                'status' => 'OK',
                'data'   => $user
            )
        );

    } else {
        // Change the HTTP status
        $response->setStatusCode(409, "Conflict");

        // Send errors to the client
        $errors = array();
        foreach ($status->getMessages() as $message) {
            $errors[] = $message->getMessage();
        }

        $response->setJsonContent(
            array(
                'status'   => 'ERROR',
                'messages' => $errors
            )
        );
    }

    return $response;
});

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
$app->get('/api/crumb/all', function () use ($app) {
	$phql = "SELECT * FROM Crumb";

	//Get the list that matches the given list id
	$crumbs = $app->modelsManager->executeQuery($phql);
    
	//Create a response to send back to the client
	$response = new Response();

	if($crumbs == false) {
	    $response->setJsonContent(
	    array(
	        'status' => 'NOT-FOUND')
	    );
	}
	else {
        /* Here is where we would loop through the results and build
           the JSON objects*/
        $data = array();
        foreach ($crumbs as $crumb) {
               $data[] = array(
                   'crumb_id' => $crumb->crumb_id,
                   'latitude' => $crumb->latitude,
                   'longitude' => $crumb->longitude,
                   'title' => $crumb->title,
                   'total_discovered' => $crumb->total_discovered,
                   'rating' => $crumb->rating
               ); 
        }
        $response->setJsonContent(
            array(
	            'status' => 'FOUND',
	            'data' => $data
            )
        );
    }

    return $response;
});

//-------------------------------------------------------------------------------
// Function Name: findCrumb
// Description: Increments the "total_discovered" field of the specified crumb.
// URL: http://uaf132701.ddns.uark.edu/api/crumb/find/<id>
// Method: GET
// Returns: JSON Object:
//    if success:
//        {
//         “status” : ”FOUND”,
//         “data”   : “{crumb object}”
//        }
//    if not success:
//        {
//         “status” : ”NOT-FOUND”
//        }
//-------------------------------------------------------------------------------
$app->get('/api/crumb/find/{id:[0-9]+}', function ($id) use ($app) {
	$phql = "SELECT * FROM Crumb WHERE crumb_id = :id:";

	//Get the list that matches the given list id
	$crumbs = $app->modelsManager->executeQuery($phql, array(
			'id' => $id
	));
    
	//Create a response to send back to the client
	$response = new Response();

	if($crumbs == false) {
	    $response->setStatusCode(409, 'Conflict');
        $response->setJsonContent(
           array(
               'status'   => 'ERROR',
               'messages' => $errors
           )
        );
	}
	else {
        // We found one!
        $crumb = $crumbs->getFirst();
        // So add one to it's total_discovered field
        $crumb->total_discovered = $crumb->total_discovered + 1;
        // Update the value in the database
        $phql = "UPDATE Crumb SET total_discovered = :bites: WHERE crumb_id = :id:";
        $status = $app->modelsManager->executeQuery($phql, 
            array(
                'id' => $id,
                'bites' => $crumb->total_discovered
	        )
        );
        
        if ($status->success() == true) {
            // Change the HTTP status
            $response->setStatusCode(201, "Created");

            $response->setJsonContent(
                array(
                    'status' => 'OK',
                    'data'   => $crumb
                )
            );
        } else {
            $response->setStatusCode(409, 'Conflict');
            $response->setJsonContent(
	           array(
	               'status'   => 'ERROR',
                   'messages' => $errors
               )
	       );
        }
    }

    return $response;
});

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
$app->get('/api/user/get/createdCrumbs/{id:[0-9]+}', function ($id) use ($app) {
	$phql = "SELECT * FROM Crumb WHERE creator_id = :id:";

	//Get the list that matches the given list id
	$crumbs = $app->modelsManager->executeQuery($phql, array(
			'id' => $id
	));

	//Create a response to send back to the client
	$response = new Response();

	if($crumbs == false) {
        $response->setStatusCode(404, "Not Found");
	    $response->setJsonContent(
	    array(
	        'status' => 'NOT-FOUND')
	    );
	}
	else {
        /* Here is where we would loop through the results and build
           the JSON objects*/
        $data = array();
        foreach ($crumbs as $crumb) {
               $data[] = array(
                   'crumb_id' => $crumb->crumb_id,
                   'latitude' => $crumb->latitude,
                   'longitude' => $crumb->longitude,
                   'title' => $crumb->title,
                   'total_discovered' => $crumb->total_discovered,
                   'rating' => $crumb->rating
               ); 
        }
        $response->setStatusCode(200, "Success");
        $response->setJsonContent(
            array(
	            'status' => 'FOUND',
	            'data' => $data
            )
        );
    }

    return $response;
});


/* Handle a request when the file/function they requested is not 
available.*/
$app->notFound(function () use ($app) {
    $app->response->setStatusCode(404, "Not Found")->sendHeaders();
    echo 'This is crazy, but this page was not found!';
    echo 'I know this made your night you poor developer...';
});

$app->handle();

$app->get('/', function () {
    throw new \Exception("An error");
});

$app->error(
    function ($exception) {
        echo "An error has occurred";
    }
);


