<?php
header('Content-Type: application/json');

$response = array();


if (isset($_POST['car_id']) && isset($_POST['user_id']) && isset($_POST['start_date']) && isset($_POST['end_date'])) {
 
    $car_id = $_POST['car_id'];
    $user_id = $_POST['user_id'];
	$start_date = $_POST['start_date'];
	$end_date = $_POST['end_date'];
  
    require_once '../conf/db_connect.php';

	$con = new DB_CONNECT();
	$con = $con->get_connect();

 
    $result = $con->query("INSERT INTO orders (car_id, user_id, start_date, end_date) 
					VALUES('$car_id', '$user_id', '$start_date', '$end_date')");
 
    if ($result) {

        $response['success'] = 1;
        $response['message'] = 'Order successfully created';
 
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