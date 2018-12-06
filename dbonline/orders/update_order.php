<?php
header('Content-Type: application/json');
$response = array();

if (isset($_POST['order_id']) && isset($_POST['start_date']) && isset($_POST['end_date']) && isset($_POST['user_id']) && isset($_POST['car_id'])) {
	
	$order_id = $_POST['order_id'];
    $start_date = $_POST['start_date'];
    $end_date = $_POST['end_date'];
	$user_id = $_POST['user_id'];
	$car_id = $_POST['car_id'];
	
	require_once '../conf/db_connect.php';
	$con = new DB_CONNECT();
	$con = $con->get_connect();

	$result = $con->query("UPDATE orders SET start_date = '$start_date', end_date = '$end_date', user_id = '$user_id', car_id = '$car_id'  WHERE order_id = $order_id");

	
	if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Order successfully updated.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing.";
 
		echo json_encode($response);
	}
} else {
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing.";
 
    echo json_encode($response);
}

?>