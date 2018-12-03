<?php
header('Content-Type: application/json');
$response = array();

if (isset($_POST['user_id']) && isset($_POST['user_name']) && isset($_POST['user_email']) && isset($_POST['user_password'])) {
	
	$user_id = $_POST['user_id'];
    $user_name = $_POST['user_name'];
    $user_email = $_POST['user_email'];
    $user_password = $_POST['user_password'];
	
	require_once '../conf/db_connect.php';
	$con = new DB_CONNECT();
	$con = $con->get_connect();

	$result = $con->query("UPDATE users SET user_name = '$user_name', user_email = '$user_email', user_password= '$user_password' WHERE user_id = $user_id");

	
	if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Product successfully updated.";
 
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