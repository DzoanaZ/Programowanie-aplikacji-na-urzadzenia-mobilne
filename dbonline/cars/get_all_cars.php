<?php
header('Content-Type: application/json');

$response = array();

require_once '../conf/db_connect.php';
$con = new DB_CONNECT();
$con = $con->get_connect();

$result = $con->query("SELECT * FROM cars");

if($con->error) {
	die('Unable to read row from DB [' . $con->error . ']');
}

if($result->num_rows > 0) {
	$response["cars"] = array();
	
	while ($row = mysqli_fetch_array($result)) {
        $car = array();
        $car["car_id"] = $row["car_id"];
        $car["car_name"] = $row["car_name"];
		$car["car_price"] = $row["car_price"];

        array_push($response["cars"], $car);
    }
	
	$response["success"] = 1;
	
	echo json_encode($response);
} else {
	$response["success"] = 0;
    $response["message"] = "No products found";
 
    echo json_encode($response);
}

?>