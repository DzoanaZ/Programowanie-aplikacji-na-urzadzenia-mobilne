<?php
 
class DB_CONNECT {
	
	private $con;
 

    function __construct() 
	{
		require_once 'db_config.php';
		
		$this->con = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

		if($this->con->connect_errno > 0) {
			die('Unable to connect to database [' . $this->con->connect_error . ']');
		}	
    }
 
    public function get_connect() {
        return $this->con;
    }
 
    public function close() {       
		mysqli_close($this->con);
    }
 
}
 
?>