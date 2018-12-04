<?php
 header('Content-Type: application/json');

$response = array();


if (isset($_POST['car_name']) && isset($_POST['car_price'])) {
 
    $car_name = $_POST['car_name'];
    $car_price = $_POST['car_price'];
  
    require_once '../conf/db_connect.php';

	$con = new DB_CONNECT();
	$con = $con->get_connect();

 
    $result = $con->query("INSERT INTO cars(car_name, car_price) VALUES('$car_name', '$car_price')");
 
    if ($result) {

        $response['success'] = 1;
        $response['message'] = 'Car successfully created';
 
        echo json_encode($response);
    } else {
        $response['success'] = 0;
        $response['message'] = 'Oops! An error occurred.';
 
        echo json_encode($response);
    }
	
} else {
    $response['success'] = 0;
    $response['message'] = 'Required field(s) is missing';

    echo json_encode($response);
}
?>