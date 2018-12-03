<?php
header('Content-Type: application/json');

$response = array();

require_once '../conf/db_connect.php';
$con = new DB_CONNECT();
$con = $con->get_connect();

$result = $con->query("SELECT * FROM users");

if($con->error) {
	die('Unable to read row from DB [' . $con->error . ']');
}

if($result->num_rows > 0) {
	$response["users"] = array();
	
	while ($row = mysqli_fetch_array($result)) {
        $user = array();
        $user["user_id"] = $row["user_id"];
        $user["user_name"] = $row["user_name"];
		$user["user_email"] = $row["user_email"];
        $user["user_password"] = $row["user_password"];

        array_push($response["users"], $user);
    }
	
	$response["success"] = 1;
	
	echo json_encode($response);
} else {
	$response["success"] = 0;
    $response["message"] = "No products found";
 
    echo json_encode($response);
}

?>