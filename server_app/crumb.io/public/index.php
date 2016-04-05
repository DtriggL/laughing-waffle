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
$app->post('/api/crumb/add', function () use ($app) {
    // Get the raw JSON content
    $item = $app->request->getJsonRawBody();

    $phql = "INSERT INTO Crumb (user_id, latitude, longitude, text, title) VALUES (:user_id:, :latitude:, :longitude:, :text:, :title:)";
    // Get a collection of users that meet the criteria
    $status = $app->modelsManager->executeQuery($phql, array(
        'user_id' => $item->user_id,
        'latitude' => $item->latitude,
        'longitude' => $item->longitude,
        'title' => $item->title,
        'text' => $item->text
    ));

    // Create a response
    $response = new Response();

    if ($status->success() == true) {
        // Change the HTTP status
        $response->setStatusCode(201, "Created");

        $item->note_id = $status->getModel()->note_id;

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
$app->get('/api/crumb/{id:[0-9]+}', function ($id) use ($app) {
	$phql = "SELECT * FROM Crumb WHERE note_id = :id:";

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
                    'note_id' => $crumb->user_id,
                    'user_id' => $crumb->user_id,
                    'latitude' => $crumb->latitude,
                    'longitude' => $crumb->longitude,
                    'title' => $crumb->title,
                    'text' => $crumb->text
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

    $phql = "UPDATE Crumb SET title = :title:, text = :text: WHERE note_id = :id:";
    $status = $app->modelsManager->executeQuery($phql, array(
        'id' => $crumb->note_id,
        'title' => $crumb->title,
        'text' => $crumb->text
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

