<?php
require_once("SimpleRest.php");
require_once("Database/DBHandler.php");
require_once('SampleConstants.php');
		
class SalesRestHandler extends SimpleRest {

	function getAllSales() {

		$dbHandler = new DBHandler();
		$rawData = $dbHandler->getAllSales();

		$this->returnResponse($rawData);
	}

	function checkUpdates($dbversion) {
		$dbHandler = new DBHandler();
		$rawData = $dbHandler->getLatestDBVersion();
		
		if($rawData > $dbversion)
			$Data = array('UpdateAvailable' => 'true');
		else
			$Data = array('UpdateAvailable' => 'false');
			
		$this->returnResponse($Data);
	}

	function generateSampleEntries( ) {

		$dbHandler = new DBHandler();

		if( $dbHandler->clearDB() === true ) {

			$entryCount = 0;

			$sample = new SampleConstants();

			foreach ($sample->getCompanies() as $compInd => $company) {
				
				foreach ($company['products'] as $prodInd => $product) {
					
					foreach ($sample->getRegionsList() as $regInd => $region) {
						
						$yearDetails = $sample->getYearDetails();

						for($i=$yearDetails['min'];$i<=$yearDetails['max'];$i++) {

							for($month=1;$month<=$yearDetails['months'];$month++) {
								
								$params['id'] = $entryCount;
								$params['company'] = $company['name'];
								$params['product'] = $product;
								$params['region'] = $region;
								$params['amount'] = rand($sample->getMinAmount(),$sample->getMaxAmount());
								$params['sale_date'] = $i.'-'.$month.'-'.'01';

								$dbHandler->insertSales($params);

								$entryCount++;
							}
						}
					}
				}
			}
			$dbHandler->updateLookUp();
			$Data = array('SampleGenerated' => 'true');
		}
		else{
			$Data = array('SampleGenerated' => 'false');
		}
		$this->returnResponse($Data);
	}

	function returnResponse($rawData) {
		if(empty($rawData)) {
			$statusCode = 404;
			$rawData = array('error' => 'No Sales found!');		
		} else {
			$statusCode = 200;
		}

		// $requestContentType = $_SERVER['HTTP_ACCEPT'];
		$requestContentType = 'application/json';
		// $requestContentType = 'text/html';

		$this ->setHttpHeaders($requestContentType, $statusCode);
				
		if(strpos($requestContentType,'application/json') !== false){
			$response = $this->encodeJson($rawData);
			echo $response;
		}
		// else if(strpos($requestContentType,'text/html') !== false){
		// 	$response = $this->encodeHtml($rawData);
		// 	echo $response;
		// } else if(strpos($requestContentType,'application/xml') !== false){
		// 	$response = $this->encodeXml($rawData);
		// 	echo $response;
		// }
		else
			echo "Invalid Request Content Type: ".$requestContentType;
	}
	
	public function encodeHtml($responseData) {
	
		$htmlResponse = "<table border='1'>";
		foreach($responseData as $key=>$value) {
    			$htmlResponse .= "<tr><td>". $key. "</td><td>". $value. "</td></tr>";
		}
		$htmlResponse .= "</table>";
		return $htmlResponse;		
	}
	
	public function encodeJson($responseData) {
		$jsonResponse = json_encode($responseData);
		return $jsonResponse;		
	}
	
	public function encodeXml($responseData) {
		// creating object of SimpleXMLElement
		$xml = new SimpleXMLElement('<?xml version="1.0"?><mobile></mobile>');
		foreach($responseData as $key=>$value) {
			$xml->addChild($key, $value);
		}
		return $xml->asXML();
	}
}
?>