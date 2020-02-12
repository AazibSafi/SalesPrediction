<?php

class DB {
	
	protected $db_name = 'business_intelligence_db';
	protected $db_user = 'root';
	protected $db_pass = '';
	protected $db_host = 'localhost';
	public $con;
	
	// Open a connect to the database.
	// Make sure this is called on every page that needs to use the database.
	
	public function connect() {
	
		$connect_db = new mysqli( $this->db_host, $this->db_user, $this->db_pass, $this->db_name );
		
		if ( mysqli_connect_errno() ) {
			echo "Connection failed: %s", mysqli_connect_error();
			exit();
		}
		$this->con = $connect_db;
		return true;
	}

	public function disconnect() {

	}

}