<?php
header('Content-Type: application/json');
$response = array();

if (isset($_POST['car_id']) && isset($_POST['car_name']) && isset($_POST['car_price'])) {
	
	$car_id = $_POST['car_id'];
    $car_name = $_POST['car_name'];
    $car_price = $_POST['car_price'];
	
	require_once '../conf/db_connect.php';
	$con = new DB_CONNECT();
	$con = $con->get_connect();

	$result = $con->query("UPDATE cars SET car_name = '$car_name', car_price = '$car_price' WHERE car_id = $car_id");

	
	if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Car successfully updated.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing";
 
		echo json_encode($response);
	}
} else {
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    echo json_encode($response);
}

?>