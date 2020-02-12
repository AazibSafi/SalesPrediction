<?php
	require_once("SalesRestHandler.php");
			
	$req = "";
	if(isset($_GET["req"]))
		$req = $_GET["req"];
/*
controls the RESTful services
URL mapping
*/
	switch($req) {
		
		case "generatesample";
			$salesRestHandler = new SalesRestHandler();
			$salesRestHandler->generateSampleEntries();
			break;

		case "all":
			// to handle REST Url /mobile/list/
			$salesRestHandler = new SalesRestHandler();
			$salesRestHandler->getAllSales();
			break;
			
		case "checkUpdates":
			// to handle REST Url /mobile/show/<id>/
			$salesRestHandler = new SalesRestHandler();
			$salesRestHandler->checkUpdates($_GET["dbversion"]);
			break;

		case "" :
			//404 - not found;
			break;
	}
?>
