<?php
 header('Content-Type: application/json');
$response = array();

if (isset($_POST['order_id'])) {
    $order_id = $_POST['order_id'];
 

    require_once '../conf/db_connect.php';
 
    // connecting to db
    $con = new DB_CONNECT();
	$con = $con->get_connect();
 
    // mysql update row with matched pid
    $result = $con->query("DELETE FROM orders WHERE order_id = $order_id");
 
    if (mysqli_affected_rows($con) > 0) {
        $response["success"] = 1;
        $response["message"] = "Car successfully deleted";
 
        echo json_encode($response);
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