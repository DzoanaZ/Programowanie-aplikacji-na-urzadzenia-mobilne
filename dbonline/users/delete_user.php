<?php
 header('Content-Type: application/json');
$response = array();

if (isset($_POST['user_id'])) {
    $user_id = $_POST['user_id'];
 

    require_once '../conf/db_connect.php';
 
    // connecting to db
    $con = new DB_CONNECT();
	$con = $con->get_connect();
 
    // mysql update row with matched pid
    $result = $con->query("DELETE FROM users WHERE user_id = $user_id");
 
    if (mysqli_affected_rows($con) > 0) {
        $response["success"] = 1;
        $response["message"] = "User successfully deleted";
 
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "No user found";
 
        echo json_encode($response);
    }
} else {
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    echo json_encode($response);
}
?>