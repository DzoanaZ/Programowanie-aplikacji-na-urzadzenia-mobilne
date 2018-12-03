<?php
 header('Content-Type: application/json');

$response = array();


if (isset($_POST['user_name']) && isset($_POST['user_email']) && isset($_POST['user_password'])) {
 
    $user_name = $_POST['user_name'];
    $user_email = $_POST['user_email'];
    $user_password = $_POST['user_password'];
  
    require_once '../conf/db_connect.php';

	$con = new DB_CONNECT();
	$con = $con->get_connect();

 
    $result = $con->query("INSERT INTO users(user_name, user_email, user_password) VALUES('$user_name', '$user_email', '$user_password')");
 
    if ($result) {

        $response['success'] = 1;
        $response['message'] = 'User successfully created';
 
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