<?php
require_once("DB.php");

class DBHandler {

	public $salesTable = 'sales';
	public $lookupTable = 'lookup';

	function __construct() {
	  
	}

	function getAllSales() {
		$db = new DB();
	   	$db->connect();

		$sql = "SELECT * FROM ".$this->salesTable;
		$result=mysqli_query($db->con,$sql);

		$total_sales = array();

	  	while( $obj=mysqli_fetch_object($result) ) {
	  		$sale = array();
	  		$sale['id'] = $obj->id;
	  		$sale['Company'] = $obj->Company;
	  		$sale['Product'] = $obj->Product;
	  		$sale['Region'] = $obj->Region;
	  		$sale['Amount'] = $obj->Amount;
	  		$sale['Sale_Date'] = $obj->Sale_Date;

	  		$total_sales[] = $sale;
	  	}

		$Data = array('latest_DB_version' => $this->getLatestDBVersion()
						,'TotalSales' => $total_sales);

		mysqli_free_result($result);

		$db->con->close();

		return $Data;
	}

	function getLatestDBVersion() {
		$db = new DB();
	   	$db->connect();

		$sql = "SELECT Max(db_version) as latest_version FROM ".$this->lookupTable;
		
		$result=mysqli_query($db->con,$sql);
		$data = mysqli_fetch_object($result);

		$db->con->close();

		return $data->latest_version;
	}

	function insertSales($params) {

		$db = new DB();
	   	$db->connect();

		$sql = "INSERT INTO ".$this->salesTable." values("
				.$params['id']
				.','."'".$params['company']."'"
				.','."'".$params['product']."'"
				.','."'".$params['region']."'"
				.','.$params['amount']
				.','."'".$params['sale_date']."'"
			.")";
		
		mysqli_query($db->con,$sql);
		$db->con->close();
	}

	function clearDB() {
		return ( $this->deleteTable($this->salesTable)=== TRUE );
	}

	function deleteTable($table) {
		$db = new DB();
	   	$db->connect();

		$sql = "DELETE FROM ".$table;
		
		$result = mysqli_query($db->con,$sql);

		$db->con->close();

		return $result;
	}
	function updateLookUp() {
		$latestVersion = $this->getLatestDBVersion();
		$latestVersion++;

		$db = new DB();
	   	$db->connect();

		$sql = "INSERT INTO ".$this->lookupTable." VALUES(".$latestVersion.",".$latestVersion.")";
		
		$result = mysqli_query($db->con,$sql);

		$db->con->close();

		return $result;
	}

	function CreateSchema($params) {
		$this->createSaleTable();
		$this->createLookUpTable();
	}
	
	function createSaleTable() {
		$db = new DB();
	   	$db->connect();

		$sql = "CREATE TABLE ".$this->salesTable." (
				  `id` INT(11) NOT NULL AUTO_INCREMENT,
				  `Company` VARCHAR(255) NOT NULL,
				  `Product` VARCHAR(255) NOT NULL,
				  `Region` VARCHAR(255) NOT NULL,
				  `Amount` INT(11) NOT NULL,
				  `Sale_Date` DATE NOT NULL,
				  PRIMARY KEY (`id`)
				) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;";

		mysqli_query($db->con,$sql);
		$db->con->close();
	}
	function createLookUpTable() {
		$db = new DB();
	   	$db->connect();

		$sql = "CREATE TABLE ".$this->lookupTable." (
				  `id` INT(11) NOT NULL AUTO_INCREMENT,
				  `db_version` INT(11) NOT NULL,
				  PRIMARY KEY (`id`)
				) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;";

		mysqli_query($db->con,$sql);
		$db->con->close();
	}
}

?>