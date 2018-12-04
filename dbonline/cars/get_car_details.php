<?php
header('Content-Type: application/json');
 
$response = array();

require_once '../conf/db_connect.php';

$con = new DB_CONNECT();
$con = $con->get_connect();
	

if (isset($_GET["car_id"])) {
	$car_id = $_GET['car_id'];
 
	$result = $con->query("SELECT * FROM cars WHERE car_id = $car_id");
 
	if (!empty($result)) {
		if ($result->num_rows > 0) {
 
			$result = mysqli_fetch_array($result);
 
			$car = array();
			$car["car_id"] = $result["car_id"];
			$car["car_name"] = $result["car_name"];
			$car["car_price"] = $result["car_price"];

			$response["success"] = 1;
 
			$response["car"] = array();
 
			array_push($response["car"], $car);
 
			echo json_encode($response);
		} else {
			// no product found
			$response["success"] = 0;
			$response["message"] = "No car found";
			
			echo json_encode($response);
		}
	} else {
		$response["success"] = 0;
		$response["message"] = "No car found";
		
		echo json_encode($response);
	}
} else {
	$response["success"] = 0;
	$response["message"] = "Required field(s) is missing";
 
	echo json_encode($response);
}
?>