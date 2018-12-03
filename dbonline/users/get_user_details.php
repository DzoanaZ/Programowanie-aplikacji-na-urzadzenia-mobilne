<?php
header('Content-Type: application/json');
 
$response = array();

require_once '../conf/db_connect.php';

$con = new DB_CONNECT();
$con = $con->get_connect();
	

if (isset($_GET["user_id"])) {
	$user_id = $_GET['user_id'];
 
	$result = $con->query("SELECT * FROM users WHERE user_id = $user_id");
 
	if (!empty($result)) {
		if ($result->num_rows > 0) {
 
			$result = mysqli_fetch_array($result);
 
			$user = array();
			$user["user_id"] = $result["user_id"];
			$user["user_name"] = $result["user_name"];
			$user["user_email"] = $result["user_email"];
			$user["user_password"] = $result["user_password"];

			$response["success"] = 1;
 
			$response["user"] = array();
 
			array_push($response["user"], $user);
 
			echo json_encode($response);
		} else {
			// no product found
			$response["success"] = 0;
			$response["message"] = "No user found";
			
			echo json_encode($response);
		}
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